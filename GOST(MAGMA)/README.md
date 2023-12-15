## гост (МАГМА) шифр ( GOST MAGMA Cipher )
[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/AjayBadrinath/Cryptography/edit/main/GOST(MAGMA)/README.md)
[![ru](https://img.shields.io/badge/lang-ru-blue.svg)]()

## History 

This is a Fiestel Cipher Based Encryption on 64 bit blocks as per Specs Defined by Russian  Union  Standards.
    There is not much Information about this Cipher Developed by the Soviet Union During the time when NSA Developed DES- 56. This Remained as an Alternative 


## Structure 

This is a Symmetric Key Block Cipher With Profile:


      Network     : Fiestel 
      Block Size  : 64 bits  
      Key Size    : 256 bits
      SubKey Size : 32 bits
      No Of Rounds: 32 Rounds
      S-Box       : 8x16 
      Split-Size  : 32 bits

![GOSTDiagram](https://github.com/AjayBadrinath/Cryptography/assets/92035508/9f4b7814-ebf1-4e9c-b174-ba1fdb21694c)

## Details 

### 1.Inputs 

    Key : 256 bit 
    Message(hex) : n bit (will be divided into 64 bit blocks)


### My Comments
The key should be generated using a PseudoRandom Bit Generator ( 256 bits ). Please Refer <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/PRNG/Mersenne%20Twister">MersenneTwister</a>  <a href="https://github.com/AjayBadrinath/Cryptography/tree/main/PRNG/BBS">BlumBlumShub</a> PRNG in my Repository. 
<br></br>
Try Setting a script to generate 256 PseudoRandomly generated bits . This will be your Key. I'll leave it to your discretion .

### 2.S-BOX 

  The initial implementation By the KGB (Soviet Union's Version of NSA) was classified . The initial S-Boxes were mandated bu the Soviet Union and were kept secret to the public. The S-Boxes were seperately to GOST Chip Manufacturers (Again being KGB had Inherent Intended Backdoor).

<br></br>

But.. The De-classified GOST_R_3412-2015  has the S-Box used in this implementation.

The Central Bank of Russian Federation  used a different S-Box that was intended to have backdoors for the KGB to break into .  Ideally speaking the S-Box is the Heart of any cipher .



### 3.Transformations :

There are other functions that are given in the specification, but i am explicitly mentioning the below 2 transformations. The other Transformations given in the paper are implicit (as in implemented implicitly).


#### 1.T-Transformation.
<hr>
  GOST MAGMA uses Non-Linear Bijective Function (Essentially a Fancy term for Substitution ) being Non Linear . 
  Let 
        
  &#960; be the Substitution Transformation from S-Box Defined Above.

  
  Transformation be defined from V<sub>32</sub> -> V<sub>32</sub> (Meaning 32 bit vector space Mapping)

  || Refer to Concat Operation.

 V<sub>32</sub> -> V<sub>32</sub> : t(a)=t(a<sub>7</sub>.....|| a<sub>0</sub>) =  &#960;<sub>7</sub>||...||  &#960;<sub>0</sub>.
 
 Where a=(a<sub>7</sub>.....|| a<sub>0</sub>) &#x3F5; V<sub>32</sub> , a<sub>i</sub> &#x3F5; V<sub>4</sub> , i=(0...7)


<hr>


#### 2.g-Transformation.


<hr>


V<sub>32</sub> -> V<sub>32</sub> : g[k] (a<sub>1</sub>,a<sub>0</sub>)=t((V<sub>32</sub>(a+k)))<<< 11 , 

Where a<sub>i</sub> &#x3F5; V<sub>32</sub>  and ' + 'refer to addition modulo 2<sup>32</sup>
<hr>



### 3.Key Distribution :


The Cipher uses 256 bit Keys and uses Iterative Sub Key For Every Round from the parent Key

Initial Sub Keys For Round 1-8,9-16,17-24
K1= K255||..||K224
K2= K223||..||K192
.    .    .    .
.    .    .    .
.    .    .    .
K8= K31||.. ||K0

Final 25-32 Round 

Reverse the Order from K8->K1
To Summarise:

               Round (1->8(incl)) : MSB->LSB (32bit split) ===>Ascending Phase
                Round (9->16(incl)) : MSB->LSB (32bit split) ===> Ascending Phase
                Round (17->24(incl)) : MSB->LSB (32bit split) ===> Ascending Phase
                Round (25->32(incl)) : LSB->MSB (32bit split) ===> Descending Phase

### 4. Encryption:

This uses a Fiestel Cipher System where we initially Obtain all the subkeys and Flatten the 2d matrix to 1d for convenience sake

1.Split Message into Left And Right (32-bits)

2. Cycle Fiestel Rounds till 31 round <code> (Left,Right)= (Right,Left^g_function(Right,(key[i])))</code> This is the Implicit G Function (Different from g Function)

3. For the Last Round Perform <code>((Left^g_function(Right,(key[-1])))<<32 )^Right</code> This is Another implicit function G<sup>*</sup> Function defined in the paper.

### 5. Decryption:



The Decryption is literally the reverse for Encryption We cycle from the Last round to Round 2
applying the same Transformation.


For the Round 1 Perform <code>((Left^g_function(Right,(key[0])))<<32 )^Right</code> 



