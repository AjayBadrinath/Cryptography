/*

											Fiat Shamir Protocol Implementation Header.
Author: AjayBadrinath
Date:25-11-2023

VERSION: 1.1: Base Implementation - Modular && Added Comments for better readablity .


*/


/*

Portablity : This  library can be ported to Python and can be used as is in  Vanilla C

 For Testing Modify the test.c File and Run: make test
 For Porting to python : Run  : make SharedLib and use the .so file as a starter..

*/


#ifndef __FiatShamir__
#define __FiatShamir__


/*===========================  NECESSARY  IMPORTS  ===========================  */


#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <stdbool.h>
#include <limits.h>
// Helper Definitions 
#define MIN 0
#define PUBLIC_N(prime1,prime2)(prime1*prime2);
#define POW(res,b,e,mod) (mpz_powm(res,b,e,mod));
#define GEN_PRIME_RANGE(a,b) (rand()%(b-a+1))+a;
#define _TUNABLE 100
#define lli long long int 

/*===========================  UTILITY FUNCTIONS ===========================  */

typedef struct FSP_snd{
	
	lli n,rand,x,y;
	lli secret;
	lli PUBLIC_KEY;
}fsp_snd;

lli PrimeCheck(lli);
lli generatePrime(lli, lli);


/*===========================  SENDER FUNCTIONS    ============================*/



lli Setup_sender(fsp_snd*,lli,lli);

lli  commit_phase(fsp_snd*);

lli response_phase(fsp_snd*,lli);


/*==========================   RECIEVER FUNCTIONS   ==========================*/


typedef struct FSP_recv{
	lli prime2, prime1;
	lli n,challenge;
	lli PUBLIC_KEY;
}fsp_recv;

lli Setup_reciever(fsp_recv*);

lli challenge_phase(fsp_recv*,lli);

bool verification_phase(fsp_recv*,lli,lli);


#endif