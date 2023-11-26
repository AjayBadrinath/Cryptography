/*

											Fiat Shamir Protocol Implementation Header.
Author: AjayBadrinath
Date:25-11-2023

VERSION: 1.0: Base Implementation - Modular.


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

#define _TUNABLE 10000
#define lli long long int 

/*===========================  UTILITY FUNCTIONS ===========================  */


lli PrimeCheck(lli);
lli generatePrime(lli, lli);


/*===========================  SENDER FUNCTIONS    ============================*/


typedef struct FSP_snd{
	
	lli n,rand,x,y;
	lli secret;
	lli PUBLIC_KEY;
}fsp_snd;

lli Setup_sender(fsp_snd*,lli,lli);

lli  commit_phase(fsp_snd*);

lli respose_phase(fsp_snd*,lli);


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