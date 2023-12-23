'''

                        FileCrypt Tool Using Kuznechik Block Cipher

                    
Author : Ajay Badrinath
Date   : 23-12-23

                    Version Changelog:
                            1.Yet To add comments 
                            2.Yet to Refactor 
                            


'''
import Kuznechik1 as k
import codecs
class FileCrypt:
    
    def __init__(self,message,key):
        self.message=message
        self.key=key
        #self.file=FileHandle
        
    def PKCS7(self,block_size):
       b_msg = self.message
       pad=(block_size-len(b_msg))%block_size
       b_msg+=bytes([pad]*pad)
       return b_msg
    
    def remove_PKCS7(self,b_msg):
        return b_msg[:-b_msg[-1]]
      
    def File_Encrypt(self):
        
        #no_blocks=1
        f=open(r"E:\Cryptography\FileCrypt\testfile.txt","rb")
        f1=open(r"E:\Cryptography\FileCrypt\testfile1.txt","wb")
        pt=f.read(16*1024)
        i=0
        while pt:
            buf=bytes()
            try:
                #print(pt)
                
                for _ in range(0,len(pt),16):
                    
                    self.message=pt[_:_+16]
                    pad= self.PKCS7(128//8)
                    a=k.Kuznechik(int(codecs.encode(pad,'hex'),16),self.key)
                    encrypted=(a.encrypt())
                    #print(hex(encrypted))
                    hex_str=hex(encrypted)[2:]
                
                    if(len(hex_str)!=0x20):
                        hex_str="0"*(0x20-len(hex_str))+hex_str
                    buf+=bytes(bytes.fromhex(hex_str))
                #print(buf)
                to_write=buf
                #print(buf)
                f1.write(to_write)
                pt=f.read(16*1024)
            except ValueError:
                break
        f.close()
        f1.close()
        
    
    def File_decrypt(self):
        f1=open(r"E:\Cryptography\FileCrypt\testfile2.txt","wb")
        f=open(r"E:\Cryptography\FileCrypt\test.txt","rb")
        ct=f.read(32*1024)
        a=k.Kuznechik(k,self.key)
        
        while ct:
            buf=bytes()
            try:
                s=bytes()
                for _ in range(0,len(ct),16):
                    z= (a.decrypt(int.from_bytes(ct[_:_+16],'big')))

                    s=int.to_bytes(z, length=16, byteorder='big')
                    buf+=s
                s=self.remove_PKCS7(s)
                
                f1.write(buf)
                ct= (f.read(32*1024))
            except ValueError:
                break
        
        f.close()
        f1.close()
        

FileCrypt("D",0x8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef).File_Encrypt()
FileCrypt("D",0x8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef).File_decrypt()
