'''

                        Practical Implementation of Fiat Shamir Protocol Using Sockets - TCP 

                        
Author : AjayBadrinath
Date   : 27/11/2023
Version: 1.0


'''
import os
import socket as s
import ctypes as c

s1=os.path.dirname(os.path.abspath(__file__))
p1=os.path.dirname(os.path.dirname(s1))
fp="/FiatShamirLib/fiat.so"


lib=c.CDLL(p1+fp)
host="127.0.0.1"
port=65000

'''
Boiler plate Standard Server Code .


'''
class fsp_recv(c.Structure):
	_fields_ =[
		("prime1",c.c_longlong),
		("prime2",c.c_longlong),
		("n",c.c_longlong),
		("challenge",c.c_longlong),
		("PUBLIC_KEY",c.c_longlong)
	]
reciever=c.pointer(fsp_recv())

with s.socket(s.AF_INET,s.SOCK_STREAM) as s:
    s.bind((host,port))
    s.listen()
    
    while True:
        connectHandler,address=s.accept()
        with connectHandler:
            while True:
                n=lib.Setup_reciever(reciever)
                print(f"N:{n}\n")
                connectHandler.sendall(n.to_bytes(512,"big"))
                pk=connectHandler.recv(512)
                pk=int.from_bytes(pk,"big")
                print(f"Public Key:{pk}\n")
                x=connectHandler.recv(512)
                x=int.from_bytes(x,"big")
                print(f"Commit Phase X:{x}\n")
                challenge=lib.challenge_phase(reciever,pk)
                connectHandler.sendall(challenge.to_bytes(512,"big"))
                y=connectHandler.recv(512)
                y=int.from_bytes(y,"big")
                print(f"Challenge Computed Y:{y}\n")
                #l=y**2%reciever.contents.n
                #pl=x*(reciever.contents.PUBLIC_KEY**reciever.contents.challenge)%reciever.contents.n
                #print(l,pl)
                if(lib.verification_phase(reciever,x,y)):
                     connectHandler.sendall(b"Authenticated")
                else:
                     connectHandler.sendall(b"Not Authenticated")
                break
            break
                
