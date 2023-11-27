'''

                        Practical Implementation of Fiat Shamir Protocol Using Sockets - TCP 

                        
Author : AjayBadrinath
Date   : 27/11/2023
Version: 1.1


'''
'''


Important -> If you are spinning of your own version .Pls include the .so file generated from the makefile in the FiatShamirLib Folder
Also This was Compiled using Linux . If you are Running Windows Please Edit the MakeFile and use MINGW or whatever compiler to generate native .dll support.


'''

import os
import socket as s
import ctypes as c

s1=os.path.dirname(os.path.abspath(__file__))
p1=os.path.dirname(os.path.dirname(s1))
fp="/FiatShamirLib/fiat.so"

lib=c.CDLL(p1+fp)

'''Class Replication of The Structure Defined in the Library:'''

class fsp_snd(c.Structure):
	_fields_ =[
		("n",c.c_longlong),
		("rand",c.c_longlong),
		("x",c.c_longlong),
		("y",c.c_longlong),
		("secret",c.c_longlong),
		("PUBLIC_KEY",c.c_longlong),
	]


host="127.0.0.1"
port=65000
sender=c.pointer(fsp_snd())


with s.socket(s.AF_INET,s.SOCK_STREAM) as l:
    '''
                
                Since this is a POC I am not Porting This as a Separate Function Even though It could have ....
                
                Our Implementation Handles Only lli currently so we properly typecast our python objects to the same and vice versa

    '''
    
    l.connect((host,port))
    
    n=l.recv(512)
    n=int.from_bytes(n,"big")
    print(f"N:{n}\n")
    pk=lib.Setup_sender(sender,n,3)
    print(f"Public Key:{pk}\n")
    l.sendall(pk.to_bytes(512,"big"))
    x=lib.commit_phase(sender)
    l.sendall(x.to_bytes(512,"big"))
    challenge=l.recv(512)
    challenge=int.from_bytes(challenge,"big")
    print(f"Challenge recieved :{challenge}\n")
    y=lib.response_phase(sender,challenge)
    l.sendall(y.to_bytes(512,"big"))
    print(str(l.recv(512)))

