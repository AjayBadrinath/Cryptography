/**
 * @file BlumBlumShub.c
 *                                              Blum Blum Shub Pseudo Random Number Generator
 * 
 * 
 * 
 * @author  AjayBadrinath 
 * @brief 
 * @version 1.0
 * @date 2023-12-06
 * 
 * @copyright Copyright (c) 2023
 * 
 * 
 */



// yet to paralleize

#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <stdint.h>
#include <math.h>
#include <time.h>
#include <omp.h>
#define _MAGIC 1582734
#define _MAGIC2 172626


#define MIN(a,b) (((a)<(b))?(a):(b))
#define COPRIME(a,b)((gcd(a,b)==1)?(true):(false))



uint64_t gcd(uint64_t a,uint64_t b){
    uint64_t x=MIN(a,b);
    for (x;x>=0;x--){
        if(a%x==0 && b%x==0){
            break;
        }
    }
    return x;
}


uint64_t genPrime(uint64_t limit,uint64_t idx){
    
    bool* _state=(bool*)calloc(limit+1,sizeof(bool));
    
    uint32_t n=0,isq=0,jsq=0;
    uint64_t lim=(uint64_t)sqrt(limit)+1;
    //#pragma omp parallel for
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
    //#pragma omp parallel for
    for (uint64_t k=5;k<lim;k++){
        if (_state[k]){
            uint64_t pow_k=k*k;
            for(int i=pow_k;i<=limit;i+=pow_k){
                _state[i]=false;
            }
        }
    }
    for(uint64_t i=idx;i<limit;i++){
        if(_state[i]){
            free(_state);
            return i;
        }
    }
    free(_state);
    return -1;
    
   
}



bool PrimeCheck(uint64_t num){
	if(num<2){return 0;}
	for (uint64_t i=2;i<num/2+1;i++){
		if(num%i==0){return 0;}
	}
	return 1;
}



uint64_t BBS(uint64_t seed){
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

    //printf("%ld %ld",prime2,prime1);
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
    

}



uint64_t gen_seed(){
    FILE* f=fopen("/dev/urandom","r");
    uint64_t seed;
    fread(&seed,sizeof(seed),1,f);
    fclose(f);
    return (uint64_t)abs(seed);
}


void main(){
    uint64_t x=time(0);
    
    printf("%ld",BBS((uint64_t)gen_seed()));
}
