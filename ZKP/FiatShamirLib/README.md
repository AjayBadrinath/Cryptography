# Fiat Shamir Protocol Library

<h2>What's This  ?  </h2>

Fiat Shamir Protocol is one of Zero Knowledge Proof Protocol that doesn't require the sender to send his credentials . Instead One can prove his / her identity 
by using Digital Signatures (sort of)


<h3>Is this Secure ?</h3>

Well Kind of but can't be used in production yet... I mean the implementation is just a Proof Of Concept and Future scope of adding more to it exists




<h3>Implementation Details </h3>

Most of the Details are in the Code itself But I am including the same for Documentation Purposes here...


<ul>
  <li>
    This is intended to be on Authentication of Client -Server over TCP 
  </li>
  <li>
    There is a Provision of 2 Structures  one for the Verifier and other for the prover.

    
  </li>
  
```C
typedef struct FSP_snd{
	
	lli n,rand,x,y;
	lli secret;
	lli PUBLIC_KEY;
}fsp_snd;
```

```C
typedef struct FSP_recv{
	lli prime2, prime1;
	lli n,challenge;
	lli PUBLIC_KEY;
}fsp_recv;
```
</ul>
<li>
  Verifier Native Functions :
</li>

<ul>
  <li>
 
1.Setup Reciever:

Choose Arbitrary prime and send n to prover (Registration Phase )

  
  </li>

  ```C
lli Setup_reciever(fsp_recv*);
```
  <li>
  
2.Challenge Prover:

Challenge Can be any Rand Number

  </li>

  ```C
lli challenge_phase(fsp_recv*,lli);
```

  <li>
    
3.Verify:

Verify the same recieved from Response Phase(Prover) with y**2%n
    
  </li>

  
```C
bool verification_phase(fsp_recv*,lli,lli);
```


  
</ul>






<li>
  Prover Native Functions :
  
  <ul>
  <li>
    
  1.Setup Phase:
		 Prover Has a secret Key : s 
		 And Sends Public Key to Reciever (Verifier)

   
  </li>
    
```C

    lli Setup_sender(fsp_snd*,lli,lli);
 ```
  <li>
    
2.Commit Phase:
		Chooses a random value and bound it by common n and Send the same 
 
  </li>
  
```C

lli  commit_phase(fsp_snd*);

```
  <li>
    
3.Response Phase :
		Respond to the challenge
  
  </li>

```C

lli response_phase(fsp_snd*,lli);

```

  </ul>
</li>




</ul>
  
</ul>

### Build Instructions 

Clone this sub-repository and Maintain the Folder Structure as in this case .

Portablity : This  library can be ported to Python and can be used as is in  Vanilla C

 For Testing Modify the test.c File and Run:
 ```make
make  test
```

 For Porting to python/JS  : Run  : 
 
 ```make
make Clean
```

 ```make 
 make SharedLib
```
  to create a shared object file  and use the .so file as a starter.

### Caveats 

<li>
  Do note  that this was tested on a Linux System .. What may have compiled for me wouldn't for you if you would. So It is recommended to run the <code>Make Clean && Make SharedLib</code> commands above 
</li>


<li>
    This will not work on Windows . You need a MINGW / Equivalent compiler and compile the same to .dll file as opposed to .so file .
</li>


<li>
 If you Have Troubles Working with generating shared library Run 
  <code>objdump -D lib.so </code> 
  
  to identify if the Function Implementation is there and the namespace isn't mangled..
</li>

### Future Scope 

This current version is just a proof of concept version of ZKP . However there exist many complex algorithms in the wild out there
The aim of the Entire Repository is to try and implement these algorithms with minimal dependencies.
I mean from scratch .. 

### So What's Beyond this ?

If possible this shall be maintained regularly and I shall add a bit of complexity as such 
<li>
Further improvements to use openssl for prime generation
</li>
<li>
Use longer datatypes rather than lli -> Port to gmp for arbitrary numerical precision
</li>
<li>
Need to add Public Key Infrastructure( PKI ) for Key Distribution  Otherwise this is useless
</li>
<li>
Do Note that the sender public Key has to be registered.(Need to add Database Support YET!)
</li>
After this is done this should be a relatively well built Library which is cross-platform (partly)

### Use Case 

See <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/ZKP/Implementation"> Implementation </a>
for implementation in Python using this Library with the help of ctypes
