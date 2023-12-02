
'''
                                        Implementation Of GOST - R- 3412-2015 (MAGMA)
Author: AjayBadrinath
Date :  03-12-23
Vesion :1.1
                Version Changelog:
                    1.Wrapped functions with class for Code Modularity
                    2.Comments Yet to be added...
                    3.Yet To perform Unit Test.

'''

#Rough Sketch of GOST CIPHER IN PYTHON 
'''

1.Pre Process the text.
    1. Convert i/p to binary
    2. Pad with Zeros.
    
'''
class GOST_MAGMA:
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

    def __init__(self,message,key):
        self.x=message
        self.k=key
    def rol(self,x):
        return ((x<<11)^(x>>(32-11)))&0xffffffff
    def Transform(self,x):
        z=0
        #0xcafebabe-> extract bits from MSB to LSB 
        for i in range(7,-1,-1):
            z=z<<4
            z^=self.s_box[i][x>>(4*i)&0xf]
        return z
    
    def g_function(self,x,k):
        return self.rol(self.Transform((x+k)%2**32))
    #Key Schedule input Key is  256 bits .
    def KeySchedule(self):
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
        l1=[]
        #print(len(l[0]))
        for i in range(len(l[0])):
            for j in l:
                l1.append(j[i])
        
        return l1
    
    #a is a 64 bit plaintext, k is the initial 256-bit key
    def encrypt(self):
        key=self.flatten(self.KeySchedule())
        Left=(self.x>>32)&0xffffffff
        Right=(self.x&0xffffffff)
        #print(hex(Left),hex(Right))
        for i in range(31):
            (Left,Right)= (Right,Left^self.g_function(Right,int(key[i],16)))
        
        return ((Left^self.g_function(Right,int(key[-1],16)))<<32 )^Right



    def decrypt(self):
        key=self.flatten(self.KeySchedule())
        Left=(self.x>>32)&0xffffffff
        Right=(self.x&0xffffffff)
        #print(hex(Left),hex(Right))
        for i in range(31,0,-1):
            (Left,Right)= (Right,Left^self.g_function(Right,int(key[i],16)))
        return ((Left^self.g_function(Right,int(key[0],16)))<<32 )^Right










s=GOST_MAGMA(0x4ee901e5c2d8ca3d,0xffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff)
print(hex(s.decrypt()))

'''

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