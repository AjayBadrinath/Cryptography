#include <stdio.h>
#include <stdlib.h>
#include <string.h>
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
char* Encrypt(char* char_set,char*message,int key){
	char*cipher=(char*) malloc(sizeof(char)*1024);
	int pos, idx=0;
	for(int i=0;i<strlen(message);i++){
		pos=find_char(char_set,*(message+i));
		*(cipher+idx++)=*(char_set+(pos+key)%91);
	}
	return cipher;
}
char* Decrypt(char*char_set,char*cipher,int key){
	char*decrypt_text=(char*) malloc(sizeof(char)*1024);
	int pos, idx=0;
	for(int i=0;i<strlen(cipher);i++){
		pos=find_char(char_set,*(cipher+i));
		*(decrypt_text+idx++)=*(char_set+(pos-key)%91);
	}
	//printf("\n%s\n",decrypt_text);
	return decrypt_text;

}
void main(){
char* char_set=GENERATE_CHAR_SET();
int key;
char*message=(char*)malloc(sizeof(char)*1024);
printf("Enter the message to Encrypt : ");
scanf("%[^\n]%*c",message);
printf("Enter the key :");
scanf("%d",&key);

char* cipher=Encrypt(char_set,message,key);
printf("Encrypted Text: %s\n",cipher);
printf("Decrypted Text: %s\n",Decrypt(char_set,cipher,key));


}
