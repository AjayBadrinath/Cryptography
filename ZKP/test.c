/*
Test For the FSP Implementation  

The Functions are yet to be tested on a TCP Implementation by exchanging values between sender and reciever.

*/

#include "FiatShamir.c"

int main(){
    fsp_snd* sender=(fsp_snd*)malloc(sizeof(fsp_snd));
    fsp_recv*reciever=(fsp_recv*)malloc(sizeof(fsp_recv));
    int n=Setup_reciever(reciever);
    int pk=Setup_sender(sender,n,3);
    int x=commit_phase(sender);
    int challenge=challenge_phase(reciever,pk);
    int y=respose_phase(sender,challenge);
    (verification_phase(reciever,x,y))?printf("Authenticated\n"):printf("Not Authenticated\n");

}