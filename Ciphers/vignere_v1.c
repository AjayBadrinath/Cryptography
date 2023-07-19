#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char* SHL_CHARECTERS(char*alphabets){
	char tmp=*alphabets;
	for(int i=1;i<91;i++)*(alphabets+i-1)=*(alphabets+i);
	*(alphabets+90)=tmp;
	return alphabets;
}


int find_row(char**vignere_matrix,char letter){
int i;
	for( i=0;i<91;i++){if(vignere_matrix[i][0]==letter){break;}}
	return i;
}

char** GENERATE_VIGNERE_MATRIX(){
	char* alphabets=(char*)malloc(91*sizeof(char));
	for(int i=32;i<123;i++){sprintf((alphabets+(i-32)),"%c",i);}
	char** matrix=(char**)malloc(91*sizeof(char*));
	for(int i=0;i<91;i++){*(matrix+i)=(char*)malloc(91*sizeof(char));}
	printf("\n-------------------\n");
	for(int i=0;i<91;i++){
		sprintf(*(matrix+i),"%s",alphabets);
		alphabets=SHL_CHARECTERS(alphabets);
	}
	for(int i=0;i<91;i++)
		printf("%s\n",*(matrix+i));
	printf("\n-------------------\n");
	return matrix;

}

char* Encrypt(char*message,char*key,char**vignere_matrix){
	//ACED DE =DBCC
	int msg_len=strlen(message);
	int key_len=strlen(key);
	int row,col_key,k=0;
	char* out=(char*)malloc(1024*sizeof(char));
	for(int i=0;i<msg_len;i++){
		row=find_row(vignere_matrix,*(message+i));
		col_key=find_row(vignere_matrix,*(key+(i%key_len)));
		*(out+k++)=*(*(vignere_matrix+row)+col_key);

	}
	//printf("%s",out);
	return out;
}

int search_element(char** arr,int col,char elem){
	int i;

	for(i=0;i<strlen(*(arr+col));i++){
		if(*(*(arr+i)+col)==elem){
			break;

		}
	}
	return i;
}

char* Decrypt(char*cipher_text,char*key,char**vignere_matrix){

	int cipher_len=strlen(cipher_text);
	int key_len=strlen(key);
	char* decrypted_text=(char*)malloc(sizeof(char)*1024);
	int col,elem_idx,k=0;
	for(int i=0;i<cipher_len;i++){
		col=find_row(vignere_matrix,*(key+(i%key_len)));
		elem_idx=search_element(vignere_matrix,col,*(cipher_text+i));
		//printf("%d\n",elem_idx);
		*(decrypted_text+k++)=*(*(vignere_matrix)+elem_idx);
	}
	//printf("\n%s\n",decrypted_text);
	return decrypted_text;
}

void main(){

char* key=(char*)malloc(sizeof(char)*1024);
char*message=(char*)malloc(sizeof(char)*1024);//1KB Allocation

printf("Enter the message to Encrypt: ");
scanf("%[^\n]%*c",message);
printf("Enter the key: ");
scanf("%[^\n]%*c",key);
printf("Vignere Matrix\n");


char**vignere_matrix=GENERATE_VIGNERE_MATRIX();
char*cipher=Encrypt(message,key,vignere_matrix);

printf("\nEncrypted Text : %s \n",cipher);
printf("Decrypted Text : %s \n",Decrypt(cipher,key,vignere_matrix));

//char*cipher=Encrypt("ACER","DE",vignere_matrix);
//Decrypt(cipher,"DE",vignere_matrix);

}
