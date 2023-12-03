
'''
                                        Implementation Of GOST - R- 3412-2015 (MAGMA)  


Author: AjayBadrinath
Date :  03-12-23
Vesion :1.2
                Version Changelog: (04-12-23)
                    1.Wrapped functions with class for Code Modularity
                    2.Added Comments & Docstring.
                    3.Yet To perform Unit Test.

'''

'''

Class implementing GOST - 64 bit Blocks .


    1. This is a Fiestel Cipher Based Encryption on 64 bit blocks as per Specs Defined by Russian  Union  Standards.
    There is not much Information about this Cipher Developed by the Soviet Union During the time when NSA Developed DES- 56. This Remained as an Alternative 
    So Here's the Pdf in Russian :  https://web.archive.org/web/20150924113434/http://tc26.ru/standard/gost/GOST_R_3412-2015.pdf

    
'''

class GOST_MAGMA:


    # SBOX Defined as PI Transformation in the Specification . (8 SBOX)
    
    '''

    According to papers I read .. Seems as though SBOX For GOST is unaffected by SBOX  Related attacks 
    
    
    '''
    s_box=[
	[0xC, 0x4, 0x6, 0x2, 0xA, 0x5, 0xB, 0x9, 0xE, 0x8, 0xD, 0x7, 0x0, 0x3, 0xF, 0x1],
	[0x6, 0x8, 0x2, 0x3, 0x9, 0xA, 0x5, 0xC, 0x1, 0xE, 0x4, 0x7, 0xB, 0xD, 0x0, 0xF],
	[0xB, 0x3, 0x5, 0x8, 0x2, 0xF, 0xA, 0xD, 0xE, 0x1, 0x7, 0x4, 0xC, 0x9, 0x6, 0x0],
	[0xC, 0x8, 0x2, 0x1, 0xD, 0x4, 0xF, 0x6, 0x7, 0x0, 0xA, 0x5, 0x3, 0xE, 0x9, 0xB],
	[0x7, 0xF, 0x5, 0xA, 0x8, 0x1, 0x6, 0xD, 0x0, 0x9, 0x3, 0xE, 0xB, 0x4, 0x2, 0xC],
	[0x5, 0xD, 0xF, 0x6, 0x9, 0x2, 0xC, 0xA, 0xB, 0x7, 0x8, 0x1, 0x4, 0x3, 0xE, 0x0],
	[0x8, 0xE, 0x2, 0x5, 0x6, 0x9, 0x1, 0xC, 0xF, 0x4, 0xB, 0x0, 0xD, 0xA, 0x3, 0x7],
	[0x1, 0x7, 0xE, 0xD, 0x0, 0x5, 0x8, 0x3, 0x4, 0xF, 0xA, 0x6, 0x9, 0xC, 0xB, 0x2]
    ]
    '''
        My implementation Requires the Same Message Variable be Passed upon instantiation of class.
        Message Could be PlainText or Cipher Text. 
        If Either be the case the respective functions have to be called 
        
    '''
    
    def __init__(self,message,key):
        '''

        Parameters  :

                    Message : Plaintext or Cipher Text.
                    Key     : 256 Bit Key From a Trusted KeyGen

        '''
        self.x=message
        self.k=key
    
    def rol(self,x):
        '''

        
        Native Function For Left Circular Shift by 11.
        
        0xffffffff==2**32-1 ->MASK For 32 bit ints.

        Parameters: 32-bit int 
        Returns   : (int32) ->  Circular Shift By 11


        '''
        return ((x<<11)^(x>>(32-11)))&0xffffffff
    
    
    def Transform(self,x):
        ''' 
        Transform Function t(x) : -> Non Linear Bijective Function  
        Extract from MSB to LSB & Substitute from SBox 
        Parameters: 32bit int x
        Return    : t(x)->Mapped as a Non-Linear Bijection w.r.t sbox

        '''
        z=0
        
        #0xcafebabe-> extract bits from MSB to LSB 
        for i in range(7,-1,-1):
            z=z<<4
            z^=self.s_box[i][x>>(4*i)&0xf]
        
        
        return z
    
    '''

    Another Native Function Defined By the Russian Standards . Mandates g_Function that applies (AM(key+msg))>>>11 ,
    where AM= Addition Modulo 2**32
    
    '''
    
    def g_function(self,x,k):
        '''
        Function g_box :
        Parameters:
               x: ( 32bit int )Message
               k: (32 bit int) Round Key
        Returns : 
                (int 32) -> (AM(key+msg))>>>11 
        
        '''
        return self.rol(self.Transform((x+k)%2**32))
    
    '''

    Key Schedule for GOST Which Split 256 bit keys to 32 Sub Key Round for Each Round ..
    
    To Be Specific : 

            The key Schdule First Splits the 256 Bit Keys to 8 sub keys of 32 bit length 
            Then For 
                Round (1->8(incl)) : MSB->LSB (32bit split) ===>Ascending Phase
                Round (9->16(incl)) : MSB->LSB (32bit split) ===> Ascending Phase
                Round (17->24(incl)) : MSB->LSB (32bit split) ===> Ascending Phase
                Round (25->32(incl)) : LSB->MSB (32bit split) ===> Descending Phase
    
    
    '''

    
    #Key Schedule input Key is  256 bits .
    def KeySchedule(self):
        '''
        Function To Generate a Key Schedule For 32 Rounds.
        Parameters: 
            Self-initialised.
        Returns : 
            list[][]:k_Sequence

        '''

        k_split=[]
        k_sequence=[]
        MASK=0xffffffff
        
        for i in  range(7,-1,-1):
            k_split.append(hex(self.k>>(32*i)&MASK))
        #print(k_split)
        #order asc->asc->asc->desc
        
        for i in range(8):
            k_sequence.append([k_split[i],k_split[i],k_split[i],k_split[len(k_split)-i-1]])
        
        
        return k_sequence
    
    
    def flatten(self,l:list):
        '''
        Function To Flatten The 2d-KeySchedule Array For Convienience Sake.

        '''
        l1=[]
        #print(len(l[0]))
        
        for i in range(len(l[0])):
            for j in l:
                l1.append(j[i])
        
        
        return l1
    
    '''
            Fiestel Based Encryption  with Transformations. For 32 Rounds 
    '''
    
    #a is a 64 bit plaintext, k is the initial 256-bit key
    def encrypt(self):
        '''
        Function to Perform Encryption : (This Has to be called For Each Block )
        Parameters: 
            Self Initialised Key && Plaintext passed as Constructor Args when Instantiation of Class
        Returns:
            (int 64): Cipher Text -> c
        '''
        key=self.flatten(self.KeySchedule())

        Left=(self.x>>32)&0xffffffff
        
        Right=(self.x&0xffffffff)
        #print(hex(Left),hex(Right))
        
        for i in range(31):
            (Left,Right)= (Right,Left^self.g_function(Right,int(key[i],16)))
        
        return ((Left^self.g_function(Right,int(key[-1],16)))<<32 )^Right # This is Defined As the G* Function that Essentially Applies the Last key && joins the Bits



    def decrypt(self):

        '''
        Function to Perform Decryption : (This Has to be called For Each Block )
        Parameters: 
            Self Initialised Key && CipherText passed as Constructor Args when Instantiation of Class
        Returns:
            (int 64): Plain Text -> P
        '''
        key=self.flatten(self.KeySchedule())
        
        Left=(self.x>>32)&0xffffffff
        
        Right=(self.x&0xffffffff)
        #print(hex(Left),hex(Right))
        
        for i in range(31,0,-1):
            (Left,Right)= (Right,Left^self.g_function(Right,int(key[i],16)))
        
        return ((Left^self.g_function(Right,int(key[0],16)))<<32 )^Right   # This is Defined As the G* Function that Essentially Applies the Last key && joins the Bits




s=GOST_MAGMA(0x4ee901e5c2d8ca3d,0xffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff)
print(hex(s.decrypt()))

'''
Test Cases...


p=Transform(int("fdb97531",16))
#print(hex(p))
k=int("ffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff",16)
#print(x)
#p2=KeySchedule(x)
#print(p2)
#print(hex(encrypt(0xfedcba9876543210,0xffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff)))
#print(hex(g_function(0x87654321,0xfedcba98)))
print(hex(decrypt(0x4ee901e5c2d8ca3d,0xffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff)))




def convert_binary(a:str):
    binseq=''.join(format(ord(i),"08b") for i in a)
    return binseq
    
#print(convert_binary("a"))
'''