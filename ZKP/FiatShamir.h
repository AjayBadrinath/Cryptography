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


/*===========================  UTILITY FUNCTIONS ===========================  */


int PrimeCheck(int);
int generatePrime(int, int);


/*===========================  SENDER FUNCTIONS    ============================*/


typedef struct FSP_snd{
	
	int n,rand,x,y;
	int secret;
	int PUBLIC_KEY;
}fsp_snd;

int Setup_sender(fsp_snd*,int,int);

int  commit_phase(fsp_snd*);

int respose_phase(fsp_snd*,int);


/*==========================   RECIEVER FUNCTIONS   ==========================*/


typedef struct FSP_recv{
	int prime2, prime1;
	int n,challenge;
	int PUBLIC_KEY;
}fsp_recv;

int Setup_reciever(fsp_recv*);

int challenge_phase(fsp_recv*,int);

bool verification_phase(fsp_recv*,int,int);


#endif