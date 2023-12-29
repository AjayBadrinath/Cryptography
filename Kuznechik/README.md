# 	Кузнечик шифр  (GOST Kuznechik - 128) 
[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/AjayBadrinath/Cryptography/blob/main/GOST(MAGMA)/README.md)
[![ru](https://img.shields.io/badge/lang-ru-blue.svg)](https://github.com/AjayBadrinath/Cryptography/blob/main/GOST(MAGMA)/README.ru.md)

## History 

This is a SP Network (Substitution Permutation)  Cipher Based Encryption on 128 bit blocks as per Specs Defined by Russian  Union  Standards (GOST).
This is aimed as an Alternative to the NSA Approved AES (Rijndael) Cipher. Kuznechik was apparently named after the three proposers for the algorithm (Kuzmin Nechaev and Company)(Ru:Кузьмин, Нечаев и Компания). However the GOST Standard for the same does not declare the <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/GOST(MAGMA)">гост (МАГМА) шифр ( GOST MAGMA Cipher )</a> as defunct.
Recently <a href="https://www.veracrypt.fr/code/VeraCrypt/"> VeraCrypt </a> also Adopted Kuznechik Cipher for Disk Encryption.

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

```
<hr>

### 4. Linear Transformation:
The Linear transformation is defined from the Vector Space  Mapping:
<code>V<sup>16</sup><sub>8</sub>->V<sub>8</sub></code>:
<code>_iter=[148,32,133,16,194,192,1,251,1,192,194,16,133,32,148,1]</code>
    Define l(x):  &#x2207;(_iter[0]*&#x0394;(x<sub>15</sub>)||...||_iter[-1]*&#x0394;(x<sub>0</sub>)) Operations of Addition And Multiplication closed under Field.
    Implementation:
    
    
```python
    
    _map_=[148,32,133,16,194,192,1,251,1,192,194,16,133,32,148,1][::-1]
    res=0

    for i in range(15,-1,-1):
         res^=self.Mod_Polynomial_Reduction(self.M(_map_[i],(x>>8*i)&0xff),0b111000011)

 ```

## Transformations {Decryption}



### 1.S-Inverse Transformation

<hr>

Herewith We Refer the S<sup>-1</sup>-Transformation as The Substitution from the p-box
This Transformation is defined formally int the 128 bit Vector Space Mapping of 


<code>V<sub>128</sub>->V<sub>128</sub> : S<sup>-1</sup>(x):S<sup>-1</sup>(x<sub>15</sub>||...||x<sub>0</sub>)= &#960;<sup>-1</sup>(x<sub>15</sub>)||...|| &#960;<sup>-1</sup>(x<sub>0</sub>)</code>


|| Refers to Concat Operation.

Implementation:

Maintain a variable s such that s is left shifted by 8 bits every iteration (16) such that (16*8=128) bits of substituted bits from P<sup>-1</sup> box obtained 
0xff acts as an 8-bit mask.


```python
s=0
for i in range(15,-1,-1):
      s<<=8
      s^=(self.pi_inv[(x>>8*i&0xff)])
            

```


### 2.R Inverse Transformation 

The R<sup>-1</sup> transformation defined in the standards Map 128 bit Vector Space :

<code>V<sub>128</sub>->V<sub>128</sub> : R<sup>-1</sup> (x):R<sup>-1</sup> (x<sub>15</sub>||...||x<sub>0</sub>)=x<sub>14</sub>||...||x<sub>0</sub>||L(x<sub>15</sub>||...||x<sub>0</sub>)</code>


Where <code> L(k) k=x<sub>15</sub>||...||x<sub>0</sub></code> refer to Linear Transformation Function .

Implementation : 
Apply Linear Transformation to the Entire Input xor with All other x Except x<sub>15</sub>  (8 bits MSB)
```python
((x<<8)^self.linear_Transformation(x<<8^((x>>120)&0xff)))
```
### 3.L Inverse Transformation

The L<sup>-1</sup> Transformation defined in the standards   Map 128 bit Vector Space :
<code>V<sub>128</sub>->V<sub>128</sub> : (R<sup>-1</sup>)<sup>16</sup>(x)</code> 
Meaning Repeatedly apply R(x) to x 16 times
Implementation:
(Apply R<sup>-1</sup>(x) to X )* 16
```python
for _ in range(16):
      x=self.R_Transformation_inverse(x)

```
## Miscellaneous Functions {Decryption} {Encryption}
These are functions that are used to perform Multiplication and Mod under the Galois Field F discussed above  for the polynomial p(x)
To Get a gist of what is being done here Kindly refer the links cited in the source .
### 1.M Function 
This function essentially multiplies two binary polynomials and returns its product
Algorithm:
      Performing Multiplication in the Field{0,1} is essentially left shifting bits. 
      1.So Keep the multiplicand constant
      2.We need to just work with the multiplier and here it is <code>y</code>. 
      3.Access each LSB of y using <code>y&1</code> and check if the LSB is 1 . That means that there is a power in the binary polynomial {0,1}y=>1.y<sup>deg</sup>
      4.Keep another variable as result xor with <code>x<<deg</code> , deg  here is the degree of the polynomial (bit index ) of y where the coeff=1
      5. increment deg for till we exhaust the multiplier y (<code>y>>1</code>)
      

```python
c=0
deg=0
while y!=0:
      if(y&1):
            c^=(x<<deg)
      deg+=1
      y>>=1
```

### 2.Deg Poly 
This function Does the same thing as the deg defined above but is specific to the use case which dictates where we need to find the highest power of the given polynomial ofc len (str) does the same .. but bit shifting seems more sensible. I mean the  this function can be dynamically be re-used when the polynomial is constantly being reduce during an iteration (dependent variable),(<code>k>>=1 or k<<=1</code>).So the powers do change.

```python
deg=0
while x!=0:
      deg+=1
      x>>=1
```

### 3. Mod Polynomial Reduction 
This Function is for performing Modulus under the GF .This is to reduce the Product From the M Function Into the domain of GF (p<x>). Here this function is not specific to a particular domain as such . But Kuznechik uses <code> GF(2)[x]->p(x)=x<sup>8</sup>+x<sup>7</sup>+x<sup>6</sup>+x+1 (111000011) as mod</code> as its modulus domain.
So Any product Produced from the M function is essential to be reduced to the GF(2)[x]->p(x) domain.

Algorithm:
            1.Iterate till deg(z) <deg(mod):while performing (m<<diff) where diff is the 
            difference between the highest power of z and m itself

```python
z=x
while True:
      if(self.degree_Poly(z)<self.degree_Poly(m)):
            break
      else:
            diff=self.degree_Poly(z)-self.degree_Poly(m)
            z^=(m<<diff)
```
## Key Deployment Functions {Decryption} {Encryption} KDF
Interestingly as I had discussed above this algorithm is unique in the sense that it uses Fiestel Structure for its Key Deployment Function.
Kuznechik uses 256 bit seed Key and 128 bit partition Keys for Each Round (10)

### 1.F Transformation
This Is the part that performs the Fiestel Swap where we essentially apply (L(S(roundconst^k1))^k2,k1) where + under the GF {0,1} implicitly implies xor operation.
Returns two Parts where the k1 is untouched and returned and the other key is (k1) xor (c) where c is the round constant.Then applyed finally LS Transformation Layers in the same order.

```python
[self.L_Transformation(self.S_Transformation(c1^k1))^k2,k1]
```

### 2. Round Function 
This Function Essentially is just a utility function to compute Round const (128 bit) For i={0...9} and returns L_Transformation(round_no)
```python
(self.L_Transformation(round_no))
```
### 3. Key Schedule Function
This Function is used to compute key partitions (subkeys) for each round using the round constant and F Transformation discussed above.
The initial Key Partition are
k1=K<sub>256</sub>||...||K<sub>128</sub>
k2=K<sub>127</sub>||...||K<sub>0</sub>
In short this function performs As described in the GOST Standard:
<code>
(K<sub>2i+1</sub>,K<sub>2i+2</sub>)=FTransformation(RoundConst<sub>8(i-1)+8</sub>)...FTransformation(RoundConst<sub>8(i-1)+1</sub>(K<sub>2i-1</sub>,K<sub>2i</sub>)
i={1,2,3,4}
</code>
```python
k1=(key>>128)&self.MASK_32
k2=(key)&self.MASK_32
key_arr=[k1,k2]
for i in range(1,33):
      c=self.Round_constant(i)
      f=self.F_Transformation(c,k1,k2)
      k1,k2=f
      if(i%8==0):
          key_arr.extend([k1,k2])
```
## Encryption
Encryption is done using multiple round sub keys with  Composition of functions L Transformation(S Transformation(x+k)) repeated till round 9 and the final round is sans the Composition we simply perform x+k<sub>10</sub> representing the last sub key. Do Note that For Encryption we use the Functions tagged as {Encryption}/{Misc.}.
```python
a=self.message
k=self.Key_Schedule(self.key)
for i in range(9):
      
      a=self.L_Transformation(self.S_Transformation(k[i]^a))

return a^k[-1]
```
### Decryption
Decryption is similar to Encryption and is  done using multiple round sub keys in reverse with  Composition of functions S Inverse Transformation( L Inverse Transformation((x+k))) repeated till round 1 (from reverse) and the final round is sans the Composition we simply perform x+k<sub>0</sub> representing the first sub key. Do Note that For Decryption we use the Functions tagged as {Decryption}/{Misc.}.
```python
pt=cipher
k=self.Key_Schedule(self.key)

for i in (range(9,0,-1)):
      pt=self.S_Inv_Transformation(self.L_Transformation_inverse(k[i]^pt))
return pt^k[0]
```
