# 	Кузнечик шифр  (GOST Kuznechik - 128) 

## History 

This is a SP Network (Substitution Permutation)  Cipher Based Encryption on 128 bit blocks as per Specs Defined by Russian  Union  Standards (GOST).
This is aimed as an Alternative to the NSA Approved AES (Rijndael) Cipher. Kuznechik was apparently named after the three proposers for the algorithm (Kuzmin Nechaev and Company)(Ru:Кузьмин, Нечаев и Компания). However the GOST Standard for the same does not declare the <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/GOST(MAGMA)">гост (МАГМА) шифр ( GOST MAGMA Cipher )</a> as defunct.
Recently VeraCrypt Also Adopted Kuznechik Cipher for Disk Encryption.

## Structure 

This is a Symmetric Key Block Cipher With Profile:


      Network     : SPN + Fiestel (For Key Deployment)
      Block Size  : 128 bits  
      Key Size    : 256 bits
      SubKey Size : 128 bits
      No SubKeys  : 10 Sub Keys
      No Of Rounds: 10 Rounds
      P-Box       : 16x16 -> 256
      P-Inv_Box   : 16x16 -> 256
    
## Details 

### 1.Inputs 

    Key : 256 bit 
    Message(hex) : n bit (will be divided into 128 bit blocks)


### My Comments
The key should be generated using a PseudoRandom Bit Generator ( 256 bits ). Please Refer <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/PRNG/Mersenne%20Twister">MersenneTwister</a>  <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/PRNG/BBS">BlumBlumShub</a> PRNG in my Repository. 
<br></br>
Try Setting a script to generate 256 PseudoRandomly generated bits . This will be your Key. I'll leave it to your discretion .
Also Kuznechik Employs Galois Field Arithmetic <code> (Operations On Field  GF(2)[x]->p(x) ) with p(x)=x<sup>8</sup>+x<sup>7</sup>+x<sup>6</sup>+x+1 </code>So to understand the Source code will require some Knowledge on Galois Field Arithmetic and Number Theory.

### 2.P-BOX
The Non Linear Bijective Transformation P-Box is defined in the standards as P = V<sub>8</sub> &#960;<sup>-1</sup>Int<sub>8</sub>
Where V<sub>8</sub> is a Bijective Mapping associated with the ring of the Z<sub>2<sup>8</sup></sub>=>V<sub>8</sub>- Z<sub>2<sup>8</sup></sub> and 

Int<sub>8</sub> refers to inverse mapping to the vector V<sup>-1</sup> that is V<sub>8</sub>=> Z<sub>2<sup>8</sup></sub>- V<sub>8</sub>


## Transformations {Encryption}

The Kuznechik cipher uses a variety of transformations in order to perform Substitution and Permutation wrt each block

### 1.S Transformation

<hr>

Herewith We Refer the S-Transformation as The Substitution from the p-box
This Transformation is defined formally int the 128 bit Vector Space Mapping of 


<code>V<sub>128</sub>->V<sub>128</sub> : S(x):S(x<sub>15</sub>||...||x<sub>0</sub>)= &#960;(x<sub>15</sub>)||...|| &#960;(x<sub>0</sub>)</code>


|| Refers to Concat Operation.

Implementation:

Maintain a variable s such that s is left shifted by 8 bits every iteration (16) such that (16*8=128) bits of substituted bits from P box obtained 
0xff acts as an 8-bit mask.


```python
s=0
for i in range(15,-1,-1):
  s<<=8
  s^=(self.pi[(x>>8*i&0xff)])

return s
```


<hr>

### 2.R Transformation 

The R transformation defined in the standards Map 128 bit Vector Space :

<code>V<sub>128</sub>->V<sub>128</sub> : R(x):R(x<sub>15</sub>||...||x<sub>0</sub>)=L(x<sub>15</sub>||...||x<sub>0</sub>)||x<sub>15</sub>||...||x<sub>1</sub></code>


Where <code> L(k) k=x<sub>15</sub>||...||x<sub>0</sub></code> refer to Linear Transformation Function .

Implementation : 
Apply Linear Transformation to the Entire Input xor with All other x Except x<sub>0</sub>  (8 bits LSB)
```python
((linear_Transformation(x)<<120)^(x>>8))
```

### 3.L Transformation

The L Transformation defined in the standards   Map 128 bit Vector Space :
<code>V<sub>128</sub>->V<sub>128</sub> : R<sup>16</sup>(x)</code> 
Meaning Repeatedly apply R(x) to x 16 times
Implementation:
(Apply R(x) to X )* 16
```python
for _ in range(16):
  x=self.R_Transformation(x)
return x
```
<hr>



