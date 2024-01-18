/*
                                        Implementation Of GOST - R- 3412-2015 (Kuznechik)  


Author: AjayBadrinath
Date :  18-01-24
Version :1.0
                Version Changelog: (27-12-23)
                    1.Porting  From Python ...
                    2.Extend Support For Arbitrary Prescision Numbers(gmp)





*/

#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <gmp.h>
#include <assert.h>
int  pi[]={
    252, 238, 221, 17, 207, 110, 49, 22, 251, 196, 250, 218, 35, 197, 4, 77, 233,
119, 240, 219, 147, 46, 153, 186, 23, 54, 241, 187, 20, 205, 95, 193, 249, 24, 101,
90, 226, 92, 239, 33, 129, 28, 60, 66, 139, 1, 142, 79, 5, 132, 2, 174, 227, 106, 143,
160, 6, 11, 237, 152, 127, 212, 211, 31, 235, 52, 44, 81, 234, 200, 72, 171, 242, 42,
104, 162, 253, 58, 206, 204, 181, 112, 14, 86, 8, 12, 118, 18, 191, 114, 19, 71, 156,
183, 93, 135, 21, 161, 150, 41, 16, 123, 154, 199, 243, 145, 120, 111, 157, 158, 178,
177, 50, 117, 25, 61, 255, 53, 138, 126, 109, 84, 198, 128, 195, 189, 13, 87, 223,
245, 36, 169, 62, 168, 67, 201, 215, 121, 214, 246, 124, 34, 185, 3, 224, 15, 236,
222, 122, 148, 176, 188, 220, 232, 40, 80, 78, 51, 10, 74, 167, 151, 96, 115, 30, 0,
98, 68, 26, 184, 56, 130, 100, 159, 38, 65, 173, 69, 70, 146, 39, 94, 85, 47, 140, 163,
165, 125, 105, 213, 149, 59, 7, 88, 179, 64, 134, 172, 29, 247, 48, 55, 107, 228, 136,
217, 231, 137, 225, 27, 131, 73, 76, 63, 248, 254, 141, 83, 170, 144, 202, 216, 133,
97, 32, 113, 103, 164, 45, 43, 9, 91, 203, 155, 37, 208, 190, 229, 108, 82, 89, 166,
116, 210, 230, 244, 180, 192, 209, 102, 175, 194, 57, 75, 99, 182
};
int pi_inv[]={
    165, 45, 50, 143, 14, 48, 56, 192, 84, 230, 158,
    57, 85, 126, 82, 145, 100, 3, 87, 90, 28, 96,
    7, 24, 33, 114, 168, 209, 41, 198, 164, 63, 224,
    39, 141, 12, 130, 234, 174, 180, 154, 99, 73, 229,
    66, 228, 21, 183, 200, 6, 112, 157, 65, 117, 25,
    201, 170, 252, 77, 191, 42, 115, 132, 213, 195, 175,
    43, 134, 167, 177, 178, 91, 70, 211, 159, 253, 212,
    15, 156, 47, 155, 67, 239, 217, 121, 182, 83, 127,
    193, 240, 35, 231, 37, 94, 181, 30, 162, 223, 166,
    254, 172, 34, 249, 226, 74, 188, 53, 202, 238, 120,
    5, 107, 81, 225, 89, 163, 242, 113, 86, 17, 106,
    137, 148, 101, 140, 187, 119, 60, 123, 40, 171, 210,
    49, 222, 196, 95, 204, 207, 118, 44, 184, 216, 46,
    54, 219, 105, 179, 20, 149, 190, 98, 161, 59, 22,
    102, 233, 92, 108, 109, 173, 55, 97, 75, 185, 227,
    186, 241, 160, 133, 131, 218, 71, 197, 176, 51, 250,
    150, 111, 110, 194, 246, 80, 255, 93, 169, 142, 23,
    27, 151, 125, 236, 88, 247, 31, 251, 124, 9, 13,
    122, 103, 69, 135, 220, 232, 79, 29, 78, 4, 235,
    248, 243, 62, 61, 189, 138, 136, 221, 205, 11, 19,
    152, 2, 147, 128, 144, 208, 36, 52, 203, 237, 244,
    206, 153, 16, 68, 64, 146, 58, 1, 38, 18, 26,
    72, 104, 245, 129, 139, 199, 214, 32, 10, 8, 0,
    76, 215, 116 
};

#define mask_128 "ffffffffffffffffffffffffffffffff"
/**
 * @brief 
 *  Functions pertaining to gmp returns void . as params are implicitly changed 
 * 
 * @param res 
 * @param x 
 */
void linear_Transformation(mpz_t ,mpz_t );
void L_Transformation(mpz_t ,mpz_t );
void S_Transformation(mpz_t res,mpz_t x){
    mpz_t s,tmp,tmp2,x_tmp;
    mpz_init(s);
    mpz_init(tmp);
    mpz_init(x_tmp);
    mpz_init(tmp2);
    mpz_t mask_8;
    mpz_init(mask_8);
    mpz_set_ui(s,0);
    mpz_set_ui(tmp,0);
    mpz_set_ui(tmp2,0);
    mpz_set_ui(x_tmp,0);
    mpz_set_str(mask_8,"ff",16);
    //int ret = mpz_set_str(mask_8, "dfa", 16);
//printf("Return value: %d\n", ret);
    for(int i=15;i>=0;i--){
        //s<<=8;
        mpz_set(x_tmp,x);
        mpz_mul_2exp(s, s, 8);
        mpz_tdiv_q_2exp(x_tmp, x_tmp, 8*i);
        mpz_and(tmp,x_tmp,mask_8);
        //gmp_printf("Val: %Zx\n", tmp);
        mpz_set_ui(tmp2,pi[mpz_get_ui(tmp)]);
        mpz_xor(s,s,tmp2);
        //mpz_xor(s,s,pi[mpz_get_ui(mpz_and(,mask_8))]);//x>>8
        //s^=( pi[(x>>8*i&0xff)]);

    }
    mpz_set(res,s);
    mpz_clear(tmp);
    mpz_clear(tmp2);
    mpz_clear(s);
    mpz_clear(mask_8);
    mpz_clear(x_tmp);
}

void S_Inv_Transformation(mpz_t res,mpz_t x){
    mpz_t s,tmp,tmp2,x_tmp;
    mpz_init(s);
    mpz_init(tmp);
    mpz_init(x_tmp);
    mpz_init(tmp2);
    mpz_t mask_8;
    mpz_init(mask_8);
    mpz_set_ui(s,0);
    mpz_set_ui(tmp,0);
    mpz_set_ui(tmp2,0);
    mpz_set_ui(x_tmp,0);
    mpz_set_str(mask_8,"ff",16);
    //int ret = mpz_set_str(mask_8, "dfa", 16);
//printf("Return value: %d\n", ret);
    for(int i=15;i>=0;i--){
        //s<<=8;
        mpz_set(x_tmp,x);
        mpz_mul_2exp(s, s, 8);
        mpz_tdiv_q_2exp(x_tmp, x_tmp, 8*i);
        mpz_and(tmp,x_tmp,mask_8);
        //gmp_printf("Val: %Zx\n", tmp);
        mpz_set_ui(tmp2,pi_inv[mpz_get_ui(tmp)]);
        mpz_xor(s,s,tmp2);
        //mpz_xor(s,s,pi[mpz_get_ui(mpz_and(,mask_8))]);//x>>8
        //s^=( pi[(x>>8*i&0xff)]);

    }
    mpz_set(res,s);
    mpz_clear(tmp);
    mpz_clear(tmp2);
    mpz_clear(s);
    mpz_clear(mask_8);
    mpz_clear(x_tmp);
}


int deg_Poly_V_128(mpz_t x){
    int deg=0;
    mpz_t tmp;
    mpz_init(tmp);
    mpz_set(tmp,x);
    while (mpz_get_ui(tmp)!=0){
        deg++;
        mpz_tdiv_q_2exp(tmp,tmp,1);
    }
    mpz_clear(tmp);
    return deg;

}
void R_Transformation(mpz_t res,mpz_t x){
    mpz_t _res_lin_,_shift_r_120,_shr_8,_xor_;
    mpz_init(_res_lin_);
    mpz_init(_shift_r_120);
    mpz_init(_shr_8);
    mpz_init(_xor_);
    linear_Transformation(_res_lin_,x);
    mpz_mul_2exp(_shift_r_120,_res_lin_,120);
    mpz_tdiv_q_2exp(_shr_8,x,8);
    mpz_xor(res,_shift_r_120,_shr_8);
    mpz_clear(_res_lin_);
    mpz_clear(_shift_r_120);
    mpz_clear(_shr_8);
    mpz_clear(_xor_);

}

void Multiply_Poly_V_128(mpz_t res,mpz_t x ,mpz_t y){
    mpz_t c,lsh_deg,tmp,ones;
    mpz_init(c);
    mpz_init(lsh_deg);
    mpz_init(tmp);
    mpz_init(ones);
    mpz_set_str(ones,"1",10);
    int degree;
    while (mpz_get_ui(y)!=0){
        mpz_and(tmp,y,ones);
        if(tmp){
            mpz_mul_2exp(lsh_deg,x,degree);
            mpz_xor(c,c,lsh_deg);
        }
        degree++;
        mpz_tdiv_q_2exp(y,y,1);

    }
    mpz_set(res,c);
    mpz_clear(lsh_deg);
    mpz_clear(c);
    mpz_clear(tmp);
    mpz_clear(ones);



}
void Mod_Poly_Reduction(mpz_t res,mpz_t x,mpz_t m){
    mpz_t z,tmp;
    mpz_init(z);
   // mpz_init(difference);
    mpz_init(tmp);
    int difference=0;
    mpz_set(z,x);

    while (1){
        if((deg_Poly_V_128(z)<deg_Poly_V_128(m))){
            break;
        }
        else{
            difference=deg_Poly_V_128(z)-deg_Poly_V_128(m);

            mpz_mul_2exp(tmp,m,difference);
            mpz_xor(z,z,tmp);

        }
    }
    mpz_set(res,z);
    mpz_clear(tmp);
    mpz_clear(z);
}

void linear_Transformation(mpz_t res,mpz_t x){
    int _map_[]={
        1, 148, 32, 133, 16, 194, 192, 1, 251, 1, 192, 194, 16, 133, 32, 148
    };
    mpz_t _tmp_shift_8,_tmp_mask,_tmp_8_and,m_res,V_8_Field,_mod_res,_m;
    mpz_init(_m);
    mpz_init(_tmp_shift_8);
    mpz_init(_tmp_mask);
    mpz_init(_tmp_8_and);
    mpz_init(m_res);
    mpz_init(V_8_Field);
    mpz_init(_mod_res);
    mpz_set_str(V_8_Field,"111000011",2);
    mpz_set_str(_tmp_mask,"ff",16);
    
    for (int i=15;i>=0;i--){
        mpz_tdiv_q_2exp(_tmp_shift_8,x,8*i);
        mpz_and(_tmp_8_and,_tmp_shift_8,_tmp_mask);
        mpz_set_ui(_m,_map_[i]);
        Multiply_Poly_V_128(m_res,_tmp_8_and,_m);
        Mod_Poly_Reduction(_mod_res,m_res,V_8_Field);
        
    }
    mpz_clear(_tmp_shift_8);
    mpz_clear(_tmp_mask);
    mpz_clear(_tmp_8_and);
    mpz_clear(V_8_Field);
    mpz_clear(m_res);
    mpz_clear(_mod_res);
    mpz_clear(_m);

}

void Round_constant(mpz_t res,int round_no){
    mpz_t rno;
    mpz_init(rno);
    mpz_set_ui(rno,round_no);
    L_Transformation(res,rno);
    mpz_clear(rno);
}
void L_Transformation(mpz_t res,mpz_t x){
    mpz_t tmp;
    mpz_init(tmp);
    mpz_set(tmp,x);
    for (int i=0;i<16;i++){
        R_Transformation(tmp,tmp);
    }
    mpz_set(res,tmp);
    mpz_clear(tmp);

}
void L_Transformation_inverse(mpz_t res,mpz_t x){
    mpz_t tmp;
    mpz_init(tmp);
    mpz_set(tmp,x);
    for (int i=0;i<16;i++){
        R_Transformation_inverse(tmp,tmp);
    }
    mpz_set(res,tmp);
    mpz_clear(tmp);

}

mpz_t* F_Transformation(mpz_t round_const,mpz_t k1,mpz_t k2){
    mpz_t * arr=malloc(sizeof(mpz_t)*2);
    mpz_t _c1_xor_k1_,_S_tmp_,_L_tmp,_LSX_xor_k2;
    mpz_init(_c1_xor_k1_);
    mpz_init(_S_tmp_);
    mpz_init(_L_tmp);
    mpz_init(_LSX_xor_k2);
    mpz_xor(_c1_xor_k1_,round_const,k1);
    S_Transformation(_S_tmp_,_c1_xor_k1_);
    L_Transformation(_L_tmp,_S_tmp_);
    mpz_xor(_LSX_xor_k2,_L_tmp,k2);
    mpz_set(*(arr++),_LSX_xor_k2);
    mpz_set(*(arr++),k2);
    
    mpz_clear(_c1_xor_k1_);
    mpz_clear(_S_tmp_);
    mpz_clear(_L_tmp);
    mpz_clear(_LSX_xor_k2);

    return arr;
}
mpz_t * Key_Schedule(mpz_t key){
    mpz_t * key_arr=malloc(sizeof(mpz_t)*10);
    mpz_t k1,k2,mask,_shr_128,_and_mask,rnd_const;
    mpz_init(k1);
    mpz_init(k2);
    mpz_init(mask);
    mpz_init(rnd_const);
    mpz_init(_shr_128);
    mpz_init(_and_mask);
    mpz_set_str(mask,mask_128,16);
    mpz_tdiv_q_2exp(_shr_128,key,128);
    mpz_and(_and_mask,_shr_128,mask);
    mpz_set(k1,_and_mask);
    mpz_and(_and_mask,key,mask);
    mpz_set(k2,_and_mask);
    mpz_set(*(key_arr++),k1);
    mpz_set(*(key_arr++),k2);
    mpz_t * arr=malloc(sizeof(mpz_t)*2);

    for (int i=1;i<=32;i++){
        Round_constant(rnd_const,i);
        arr=F_Transformation(rnd_const,k1,k2);
        mpz_set(k1,*(arr++));
        mpz_set(k2,*(arr));
        if(i%8==0){
            mpz_set(*(key_arr++),k1);
            mpz_set(*(key_arr++),k2);
        }
    }
    mpz_clear(k1);
    mpz_clear(k2);
    mpz_clear(mask);
    mpz_clear(rnd_const);
    mpz_clear(_shr_128);
    mpz_clear(_and_mask);
    return key_arr;
}
void encrypt(mpz_t res,mpz_t message,mpz_t key){
    mpz_t * key_arr=malloc(sizeof(mpz_t)*10);

    mpz_t msg,_xor,_Sx_,_lsx_;
    mpz_init(_xor);
    mpz_init(_Sx_);
    mpz_init(_lsx_);
    mpz_init(msg);
    mpz_set(msg,message);

    key_arr=Key_Schedule(key);
    for (int i=0;i<9;i++){
        mpz_xor(_xor,key_arr[i],msg);
        S_Transformation(_Sx_,_xor);
        L_Transformation(_lsx_,_Sx_);
        mpz_set(msg,_lsx_);

    }
    mpz_xor(res,msg,key_arr[9]);
    mpz_clear(_xor);
    mpz_clear(_Sx_);
    mpz_clear(_lsx_);
    mpz_clear(msg);
}
int main(){
//printf("%lx",S_Transformation(0xffeeddccbbaa99881122334455667700)>>32);
mpz_t test,msg,res,key,x,m;
mpz_init(test);
mpz_init(res);
mpz_init(key);
mpz_init(x);
mpz_init(m);

mpz_set_str(x,"11111100",2);
mpz_set_str(m,"100010",2);
mpz_init(msg);
mpz_set_str(key,"8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef",16);
mpz_set_str(msg,"1122334455667700ffeeddccbbaa9988",16);

mpz_set_ui(res,10);
mpz_set_str(test,"b66cd8887d38e8d77765aeea0c9a7efc",16);
encrypt(res,msg,key);
//Mod_Poly_Reduction(res,x,m);
//S_Transformation(res,test);
//S_Inv_Transformation(res,res);
//Multiply_Poly_V_128(res,test,test);
gmp_printf("Val: %Zx\n", res);
}
