'''
                                        Implementation Of GOST - R- 3412-2015 (Kuznechik)  


Author: AjayBadrinath
Date :  20-12-23
Version :1.0
                Version Changelog: (27-12-23)
                    1.Run Unit Test 
                    2.Added Comments
                    3.Yet to Benchmark w.r.t -> Enc/Dec Speed && Mem Usage 

'''
'''


    Class Implementing the Kuznechik Cipher . This is a cipher Proposed by the Russian Security Standard GOST.
    
    The Technical Specifications of this cipher are in the pdf archive in Russian :
    https://web.archive.org/web/20150924113434/http://tc26.ru/standard/gost/GOST_R_3412-2015.pdf
    
    Compared to the previous implementation of the GOST(MAGMA) Cipher which is of 64 bit block,
    
    Kuznechik is a 128 bit block cipher unlike its cousin MAGMA it is rather based on SP Network rather than the 
    Fiestel System .

    Note: Reading the source and understanding the same WILL Require Some Elementary Knowledge of Galois Field Arithmetic + Number Theory . 
    
    Some transformations are done using the same Pls Refer:
    
        1.https://math.stackexchange.com/questions/245621/arithmetic-operations-in-galois-field
        2.https://en.wikipedia.org/wiki/Kuznyechik
        3.The Russian Spec itself
        4.https://stackoverflow.com/questions/13202758/multiplying-two-polynomials
    

'''


class Kuznechik:
    '''
    
        This class encapsulates all the constants and the iterators in one single class module.
        Although for the sake of readablity i should have made exported them as a constants module separately. 
        
        Constants:
           
            
            Since this cipher is designed on the lines of Being a SP Network Cipher we have these s-boxes / permute-boxes.
           
            pi     : Used as non-linear bijective transformation    
            
            pi_inv : Used as non-linear bijective transformation
            
            Note   : Mathematically Speaking the inv (fn(x))->x 
                So pi_inv(pi(x))=>x So the pi_inv can safely be called as pi inverse (inv_p-box)
            
            _map   : Iterator Array For the sake of conveinience used for Linear Transformation Constants (c)*nabla(x)
            
            MASK_32: This Acts as MASK as the name suggests to filter bits (32*4-bit )and prevent overflow 
                    ik the name is misleading as it was named after 32 * hex not 32 bits so 128 bits(32*4)
    
    '''
    pi=[252, 238, 221, 17, 207, 110, 49, 22, 251, 196, 250, 218, 35, 197, 4, 77, 233,
    119, 240, 219, 147, 46, 153, 186, 23, 54, 241, 187, 20, 205, 95, 193, 249, 24, 101,
    90, 226, 92, 239, 33, 129, 28, 60, 66, 139, 1, 142, 79, 5, 132, 2, 174, 227, 106, 143,
    160, 6, 11, 237, 152, 127, 212, 211, 31, 235, 52, 44, 81, 234, 200, 72, 171, 242, 42,
    104, 162, 253, 58, 206, 204, 181, 112, 14, 86, 8, 12, 118, 18, 191, 114, 19, 71, 156,
    183, 93, 135, 21, 161, 150, 41, 16, 123, 154, 199, 243, 145, 120, 111, 157, 158, 178,
    177, 50, 117, 25, 61, 255, 53, 138, 126, 109, 84, 198, 128, 195, 189, 13, 87, 223,
    245, 36, 169, 62, 168, 67, 201, 215, 121, 214, 246, 124, 34, 185, 3, 224, 15, 236,
    222, 122, 148, 176, 188, 220, 232, 40, 80, 78, 51, 10, 74, 167, 151, 96, 115, 30, 0,
    98, 68, 26, 184, 56, 130, 100, 159, 38, 65, 173, 69, 70, 146, 39, 94, 85, 47, 140, 163,
    165, 125, 105, 213, 149, 59, 7, 88, 179, 64, 134, 172, 29, 247, 48, 55, 107, 228, 136,
    217, 231, 137, 225, 27, 131, 73, 76, 63, 248, 254, 141, 83, 170, 144, 202, 216, 133,
    97, 32, 113, 103, 164, 45, 43, 9, 91, 203, 155, 37, 208, 190, 229, 108, 82, 89, 166,
    116, 210, 230, 244, 180, 192, 209, 102, 175, 194, 57, 75, 99, 182]
    pi_inv=[
        165, 45, 50, 143, 14, 48, 56, 192, 84, 230, 158,
        57, 85, 126, 82, 145, 100, 3, 87, 90, 28, 96,
        7, 24, 33, 114, 168, 209, 41, 198, 164, 63, 224,
        39, 141, 12, 130, 234, 174, 180, 154, 99, 73, 229,
        66, 228, 21, 183, 200, 6, 112, 157, 65, 117, 25,
        201, 170, 252, 77, 191, 42, 115, 132, 213, 195, 175,
        43, 134, 167, 177, 178, 91, 70, 211, 159, 253, 212,
        15, 156, 47, 155, 67, 239, 217, 121, 182, 83, 127,
        193, 240, 35, 231, 37, 94, 181, 30, 162, 223, 166,
        254, 172, 34, 249, 226, 74, 188, 53, 202, 238, 120,
        5, 107, 81, 225, 89, 163, 242, 113, 86, 17, 106,
        137, 148, 101, 140, 187, 119, 60, 123, 40, 171, 210,
        49, 222, 196, 95, 204, 207, 118, 44, 184, 216, 46,
        54, 219, 105, 179, 20, 149, 190, 98, 161, 59, 22,
        102, 233, 92, 108, 109, 173, 55, 97, 75, 185, 227,
        186, 241, 160, 133, 131, 218, 71, 197, 176, 51, 250,
        150, 111, 110, 194, 246, 80, 255, 93, 169, 142, 23,
        27, 151, 125, 236, 88, 247, 31, 251, 124, 9, 13,
        122, 103, 69, 135, 220, 232, 79, 29, 78, 4, 235,
        248, 243, 62, 61, 189, 138, 136, 221, 205, 11, 19,
        152, 2, 147, 128, 144, 208, 36, 52, 203, 237, 244,
        206, 153, 16, 68, 64, 146, 58, 1, 38, 18, 26,
        72, 104, 245, 129, 139, 199, 214, 32, 10, 8, 0,
        76, 215, 116 
        ]
    MASK_32=0xffffffffffffffffffffffffffffffff       

    def __init__(self,message,key):
        '''
            Default Class Constructor . Implicitly called everytime the class is passed with an arguement.
            Parameters:
                Message:(128-bit)[ 1 block  ]
                Key:256 bit key
        
        '''
        self.message=message
        self.key=key

    
    def R_Transformation(self,x):
        '''
            Tag: [Used in Encryption]
            Function to perform R Transformation in the domain(V(128)->V(128))
            
            Parameters:
                 x: 128 bit output from applying S-box transformation
            
            Returns:
                 L(x(15)...x(0))||x(15)....x(1) -> 128 bit 
        
        '''
        return ((self.linear_Transformation(x)<<120)^(x>>8))
    
    def R_Transformation_inverse(self,x):
        '''
            Tag:[ Used in Decryption ]
            Function to perform the inverse of R_Transformation in the domain(V(128)->V(128)) 

            Parameters:
                x: 128 bit input from L_Transformation_inverse function 
            Returns   :
                x(14)||x(13)...||x(0)||L(x(14)||x(13)||...||x(0)||x(15)) -> 128 bit 

        '''
        return((x<<8)^self.linear_Transformation(x<<8^((x>>120)&0xff)))
    
    def L_Transformation(self,x):
        '''
            Tag: [ Used in Encryption ]
            Another Native Function to Kuznechik cipher to Apply R_Transformation func 16 times with the same domain. 
            
            Parameters:
                x: 128 bit input for which R_Transformation function has to be applied
            
            Returns:
                R_Transformation(R_Transformation....(x)) iterated 16 times -> 128 bits 

        '''
        for _ in range(16):
            x=self.R_Transformation(x)
        return x
    
    def L_Transformation_inverse(self,x):
        '''
            Tag: [ Used in Decryption ]
            Native function that Applies the inverse of the L_Transformation by implicitly calling the R_Transformation_inverse function. Same domain Mapping.
            
            Parameters:
                x: 128 bit input for which R_Transformation_inverse function has to be applied
            Returns:
                (R_Transformation_inverse(R_Transformation_inverse(....(x))) iterated 16 times) -> 128 bits 
                

        '''
        for _ in range(16):
            x=self.R_Transformation_inverse(x)
        return x&self.MASK_32
    
    def F_Transformation(self,c1,k1,k2):
        '''
            Tag:[Used in Key Schedule]
            Native function for Key Generation or Key Schedule Generation . 
            If you notice Interstingly this cipher follows a pattern of Fiestel Network for Key Schedule.
            Parameters:
                Key1:256 bit keys (Apply Combinations of Transformations to this )
                Key2:256 bit keys  (Nothing done here . Just return the same key as is )
                Round_Constant: Passed implicitly from the Round_constant function defined.
            Returns: 
                Array(L(S(roundconst^k1)),k2)


        '''
        return [self.L_Transformation(self.S_Transformation(c1^k1))^k2,k1]
    
    def S_Transformation(self,x):
        '''
            Tag:[Used in Encryption]
            Function to Apply pi(p-box )to the input in the domain (V(128)->V(128)).

            Parameters:
                x: 128 bit input to Be substituted with pi
            Returns   :
                pi(x) with each nibble .



        '''

        s=0
        for i in range(15,-1,-1):
            s<<=8
            s^=(self.pi[(x>>8*i&0xff)])
            
        return s
    
    def S_Inv_Transformation(self,x):

        '''
            Tag:[ Used in Decryption ]
            Function to Apply pi_inv(p_inv-box )to the input in the domain (V(128)->V(128)). 

            Parameters:
                x: 128 bit input to Be substituted with pi_inv
            Returns   :
                pi_inv(x) with each nibble .

        '''
        s=0
        for i in range(15,-1,-1):
            s<<=8
            s^=(self.pi_inv[(x>>8*i&0xff)])
            
        return s
    
    def M(self,x,y):
        '''
            Tag:[Misc.]
            The Delta Function Defined in the GOST Spec .
            Utility Function to Multiply two Polynomials in binary to put it simply.
            Essentially applies (x<< degree y)+(x<<degree  y)...where + refers to xor operation
            Parameters:
                x: Binary Polynomial 
                y: Binary Polynomial
            Returns:
                x*y -> Product of two polynomials.

        '''
        c=0
        deg=0
        while y!=0:
            if(y&1):
                
                c^=(x<<deg)
            deg+=1
            y>>=1
        return c
    
    def degree_Poly(self,x):
        '''
            Tag:[Misc]
            This is a helper function for Mod_Reduction. Computes the highest degree for a given polynomial:
            eg:11010 -> 4

            Parameters:
                x: A binary Number / Polynomial Representation 
            Returns:
                Highest Degree for the given polynomial

        '''
        deg=0
        while x!=0:
            deg+=1
            x>>=1
        return deg
    
    def Mod_Polynomial_Reduction(self,x,m):

        '''
            Tag:[Misc]
            Function to compute Mod for a given binary Polynomial in the field of GF(2) F->{0,1}
            Essentially iterate till deg(z) <deg(mod):while performing (m<<diff) whrere diff is the 
            difference between the highest power of z and m itself

            Parameters:
                x:A binary polynomial to be reduced.
                m:Modulus binary Number .(For this Cipher We use )
                GF(2)[x]->p(x)=x**8+x**7+x**6+x+1 (111000011) as mod

            Returns:
                z mod m
            


        '''
        z=x
        while True:
            if(self.degree_Poly(z)<self.degree_Poly(m)):
                break
            else:
                diff=self.degree_Poly(z)-self.degree_Poly(m)
                z^=(m<<diff)
            
        return z
    
    def linear_Transformation(self,x):
        '''
            Tag:[Used in Encryption][Used in Decryption]
            Transformation proposed in the GOST Specification with Mapping Domain : V(8) ->V(8) 
            Apply nabla(_map[iter]*delta(x))
            Parameter:
                x: input to transform
            Returns:
                LinearTransformation(x(15)||...||x(0))
            



        '''
        _map_=[148,32,133,16,194,192,1,251,1,192,194,16,133,32,148,1][::-1]
        res=0

        for i in range(15,-1,-1):
            res^=self.Mod_Polynomial_Reduction(self.M(_map_[i],(x>>8*i)&0xff),0b111000011)
        return res
    

   
    def Round_constant(self,round_no):
        '''
            Tag:[Used in Key Schedule]
            Utility Function Explicitly Written for sake of readablity .
            Computes the RoundConstant c by applying L_Transformation
            Parameters:
                round_no: Number {0...9}
            Returns:
                L_Transformation(round_no) -> 256 bit
        '''

        return (self.L_Transformation(round_no))
    
    def Key_Schedule(self,key):

        '''
            This is the Function to perform Key Deployment from the initial key.
            This Key deployment algorithm uses Fiestel system to Get Separate Keys for each Round/iteration.
            initial keys:k>>128,k&mask_32
            Each set of keys obtained from each iteration are applied the F_Transformation.
            Every 8th iteration keys thus obtained is added to key_list.

            Parameters:
                Key:256 Pseudorandomly generated Key 
            Returns: 
                KeySchedule Array of Size 10

        
        '''
        
        k1=(key>>128)&self.MASK_32
        k2=(key)&self.MASK_32
        key_arr=[k1,k2]
        for i in range(1,33):
            c=self.Round_constant(i)
            f=self.F_Transformation(c,k1,k2)
            k1,k2=f
            if(i%8==0):
                key_arr.extend([k1,k2])
        return key_arr
    

    def encrypt(self):
        '''
            Function To Encrypt Bloc Using Kuznechik Cipher .
            Uses all the function marked as [Used in Encryption] tag in the docstring.
            Parameters:
                None
                Implicitly uses the KeySchedule array for each round and apply L_Transformation(S(round_key^msg))
            Returns:
                128 bit Encrypted Cipher block
        '''
        a=self.message
        k=self.Key_Schedule(self.key)
        for i in range(9):
            
            a=self.L_Transformation(self.S_Transformation(k[i]^a))

        return a^k[-1]
    

    def decrypt(self,cipher):
        '''
            Function To Decrypt Block Using Kuznechik Cipher .
            Uses all the function marked as [Used in Decryption] tag in the docstring.
            Parameters:
                None
                Implicitly uses the KeySchedule array for each round and apply (S_inv(L_Transformation_inverse(round_key^cipher)))
            Returns:
                128 bit Decrypted Cipher block
        '''
        pt=cipher
        k=self.Key_Schedule(self.key)

        for i in (range(9,0,-1)):
            pt=self.S_Inv_Transformation(self.L_Transformation_inverse(k[i]^pt))

            #print(hex(pt))

        return pt^k[0]
        
        


