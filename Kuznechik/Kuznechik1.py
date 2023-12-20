'''
                                        Implementation Of GOST - R- 3412-2015 (Kuznechik)  


Author: AjayBadrinath
Date :  20-12-23
Version :1.0
                Version Changelog: (04-12-23)
                    1.Run Unit Test 
                    2.Yet to comment code

'''

class Kuznechik:
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
        self.message=message
        self.key=key

    
    def R_Transformation(self,x):
        return ((self.linear_Transformation(x)<<120)^(x>>8))
    
    def R_Transformation_inverse(self,x):
        return((x<<8)^self.linear_Transformation(x<<8^((x>>120)&0xff)))
    
    def L_Transformation(self,x):
        for _ in range(16):
            x=self.R_Transformation(x)
        return x
    
    def L_Transformation_inverse(self,x):
        
        for _ in range(16):
            x=self.R_Transformation_inverse(x)
        return x&self.MASK_32
    
    def F_Transformation(self,c1,k1,k2):
        return [self.L_Transformation(self.S_Transformation(c1^k1))^k2,k1]
    
    def S_Transformation(self,x):
        s=0
        for i in range(15,-1,-1):
            s<<=8
            s^=(self.pi[(x>>8*i&0xff)])
            
        return s
    
    def S_Inv_Transformation(self,x):
        s=0
        for i in range(15,-1,-1):
            s<<=8
            s^=(self.pi_inv[(x>>8*i&0xff)])
            
        return s
    
    def M(self,x,y):
        c=0
        deg=0
        while y!=0:
            if(y&1):
                
                c^=(x<<deg)
            deg+=1
            y>>=1
        return c
    
    def degree_Poly(self,x):
        deg=0
        while x!=0:
            deg+=1
            x>>=1
        return deg
    
    def Mod_Polynomial_Reduction(self,x,m):
        z=x
        while True:
            if(self.degree_Poly(z)<self.degree_Poly(m)):
                break
            else:
                diff=self.degree_Poly(z)-self.degree_Poly(m)
                z^=(m<<diff)
            
        return z
    
    def linear_Transformation(self,x):
        _map_=[148,32,133,16,194,192,1,251,1,192,194,16,133,32,148,1][::-1]
        res=0

        for i in range(15,-1,-1):
            res^=self.Mod_Polynomial_Reduction(self.M(_map_[i],(x>>8*i)&0xff),0b111000011)
        return res
    

   
    def Round_constant(self,round_no):
        return (self.L_Transformation(round_no))
    
    def Key_Schedule(self,key):
        
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
        a=self.message
        k=self.Key_Schedule(self.key)
        for i in range(9):
            
            a=self.L_Transformation(self.S_Transformation(k[i]^a))

        return a^k[-1]
    

    def decrypt(self,cipher):

        pt=cipher
        k=self.Key_Schedule(self.key)

        for i in (range(9,0,-1)):
            pt=self.S_Inv_Transformation(self.L_Transformation_inverse(k[i]^pt))

            #print(hex(pt))

        return pt^k[0]
        
        




