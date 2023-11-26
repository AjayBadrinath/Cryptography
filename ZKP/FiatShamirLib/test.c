/*


Test For the FSP Implementation  

The Functions are yet to be tested on a TCP Implementation by exchanging values between sender and reciever.

*/

#include "FiatShamir.c"

int main(){
    fsp_snd* sender=(fsp_snd*)malloc(sizeof(fsp_snd));
    fsp_recv*reciever=(fsp_recv*)malloc(sizeof(fsp_recv));
    lli n=Setup_reciever(reciever);printf("\nCommon N:%lld\n",n);
    lli pk=Setup_sender(sender,n,3);printf("PublicKey:%lld\n",pk);
    lli x=commit_phase(sender);printf("X:%lld\n",x);
    lli challenge=challenge_phase(reciever,pk);printf("challenge:%lld\n",challenge);
    lli y=response_phase(sender,challenge);printf("Y:%lld\n",y);
    (verification_phase(reciever,x,y))?printf("Authenticated\n"):printf("Not Authenticated\n");

}
