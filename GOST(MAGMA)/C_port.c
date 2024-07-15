/*
                                        Implementation Of GOST - R- 3412-2015 (MAGMA)  


Author: AjayBadrinath
Date :  14-07-24
Vesion :1.1
                Version Changelog: (14-07-24)
                    1.Non Threaded C version Done 
                    2.Added Comments & Docstring.
                     


Class implementing GOST - 64 bit Blocks .


    1. This is a Fiestel Cipher Based Encryption on 64 bit blocks as per Specs Defined by Russian  Union  Standards.
    There is not much Information about this Cipher Developed by the Soviet Union During the time when NSA Developed DES- 56. This Remained as an Alternative 
    So Here's the Pdf in Russian :  https://web.archive.org/web/20150924113434/http://tc26.ru/standard/gost/GOST_R_3412-2015.pdf

    

*/

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <gmp.h>
#include <assert.h>
#include <sys/time.h>
#include <time.h>
#include <omp.h>

/*
The S-BOX for GOST MAGMA Cipher 

*/

int s_box[8][16]={
    {0xC, 0x4, 0x6, 0x2, 0xA, 0x5, 0xB, 0x9, 0xE, 0x8, 0xD, 0x7, 0x0, 0x3, 0xF, 0x1},
	{0x6, 0x8, 0x2, 0x3, 0x9, 0xA, 0x5, 0xC, 0x1, 0xE, 0x4, 0x7, 0xB, 0xD, 0x0, 0xF},
	{0xB, 0x3, 0x5, 0x8, 0x2, 0xF, 0xA, 0xD, 0xE, 0x1, 0x7, 0x4, 0xC, 0x9, 0x6, 0x0},
	{0xC, 0x8, 0x2, 0x1, 0xD, 0x4, 0xF, 0x6, 0x7, 0x0, 0xA, 0x5, 0x3, 0xE, 0x9, 0xB},
	{0x7, 0xF, 0x5, 0xA, 0x8, 0x1, 0x6, 0xD, 0x0, 0x9, 0x3, 0xE, 0xB, 0x4, 0x2, 0xC},
	{0x5, 0xD, 0xF, 0x6, 0x9, 0x2, 0xC, 0xA, 0xB, 0x7, 0x8, 0x1, 0x4, 0x3, 0xE, 0x0},
	{0x8, 0xE, 0x2, 0x5, 0x6, 0x9, 0x1, 0xC, 0xF, 0x4, 0xB, 0x0, 0xD, 0xA, 0x3, 0x7},
	{0x1, 0x7, 0xE, 0xD, 0x0, 0x5, 0x8, 0x3, 0x4, 0xF, 0xA, 0x6, 0x9, 0xC, 0xB, 0x2}
};

/*
        Native Function For Left Circular Shift by 11.
        
        0xffffffff==2**32-1 ->MASK For 32 bit ints.

        Parameters: 32-bit int 
        Returns   : (int32) ->  Circular Shift By 11


*/
void ROL(mpz_t res, mpz_t x){
    mpz_t tmp, tmp2, tmp3,tmp4,tmp5,rol,mask_64;
    mpz_init(tmp);
    mpz_init(tmp2);
    mpz_init(tmp3);
    mpz_init(tmp4);
    mpz_init(rol);
     //gmp_printf("Transform: %Zx\n", x);
    mpz_init(mask_64);
    mpz_set_ui(tmp,0);
    mpz_set_ui(tmp2,0);
    mpz_set_ui(tmp3,0);
    mpz_set_ui(rol,0);

    mpz_set_ui(tmp4,0);
    mpz_set_str(mask_64,"ffffffff",16);
    mpz_set(tmp,x);
    mpz_set(tmp2,x);

    //mpz_mul_2exp(s, s, 8);lsh
    //mpz_tdiv_q_2exp(x_tmp, x_tmp, 8*i);rsh
    mpz_tdiv_q_2exp(tmp3,tmp,21);
   
    mpz_mul_2exp(tmp4,tmp2,11);
    mpz_xor(tmp4,tmp4,tmp3);
    mpz_and(rol,tmp4,mask_64);
    mpz_set(res,rol);
    mpz_clear(tmp);
    mpz_clear(tmp2);
    mpz_clear(tmp3);
    mpz_clear(tmp4);
    mpz_clear(rol);
    mpz_clear(mask_64);



}

/*
        Transform Function t(x) : -> Non Linear Bijective Function  
        Extract from MSB to LSB & Substitute from SBox 
        Parameters: 32bit int x
        Return    : t(x)->Mapped as a Non-Linear Bijection w.r.t sbox


*/

void Transform(mpz_t res,mpz_t x){
    mpz_t z,tmp,tmp1,tmp3,mask_4;
    //gmp_printf("Transform: %s\n", x);
    //mpz_out_str(stdout, 16, x);
    int col=0;
    mpz_init(tmp);
    mpz_init(z);
    mpz_init(tmp1);
    mpz_init(mask_4);
    mpz_init(tmp3);
    mpz_set_ui(tmp1,0);
    mpz_set_ui(z,0);
    //mpz_set_ui(tmp,x);
    mpz_set_ui(tmp3,0);
    mpz_set(tmp,x);
    mpz_set_str(mask_4,"f",16);
    for (int i=7;i>=0;i--){
        mpz_mul_2exp(z,z,4);
        mpz_tdiv_q_2exp(tmp1,tmp,4*i);
        mpz_and(tmp1,tmp1,mask_4);
        mpz_set_ui(tmp3,s_box[i][mpz_get_ui(tmp1)]);
        mpz_xor(z,z,tmp3);

    }
    mpz_set(res,z);
    mpz_clear(tmp);
    mpz_clear(z);
    mpz_clear(tmp1);
    mpz_clear(mask_4);

}

/*
    Russian Standard G_Function applies (AM(key+msg))>>>11


*/

void _G_Func_(mpz_t rop,mpz_t x, mpz_t k){

    mpz_t _tr,_rol,_sum,_modexprop,_mod,_exp;
    mpz_init(_tr);
    mpz_init(_rol);

    mpz_init(_sum);
    mpz_init(_modexprop);
    mpz_init(_mod);
    mpz_init(_exp);
    mpz_set_ui(_tr,0);
    mpz_set_ui(_rol,0);
    mpz_set_ui(_sum,0);
    mpz_set_ui(_modexprop,0);
    mpz_set_ui(_mod,2);
    mpz_set_ui(_exp,32);
    mpz_add(_sum,x,k);
    mpz_pow_ui (_modexprop,_mod,32);
    mpz_mod(_modexprop,_sum,_modexprop);
    //gmp_printf("mod: %Zx\n", _modexprop);

    Transform(_tr,_modexprop);
    ROL(_rol,_tr);
    mpz_set(rop,_rol);
    mpz_clear(_tr);
    mpz_clear(_rol);
    mpz_clear(_sum);
    mpz_clear(_modexprop);
    mpz_clear(_mod);
    mpz_clear(_exp);
}
/*
    Key Schedule for GOST Which Split 256 bit keys to 32 Sub Key Round for Each Round ..
    
    To Be Specific : 

            The key Schdule First Splits the 256 Bit Keys to 8 sub keys of 32 bit length 
            Then For 
                Round (1->8(incl)) : MSB->LSB (32bit split) ===>Ascending Phase
                Round (9->16(incl)) : MSB->LSB (32bit split) ===> Ascending Phase
                Round (17->24(incl)) : MSB->LSB (32bit split) ===> Ascending Phase
                Round (25->32(incl)) : LSB->MSB (32bit split) ===> Descending Phase


*/
mpz_t* KeySchedule( mpz_t key){
    mpz_t mask_64,_tr,_tmp;
    mpz_init(mask_64);
    mpz_init(_tr);
    mpz_init(_tmp);
    mpz_set_ui(_tmp,0);
    mpz_set_ui(_tr,0);
    int ctr=0;

    mpz_t * key_arr=malloc(sizeof(mpz_t)*32);
    for(int i=0;i<32;i++){
        mpz_init(key_arr[i]);
        mpz_set_ui(key_arr[i],0);
    }
    mpz_set_str(mask_64,"ffffffff",16);
    for (int i =7;i>=0;i--){
        mpz_tdiv_q_2exp(_tr,key,32*i);
        mpz_and(_tmp,_tr,mask_64);
        mpz_set(key_arr[ctr++],_tmp);
    }
    for(int i=8;i<16;i++){
        mpz_set(key_arr[ctr++],key_arr[i-8]);
    }
    for(int i=16;i<24;i++){
        mpz_set(key_arr[ctr++],key_arr[i-16]);
    }
    for(int i=24;i<32;i++){
        mpz_set(key_arr[ctr++],key_arr[31-i]);
    }

    mpz_clear(mask_64);
    mpz_clear(_tr);
    mpz_clear(_tmp);

    return key_arr;
    


}
/*
    Fiestel Based Encryption/Decryption  with Transformations. For 32 Rounds   
    Require Already Instantiated SubKeyArray as param 
*/
void _Crypt_Tool(mpz_t res,mpz_t* subKeyArr,mpz_t pt,char* mode){
    mpz_t tmp,left,right,mask_64,tmp1,tmp2;
    mpz_init(tmp);
    mpz_init(left);
    mpz_init(right);
    mpz_init(tmp2);
    mpz_init(tmp1);
    mpz_init(mask_64);
    mpz_set_str(mask_64,"ffffffff",16);

    //mpz_set_ui(tmp,0);
    mpz_set_ui(left,0);
    mpz_set_ui(right,0);
    mpz_set_ui(tmp2,0);
    mpz_set(tmp,pt);
    mpz_tdiv_q_2exp(tmp1,pt,32);
    mpz_and(tmp1,tmp1,mask_64);

    int _mode_lb;
    int _mode_ub;
    mpz_set(left,tmp1);
    mpz_and(tmp1,pt,mask_64);
    mpz_set(right,tmp1);
 /**
  * @brief Adopting mode based branching as enc/dec remain same ,just the order differ in fiestel !
  * 
  */
    if(strcmp(mode,"encrypt")==0){
        for(int i=0;i<31;i++){
            
            
            mpz_set_ui(tmp2,0);
            _G_Func_(tmp2,right,subKeyArr[i]);
            mpz_xor(tmp2,left,tmp2);
            mpz_set(left,right);
            mpz_set(right,tmp2);
            


        }


        /*Part of G* Transformation*/
        _G_Func_(tmp2,right,subKeyArr[31]);

    }else if (strcmp(mode,"decrypt")==0){
        for(int i=31;i>0;i--){
            
            
            mpz_set_ui(tmp2,0);
            _G_Func_(tmp2,right,subKeyArr[i]);
            mpz_xor(tmp2,left,tmp2);
            mpz_set(left,right);
            mpz_set(right,tmp2);
            


        }
        /*Part of G* Transformation*/
        _G_Func_(tmp2,right,subKeyArr[0]);

    }else{
        fprintf(stdout, "[-]Mode Incorrect !\n");
        return ;

    }
/*
Defined As the G* Function that Essentially Applies the Last key && joins the Bits
*/
    mpz_xor(tmp2,tmp2,left);
    mpz_mul_2exp(tmp2,tmp2,32);
    mpz_xor(tmp2,tmp2,right);
    mpz_set(res,tmp2);

/*
Free Alloc Mem 

*/

    mpz_clear(tmp);
    mpz_clear(left);
    mpz_clear(tmp2);
    mpz_clear(right);
    mpz_clear(tmp1);
    mpz_clear(mask_64);

}



/*
For Testing purposes!!!
void main(){
    mpz_t test,msg,ret;
    mpz_init(test);
    mpz_init(msg);
    mpz_init(ret);
    mpz_set_ui(ret,10);
    mpz_set_ui(test,10);
    //
    mpz_set_str(test,"ffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff",16);
   // mpz_out_str(stdout, 10, test);
    //mpz_set_str(msg,"fedcba98",16);
    mpz_set_str(msg,"fedcba9876543210",16);

    
   // Transform(msg,test);
   //_G_Func_(ret,test,msg);
    mpz_t* key=KeySchedule(test);
    _Crypt_Tool(ret,key,msg,"encrypt");

    for(int i=0;i<32;i++){
        //gmp_printf("Transform: %d %Zx\n",i, key[i]);
    }
    gmp_printf("Transform: %Zx\n", ret);
    return;
}

*/