## Blum Blum Shub Pseudo Random Number Generator

## What is this?
![image](https://github.com/AjayBadrinath/Cryptography/assets/92035508/6966c73d-3bb2-41a3-b979-2951240d1a6a)


This is a Pseudo Random Number Generator that can Provide Arbitratry N bit Random binary Sequence based on simple Generator Function of 
      lim <sub>n-><span>&#8734;</span>   (</sub>X<sub>n</sub>=X<sub>n-1</sub><sup>2</sup> % M ) 
<ol>
Parameters For the PRNG

  
<li>
  Where X<sub>n</sub> -> Future Sequence
</li>
<li>
  X<sub>n-1</sub><sup>2</sup> -> Past  Sequence Squared to introduce Randomness .
  
</li>
<li>
  M= p*q Where  P,Q are Large Primes  Which Are Generated By  Atkin's Sieve 
  
</li>
<li>
  S= Secret Number that is CoPrime to M
</li>
<li>
  Seed Generator -  This Part is not a Necessary Parameter But It is needed see Implementation Section For Details. The seed Could be any Number .
</li>
</ol>

## Implementation

This Implementation was done in C with std C libraries as requirements.

Aspects Of Implementation:

<ul>
 <li> <b><i>Prime Number Generator </i></b></li>
      &nbsp;  I Chose Not to use the Standard <a href ="https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes">Sieve of Erasthones</a>  as it is very slow to Generate Considerably Large Primes 
        and has a Time complexity of O(log(log(n)))
  
  <br>So I came across another Sieve generator  <a href="https://en.wikipedia.org/wiki/Sieve_of_Atkin">Atkin's Sieve</a> that is Comparatively better than the previous one 
    And has  a time complexity of O(N/log(log(N))) with a space tradeoff of O(N)
    <br>
    <li>Sieve Of Atkin Implementation </li>
    <br>
    
  ```C
    bool* _state=(bool*)calloc(limit+1,sizeof(bool));
  ```
  <br>
  This <code>_state</code> is for creating a Bool Array with N x 1 Dimension to toggle depending on the conditions .
  
  <br>

  
  ```C
    uint32_t n=0,isq=0,jsq=0;
    uint64_t lim=(uint64_t)sqrt(limit)+1;
  ```
<br>
  Here We Create a UpperLimit Of the Search space  sqrt(lim)
<br>

  ```C
    for(uint64_t i=1;i<lim;i++){
        for(uint64_t j=1;j<lim;j++){
            isq=i*i;
            jsq=j*j;
            n=4*isq+jsq;
            if(n<=limit && (n%12==1|n%12==5)){ _state[n]^=true;}
            n=3*isq+jsq;
            if((n<=limit)&&(n%12==7)){_state[n]^=true;}
            n=3*isq-jsq;
            if((i>j)&&n<=limit&&(n%12==11)){_state[n]^=true;}

        }
    }
  ```
<br>


Here This is an optimised version for the original algorithm proposed by Atkin .(For A detailed Explanation Refer Wiki)


We Toggle the state if:

      1.n=4x**2+y**2 -> if (n%12) belongs to {1,5} 
      2.n=3x**2+y**2  -> if (n%12) belongs to {7}
      3.n=3x**2-y**2  -> if (n%12) belongs to {11}

      
<br>


We then clear out the squares of Primes by setting those squares we toggled to false as shown below
<br>


```C
for (uint64_t k=5;k<lim;k++){
        if (_state[k]){
            uint64_t pow_k=k*k;
            for(int i=pow_k;i<=limit;i+=pow_k){
                _state[i]=false;
            }
        }
    }
```

<br>

<li> <b><i>BBS Generator  </i></b></li>
<br>

This part of the code is to find the Primes P,Q Such that it is congruent to 3 mod 4
So we Use the above Atkin Generator Discussed To generate large primes.
2000000 is the Upper Limit - > this can be tweaked in the code .
<br>
```C
uint64_t prime1,prime2,tmp2,iter=0;
    while(true){
        tmp2=genPrime(2000000,seed%100000+_MAGIC2+iter);
        if(tmp2%4==3){
        prime1=tmp2;
        iter=0;
        break;
    }iter++;}
    while(true){
        tmp2=genPrime(2000000,seed%100000+_MAGIC+iter);
        if(tmp2%4==3){
        prime2=tmp2;
        iter=0;
        break;
    }iter++;}
  ```
<br>
Once the Primes are Found then we compute N = p*q and Generate S such that s is coprime to N ie (gcd(i,n)==1)
<br>

```C
uint64_t n= prime1*prime2;
    uint64_t secret,tmp;
    for (uint64_t i=(uint64_t)sqrt(n/2);i<n;i++){
        //printf("%ld\n",i);
        
        if(PrimeCheck(i)){
            if(COPRIME(i,n)){
            secret=i;
            break;
            }
        }
    }
```
<br>

This is where the Core of the BBS Lies  
We generate bits upto 64  and do bit shifts to generate a uint_64 integer that is 64 bits. Do Note that this is a deterministic PRNG Meaning the Seed is the core for this Algorithm
<br>
```C
uint64_t _seed_gen= (secret*secret)%n;

   // printf("\n%ld \n%ld",n,secret);
   uint64_t no=0;
    for (int i=0;i<63;i++){
        int bit=_seed_gen%2;
       // printf("%d",bit);
        no=(no<<1)^(bit);
        _seed_gen=(_seed_gen*_seed_gen)%n;
        
    }
    return no;
```
<br>
<li> <b><i> Design Choice : Seed  Generator  </i></b></li>
<br>
One can use time(NULL) as Seed Which is a good choice .
<br>


### Why Not Time?

<p>
  But one caveat is that Since We are choosing Large Primes   the time will also be a long int which caps the upper limit & Lower Limit.
  By Prime Number Theory  There Exists Large Undeterminable Prime Gaps Where We May Never Find Any Prime Whatsoever.
  So This will Lead to an Endless loop & Memory Wastage or even so we may only find one prime where the bits generated will be the same  no matter how long the time changes.
  
</p>

<br>
<p>
  So I Chose to stick with True Source of Randomness to generate a seed  .
  The Linux <code>/dev/urandom/</code> provides cryptographically secure Environment Noise data eg device driver 
  This ensures that the Seed Generated will Drastically change and Hence the Upper & Lower Limit Search Space will also change and this will result in a new Prime
</p>
<br>

```C
uint64_t gen_seed(){
    FILE* f=fopen("/dev/urandom","r");
    uint64_t seed;
    fread(&seed,sizeof(seed),1,f);
    fclose(f);
    return (uint64_t)abs(seed);
}
```

</ul>
