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
#define MIN 0
#define PUBLIC_N(prime1,prime2)(prime1*prime2);
#define POW(res,b,e,mod) (mpz_powm(res,b,e,mod));
#define GEN_PRIME_RANGE(a,b) (rand()%(b-a+1))+a;



/*===========================  UTILITY FUNCTIONS ===========================  */


lli PrimeCheck(lli num){
	if(num<2){return 0;}
	for (lli i=2;i<num/2+1;i++){
		if(num%i==0){return 0;}
	}
	return 1;
}


lli generatePrime(lli start_Range, lli EndRange){
	lli prime;
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


lli Setup_sender(fsp_snd* sender,lli n,lli secretKey){

	sender->secret=secretKey;
	sender->n=n;
	sender->PUBLIC_KEY=(lli)pow(secretKey,2)%n;
	return sender->PUBLIC_KEY;

}

lli  commit_phase(fsp_snd*send){
	send->rand=rand()%120;
	send->x=(lli)pow(send->rand,2)%send->n;
	return send->x;
}

lli respose_phase(fsp_snd*send,lli challenge){
	send->y=(lli)abs(send->rand*(lli)pow(send->secret,challenge)%send->n);
	return send->y;
}




/*==========================   RECIEVER FUNCTIONS   ==========================*/


lli Setup_reciever(fsp_recv*recv){
	recv->prime1=generatePrime(MIN,INT_MAX%_TUNABLE);
	recv->prime2=generatePrime(MIN,INT_MAX%_TUNABLE);
	recv->n=PUBLIC_N(recv->prime2,recv->prime1);
	return recv->n;

}

lli challenge_phase(fsp_recv*recv,lli PUBLIC_KEY){
	recv->challenge=(lli)rand()%(INT_MAX%_TUNABLE);
	recv->PUBLIC_KEY=PUBLIC_KEY;
	return recv->challenge;
}


bool verification_phase(fsp_recv*recv,lli x,lli y){
	 (((lli)pow(y,2)%recv->n)==(x*(lli)pow(recv->PUBLIC_KEY,recv->challenge)%recv->n))?true:false;

}



