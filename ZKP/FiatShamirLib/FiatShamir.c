/*

											Fiat Shamir Protocol Implementation 
Author: AjayBadrinath
Date:25-11-2023

VERSION: 1.1: Base Implementation - Modular.&& Added Comments to code

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
						3. Need to add Public Key Infrastructure(PKI ) for Key Distribution . Otherwise this is useless.
						4.Do Note that the sender public Key has to be registered.(Need to add Database Support )
						

*/


#include "FiatShamir.h"


/*  

Note: 


1.The Macros are Defined  in the header file 
And I have to change the Datatype to Arbitrary prescision to handle Huge Primes(Power especially ...).

2.I have Downsized the Prime Generation Intentionally as LLI (Long Long Int ) still overflows and wraps around as -ve integer which breaks the system.
So there will be some arbitrary places of numbers here and there repeated. 


IMPORTANT:


3.Do NOTE  that This was tested on a Linux System .. What may have compiled for me wouldn't for you if you would. So It is recommended to run the "Make Clean && Make SharedLib" commands

4.Also This will not work on windows . You need a MINGW / Equivalent compiler and compile the same to .dll file as opposed to .so file .

5.If you Have Troubles Working with generating shared library Run objdump -D lib.so to identify if the Function Implementation is there and the namespace isn't mangled..


*/



/*===========================  UTILITY FUNCTIONS ===========================  */
/*
Functions to perform primality check (nah.. just basic stuff). Check out Miller-Rabin Test of Primality...
*/

lli PrimeCheck(lli num){
	if(num<2){return 0;}
	for (lli i=2;i<num/2+1;i++){
		if(num%i==0){return 0;}
	}
	return 1;
}

/*
Function to generate Prime Numbers within Range . Just BruteForce. 
*/

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
/*
Sender Native Functions (Prover Functions ):
	1.Setup Phase:
		 Prover Has a secret Key : s 
		 And Sends Public Key to Reciever (Verifier)
		

	2.Commit Phase:
		Chooses a random value and bound it by common n and Send the same 

	3.Response Phase :
		Respond to the challenge

*/

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

lli response_phase(fsp_snd*send,lli challenge){
	send->y=(lli)(send->rand*(lli)pow(send->secret,challenge)%send->n);
	return send->y;
}




/*==========================   RECIEVER FUNCTIONS   ==========================*/

/*
Reciever Native Functions:
	1.Setup Reciever:
		Choose Arbitrary prime and send n to prover (Registration Phase )
	2.Challenge Prover:
		Challenge Can be any Rand Number
	3.Verify:
		Verify the same recieved from Response Phase(Prover) with y**2%n

*/


lli Setup_reciever(fsp_recv*recv){
	recv->prime1=generatePrime(MIN,_TUNABLE);
	recv->prime2=generatePrime(MIN,_TUNABLE);
	recv->n=PUBLIC_N(recv->prime2,recv->prime1);
	return recv->n;

}

lli challenge_phase(fsp_recv*recv,lli PUBLIC_KEY){
	recv->challenge=(lli)rand()%(50);
	recv->PUBLIC_KEY=PUBLIC_KEY;
	return recv->challenge;
}


bool verification_phase(fsp_recv*recv,lli x,lli y){
	 (((lli)pow(y,2)%recv->n)==(x*(lli)pow(recv->PUBLIC_KEY,recv->challenge)%recv->n))?true:false;

}



