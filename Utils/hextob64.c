#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
int find(char*base,int length,char find){
	for(int i=0;i<length;i++){
			if(*(base+i)==(find)){
				return i;
			}
	}
	return -1;
}
char* dectoBin(int num){
int i=0;
int bin[8];
for(int i=0;i<8;i++){bin[i]=0;}
while(num>0){
	bin[i++]=num%2;
	num=num/2;
	
}
char* binary=(char*)malloc(8*sizeof(char));
//char binary[8];
int k=0;
for(int i=5;i>=0;i--){
	//printf("%d",bin[i]);
	*(binary+k)=bin[i]+'0';k++;
}
//printf("%s",binary);

return binary;
}
char* hextob64(char* hexstring,int length){
	char*mapping="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
//	printf("%d",dectoBin(find(mapping,63,'e')));
	char* concat=(char*)malloc(100*sizeof(char));
	int k=0;
	for(int i=0;i<length;i++){
		concat=strcat(concat,dectoBin(find(mapping,64,*(hexstring+i))));

	}
	printf("%s",concat);
}
int main(){
	hextob64("ef9af374",10);
}


