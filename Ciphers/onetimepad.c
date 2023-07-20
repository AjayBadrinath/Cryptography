#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <assert.h>
//YOUR CONVENTIONAL MOD FAILS IN NEGATIVE NUMBERS 
#define MOD(a,b) (((a%b)+b)%b) 
//Other functions are self-explanatory ...
char* GENERATE_CHAR_SET(){
	char*alphabets=(char*)malloc(91*sizeof(char));
	for(int i=32;i<123;i++){sprintf((alphabets+(i-32)),"%c",i);}
	return alphabets;
}

int find_char(char*char_set,char val){
	for(int i=0;i<strlen(char_set);i++){
		if(*(char_set+i)==val){
		return i;
		}
	}
	return -1;
}
char*Encrypt(char* char_set,char*message,char*key){
	assert(strlen(message)==strlen(key));
	char* cipher=(char*)malloc(sizeof(char)*1024);
	for(int i=0;i<strlen(message);i++){
		*(cipher+i)=*(char_set+MOD((find_char(char_set,*(key+i))+find_char(char_set,*(message+i))),91));
	}
	return cipher;

}
char*Decrypt(char* char_set,char*cipher_text ,char* key){
	assert(strlen(cipher_text)==strlen(key));
	char* decrypt_txt=(char*)malloc(sizeof(char)*1024);
	for(int i=0;i<strlen(cipher_text);i++){
		*(decrypt_txt+i)=*(char_set+MOD((find_char(char_set,*(cipher_text+i))-find_char(char_set,*(key+i))),91));
		//printf("%d %d\n",i,(find_char(char_set,*(key+i))+find_char(char_set,*(char_set+i)))%26);
		printf("%d %d\n",find_char(char_set,*(key+i)),find_char(char_set,*(cipher_text+i)));
	}
	return decrypt_txt;

}
void main(){
	char* char_set=GENERATE_CHAR_SET();
	char*message=(char*)malloc(sizeof(char)*1024);
	char* key=(char*)malloc(sizeof(char)*1024);
	printf("Enter the text to Encrypt");
	scanf("%s",message);
	printf("Enter the key");
	scanf("%s",key);

	char*cipher=Encrypt(char_set,message,key);
	printf("\n%s\n", cipher);
	printf("%s\n",char_set);
	printf("%s\n",Decrypt(char_set,cipher,key));

}
