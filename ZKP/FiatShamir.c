/*

											Fiat Shamir Protocol Implementation 
Author: AjayBadrinath
Date:25-11-2023

VERSION: 1.0: Base Implementation - Modular.

Comments On the Implementation :
		
		This is intented  as a library to serve for Authentication for Demo Purposes . So I have not overloaded Functions
		and Ensured Maximal Separation among Functions.Dependencies Rely on the values obtained over network by pass by value to functions.


		This is intended to be on Authentication of Client -Server over TCP

		So There is a Provision of 2 Structures to Aid the same depending on the use case

		However there are native functions to the server(reciever) and client (sender).

		All of them Are Properly Commented and Separated in order to ensure proper implementation.






Further Improvements:


						This is a Modular Implementation Of the Fiat Shamir Protocol and Just a simple Prototype
						

						1.Further improvements to use openssl for prime generation
						2.use longer datatypes rather than int 


*/


#include "FiatShamir.h"


// Helper Definitions 

#define PUBLIC_N(prime1,prime2)(prime1*prime2);
#define POW(res,b,e,mod) (mpz_powm(res,b,e,mod));
#define GEN_PRIME_RANGE(a,b) (rand()%(b-a+1))+a;



/*===========================  UTILITY FUNCTIONS ===========================  */


int PrimeCheck(int num){
	if(num<2){return 0;}
	for (int i=2;i<num/2+1;i++){
		if(num%i==0){return 0;}
	}
	return 1;
}


int generatePrime(int start_Range, int EndRange){
	int prime;
	time_t t1;
	srand((unsigned)time(&t1));
	//prime=(rand()%(EndRange-start_Range+1))+start_Range;
	prime=GEN_PRIME_RANGE(start_Range,EndRange);
	while(!PrimeCheck(prime)){
		prime=GEN_PRIME_RANGE(start_Range,EndRange);
	}
	return prime;
}




/*===========================  SENDER FUNCTIONS    ============================*/


int Setup_sender(fsp_snd* sender,int n,int secretKey){

	sender->secret=secretKey;
	sender->n=n;
	sender->PUBLIC_KEY=(int)pow(secretKey,2)%n;
	return sender->PUBLIC_KEY;

}

int  commit_phase(fsp_snd*send){
	send->rand=rand()%120;
	send->x=(int)pow(send->rand,2)%send->n;
	return send->x;
}

int respose_phase(fsp_snd*send,int challenge){
	send->y=send->rand*(int)pow(send->secret,challenge)%send->n;
	return send->y;
}




/*==========================   RECIEVER FUNCTIONS   ==========================*/


int Setup_reciever(fsp_recv*recv){
	recv->prime1=generatePrime(100,10000);
	recv->prime2=generatePrime(100,10000);
	recv->n=PUBLIC_N(recv->prime2,recv->prime1);
	return recv->n;

}

int challenge_phase(fsp_recv*recv,int PUBLIC_KEY){
	recv->challenge=(int)rand()%100;
	recv->PUBLIC_KEY=PUBLIC_KEY;
	return recv->challenge;
}


bool verification_phase(fsp_recv*recv,int x,int y){
	 (((int)pow(y,2)%recv->n)==(x*(int)pow(recv->PUBLIC_KEY,recv->challenge)%recv->n))?true:false;

}



