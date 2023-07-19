/*

@author : Ajay Badrinath

@date	: 17-07-23

*/

/*

C STANDARD LIBRARIES

*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
#include <string.h>

/*

GNU MULTIPRECISION LIBRARY

*/

//#include <openssl/bn.h>

#include <gmp.h>
/*

Structure to hold the RSA PARAMETERS

*/
typedef struct RSA{
	int prime1,prime2;
	int public_key;
	int private_key;
	int phi;
	unsigned long long  public_exponent;
	unsigned long long  private_exponent;
	int k;

}rsa;

/*
	Helper Functions/Macros to implement RSA ALgorithm

*/
#define POW(res,b,e,mod) (mpz_powm(res,b,e,mod));
#define GEN_PRIME_RANGE(a,b) (rand()%(b-a+1))+a;

/*
Previous Attempts to perform modinv / exponentiation mod operation by using C STD library :
But Integer overflow Occours while Decryption so I switched to gmp library which takes care of integer precision
*/
//double POW(int b,int e,double mod){return fmod(pow(b,e),mod);}
//#define POW(b,e,mod) (fmod(pow(b,e),mod));
//double POW(int b,int e,double mod){return powm(b,e,mod);}
/*
Ideally a PseudoPRNG generator must be used to generate pure  random numbers from external environment/noise.
But i chose to stick with STDLIB for convienience sake!

*/
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

int gcd(int a,int b){
	int tmp;
	while(b!=0){
		tmp=a%b;
		a=b;
		b=tmp;
	}
	return a;
}
/*


RSA Implementation

prime1,prime2: Large Primes
	Short Note On Prime Generation: openssl Offers a wide variety of functions from rsa.h library from PNG 
					Generate A KEYPAIR  using 
							openssl -genpkey -algorithm RSA -out privkey.pem 
							openssl rsa -pubout -in privatekey.pem -out publickey.pem   
							


Public Key(n)=prime1*prime2 Huge Composite Number 

Phi(Euler Totient Function)=(prime1-1)*(prime2-1)

k Some Arbitrary Constant

Key Generation:
	d : PrivateExponent
	e : PublicExponent
	PubKey(Public Key):{e,n}
	PrivKey(Private Key):{d,n}-> Secret!
	d*e ~ 1 mod(Phi(n));
	d ~ modinv(e)*phi(n)
	
*/

void RSA_imp(int prime_start,int prime_end,rsa* p){
	p->prime1=generatePrime(prime_start,prime_end);
	p->prime2=generatePrime(prime_start,prime_end);

	while(p->prime1==p->prime2){p->prime2=generatePrime(prime_start,prime_end);}

	p->public_key=p->prime1*p->prime2;
	p->phi=(p->prime1-1)*(p->prime2-1);
	p->k=2;
	int e=2;

	while(e<p->phi&&gcd(e,p->phi)!=1){e++;}

	p->public_exponent=e;
	int i;

	//p->private_exponent=(1+(p->k*p->phi))/p->public_exponent;
	for(  i=3;i<p->phi;i++)if(i*p->public_exponent%p->phi==1){break;}
	p->private_exponent=i;
}

rsa* init_RSA(){

	rsa* Rsa=(rsa*)malloc(sizeof(rsa));

	RSA_imp(100,10000,Rsa);

	printf("%d***\n",Rsa->prime1);

	return Rsa;
}
/*
Routines to encrypt and Decrypt Messages
parameters (Encrypt):
		1.result->Encrypted Data to be flushed to res of type mpz_t
		2.Original Message
		3.rsa struct ptr 
		Note (For Future me):
			If in case any use case of gmp library arise the general template is as follows.
				char[k*j]; Allocate char array
				sprintf(); Copy int / any dtype to char array
				init mpz_t variables
				*********
				blah blah blah
				alloc char array
				blah blah
				mpz_t va1,va2......va(k-1)
				mpz_init(va1).......
				mpz_set_ui(va1,k)......
				mpz_set_str(va1,str,base).....
				process using mpz_some_function in the documentation
				mpz_clear(var1)..... to prevent memleaks..(Important not to clear multiple times though! Else segfaults)

*/
void encrypt(mpz_t res,int msg,rsa* r){
	char str[1024],str1[1024],str2[1024];
	sprintf(str,"%d",msg);
	sprintf(str1,"%lld",r->public_exponent);
	sprintf(str2,"%d",r->public_key);
	mpz_t n,n1,n2;
	int flag;
	mpz_init(n);mpz_init(n1);mpz_init(n2);
	mpz_set_ui(n,0);mpz_set_ui(n1,0);mpz_set_ui(n2,0);
	flag=mpz_set_str(n,str,10);
	flag=mpz_set_str(n1,str1,10);
	flag=mpz_set_str(n2,str2,10);
	POW(res,n,n1,n2);
	mpz_clear(n);mpz_clear(n1);mpz_clear(n2);

}
void decrypt(mpz_t crypt,mpz_t res,rsa*r){
	char str1[1024],str2[1024];
	sprintf(str1,"%lld",r->private_exponent);
	sprintf(str2,"%d",r->public_key);
	mpz_t n1,n2;
	int flag;
	mpz_init(n1);mpz_init(n2);
	mpz_set_ui(n1,0);mpz_set_ui(n2,0);
	flag=mpz_set_str(n1,str1,10);
	flag=mpz_set_str(n2,str2,10);
	POW(res,crypt,n1,n2);
	//mpz_clear(n1);mpz_clear(n2);
	//mpz_clear(crypt);

}


unsigned long int * TEXT_ENCRYPT(char*text,int length,rsa*r){

	int ascii;
	mpz_t res1;
	int k=0;
	mpz_init(res1);mpz_set_ui(res1,0);
	unsigned long int*bytearray_enc=(unsigned long int*) malloc(length*(sizeof(unsigned long int)));
	for(int i=0;i<length;i++){
		ascii=*(text+i);
		encrypt(res1,ascii,r);
		//mpz_out_str(bytearray_enc[k++],10,res1);
		*(bytearray_enc+(k++))=mpz_get_ui(res1);

	}
	mpz_clear(res1);
	for(int i=0;i<length;i++){
		printf("%ld\n",*(bytearray_enc+i));
	}
	return bytearray_enc;


}


void TEXT_DECRYPT(unsigned long int * crypt,int length,rsa*r){
	char*arr=(char*)malloc(sizeof(char)*length);
	mpz_t res2,res3;
	mpz_init(res2);mpz_set_ui(res2,0);
	mpz_init(res3);mpz_set_ui(res3,0);
	char str[1024];
	int i;
	for( i=0;i<length;i++){
		
		sprintf(str,"%ld",*(crypt+i));
		mpz_set_str(res2,str,10);
		memset(str,0,1024);
		decrypt(res2,res3,r);
		printf("-------");
		mpz_out_str(stdout,10,res3);
		*(arr+i)=mpz_get_ui(res3);
	
	//mpz_clear(res3);
	//mpz_clear(res3);
	}
	*(arr+i)='\0';
	printf("%s",arr);
}

void EncryptFile(char* path,rsa* r){
	FILE * RSA_parameters=fopen("RSAKEY.bin","wb");
	FILE * file=fopen(path,"r");
	FILE * file_enc=fopen("test123.txt","w");
	fwrite(r,sizeof(*r),1,RSA_parameters);
	char buffer[30];
	unsigned long int* var;
	//=(unsigned long int*)malloc(sizeof(unsigned long int)*1024);
	int i=0;
	if(file!=NULL){

		while(fgets(buffer,30,file)!=NULL){
			if(strlen(buffer)>0){
				(var)=TEXT_ENCRYPT(buffer,30,r);
				memset(buffer,0,30);
				//fputc(*var,file_enc);
				putw(*var,file_enc);
			}
		}
		//for (int j=0;j<i;j++){putw(*var,file_enc);}
		
	}
	fclose(file);
	fclose(file_enc);
	fclose(RSA_parameters);

}
void DecryptFile(){
	printf("DECRYPT_FILE\n");
	FILE*fptr=fopen("RSAKEY.bin","rb");
	FILE*open=fopen("test123.txt","r");
	FILE*decrypt=fopen("tada1.txt","w");
	rsa* r=(rsa*)malloc(sizeof(rsa));
	//rsa* r;
	fread(r,sizeof(*r),1,fptr);
	fclose(fptr);
	char buffer[30];
	unsigned long int* var=(unsigned long int*)malloc(sizeof(unsigned long int)*1024);
	int i=0;
	
	if(open!=NULL){
		while(fgets(buffer,1,open)!=NULL){
			if(strlen(buffer)>0){
				//sprintf(*var,"%ld",buffer);
				//sscanf(buffer,"%ld",var);
				*(var+i++)=getw(open);
				//
				//
				//memset(buffer,0,30);
				//fputs(*var,file_enc);
				//fputs(*var,
				
			}
			TEXT_DECRYPT(var,i,r);
		}
		//printf("-=-=-\\\\\\\\\\\\====\n");
	
	}
	
	//fscanf(decrypt,"%ld",var[0]);
	//while(!feof(decrypt)){
	//	printf("%ld",var[i]);
//		fscanf(decrypt,"%d",var[i++]);
//	}
	//printf("-==-=-=%lld-=-=-\n",r->public_exponent);
	fclose(open);fclose(decrypt);
}








int main(){

	//printf("%d",generatePrime(1000,10000));
	rsa* r=init_RSA();
	mpz_t res,decryptedmsg;
	mpz_init(res);mpz_set_ui(res,0);
	mpz_init(decryptedmsg);mpz_set_ui(decryptedmsg,0);
	unsigned long int*a=(unsigned long int *)malloc(5*sizeof(unsigned long int));
	a=TEXT_ENCRYPT("hello there wazzup >?",(int)strlen("hello there wazzup >?"),r);
	for(int i=0;i<strlen("hello there wazzup >?");i++){
		printf("%ld\n",*(a+i));
	}
	EncryptFile("rsaold.txt",r);
	TEXT_DECRYPT(a,strlen("hello there wazzup >?"),r);
	//printf("\n%d\n",r->prime1);
	printf("Message to Encrypt : 120\n");
	encrypt(res,1134455,r);
	printf("\nEncrypted message:\t");
	//printf("%s",res);
	mpz_out_str(stdout,10,res);
	decrypt(res,decryptedmsg,r);
	printf("\nDecrypted msg :\t");
	mpz_out_str(stdout,10,decryptedmsg);
	printf("\n");
//	DecryptFile();
//	mpz_clear(res);mpz_clear(decryptedmsg);
	//mpz_out_str(stdout,10,decryptedmsg);
	//int crypt=encrypt(120,r);
	//printf("Encrypted Message:%d\n",crypt);
	//printf("Decrypted Message:%lld\n",decrypt(crypt,r));
	printf("\nprime1%d \nprime2%d \npubkey%d\n privkey%d\n phi%d\n public_exponent%lld\n private_exponenet%lld\n k%d\n",r->prime1,r->prime2,r->public_key,r->private_key,r->phi,r->public_exponent,r->private_exponent,r->k);

}

