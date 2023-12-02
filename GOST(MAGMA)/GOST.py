
'''
                                        Implementation Of GOST - R- 3412-2015 (MAGMA)
author: AjayBadrinath
date :  03-12-23


'''

#Rough Sketch of GOST CIPHER IN PYTHON 
'''

1.Pre Process the text.
    1. Convert i/p to binary
    2. Pad with Zeros.
    
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


#Key Schedule input Key is  256 bits .
def KeySchedule(key):
    k_split=[]
    k_sequence=[]
    MASK=0xffffffff
    for i in  range(7,-1,-1):
        k_split.append(hex(key>>(32*i)&MASK))
    #print(k_split)
    #order asc->asc->asc->desc
    for i in range(8):
        k_sequence.append([k_split[i],k_split[i],k_split[i],k_split[len(k_split)-i-1]])
    return k_sequence

def rol(x):
    return ((x<<11)^(x>>(32-11)))&0xffffffff


def g_function(x,k):
    return rol(Transform((x+k)%2**32))

def Transform(x):
    z=0
    #0xcafebabe-> extract bits from MSB to LSB 
    for i in range(7,-1,-1):
        z=z<<4
        z^=s_box[i][x>>(4*i)&0xf]
    return z


def flatten(l:list):
    l1=[]
    #print(len(l[0]))
    for i in range(len(l[0])):
        for j in l:
            l1.append(j[i])
    
    return l1

#a is a 64 bit plaintext, k is the initial 256-bit key
def encrypt(a,k):
    key=flatten(KeySchedule(k))
    Left=(a>>32)&0xffffffff
    Right=(a&0xffffffff)
    print(hex(Left),hex(Right))
    for i in range(31):
       (Left,Right)= (Right,Left^g_function(Right,int(key[i],16)))
       
    return ((Left^g_function(Right,int(key[-1],16)))<<32 )^Right

def decrypt(c,k):
    key=flatten(KeySchedule(k))
    Left=(c>>32)&0xffffffff
    Right=(c&0xffffffff)
    print(hex(Left),hex(Right))
    for i in range(31,0,-1):
       (Left,Right)= (Right,Left^g_function(Right,int(key[i],16)))
    return ((Left^g_function(Right,int(key[0],16)))<<32 )^Right



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
