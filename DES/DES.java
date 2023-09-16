package testss;
/*
@author : AjayBadrinath
@title  :DES 
*/
import java.util.Random;
/*
 * For Reference Tables...
static int [][]IP_inv= {
		{40,	8,	48,	16,	56,	24,	64,	32},
		{39,	7,	47,	15,	55,	23,	63,	31},
		{38,	6,	46,	14,	54,	22,	62,	30},
		{37,	5,	45,	13,	53,	21,	61,	29},
		{36,	4,	44,	12,	52,	20,	60,	28},
		{35,	3,	43,	11,	51,	19,	59,	27},
		{34,	2,	42,	10,	50,	18,	58,	26},
		{33,	1,	41,	9,	49,	17,	57,	25}

};
int [][]PC1={
	 {57,	49,	41,	33	,25,	17,	9},
	 {1	,58	,50,	42,	34	,26	,18},
	 {10	,2	,59	,51,	43,	35	,27},
	 {19	,11,	3	,60,	52,	44,	36},
	 {63	,55,	47,	39	,31	,23,	15},
	 {7	,62	,54,	46	,38	,30	,22},
	 {14,	6	,61	,53,	45	,37	,29},
	 {21,	13,	5	,28	,20,	12,	4}
};
int [][]PC2= {
		 {14,	17,	11,	24,	1,	5},
		 {3	,	28,	15,	6,	21,	10},
		 {23,	19,	12,	4,	26,	8},
		 {16,	7,	27,	20,	13,	2},
		 {41,	52,	31,	37,	47,	55},
		 {30,	40,	51,	45,	33,	48},
		 {44,	49,	39,	56,	34,	53},
		 {46,	42,	50,	36,	29,	32}

};
P - BOX   in decimal format 
* {58 ,50 ,42, 34, 26, 18, 10, 2 },
		{60, 52, 44, 36, 28 ,20 ,12 ,4},
		{62, 54, 46, 38, 30, 22, 14, 6 },
		{64 ,56 ,48 ,40 ,32 ,24 ,16, 8},
		{57 ,49 ,41 ,33 ,25 ,17 ,9 ,1 },
		{59 ,51, 43, 35, 27, 19, 11, 3},
		{61, 53 ,45 ,37 ,29 ,21 ,13 ,5 },
		{63 ,55 ,47, 39, 31, 23 ,15, 7 }
* 
* just to make stuff cooler i am making all into hex format nah .. for better readablity!
* P BOX OBTAINED FROM : 
* 		https://csrc.nist.gov/files/pubs/fips/46-3/final/docs/fips46-3.pdf#page=15
* 
* 
 FINAL PERMUTATION
		{16 ,7 ,20, 21},
		{29 ,12 ,28, 17},
		{1 ,15 ,23 ,26},
		{5 ,18 ,31, 10},
		{2 ,8 ,24, 14},
		{32 ,27, 3 ,9},
		{19 ,13 ,30, 6},
		{22 ,11, 4 ,25} 
8 SET SBOX
		 {
			   {14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7},
		S1	   { 0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8},
			   { 4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
			   {15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13},
			   },
			 
			   {
			   {15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10},
		S2   		{ 3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5},
			   { 0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
			   {13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9},
			   },
			 
			   {
			   {10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8},
		S3	   {13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1},
			   {13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7},
			   { 1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12},
			   },
			 
			   {
			   { 7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15},
			   {13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9},
		S4	   {10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4},
			   { 3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14},
			   },
			 
			   {
			   { 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9},
			   {14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6},
		S5	   { 4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14},
			   {11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3},
			   },
			 
			   {
			   {12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11},
			   {10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8},
		S6	   { 9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6},
			   { 4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13},
			   },
			 
			   {
			   { 4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1},
		S7	   {13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6},
			   { 1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2},
			   { 6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12},
			   },
			 
			   {
			   {13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7},
		S8	   { 1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2},
			   { 7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8},
			   { 2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11},
			   },

};
*/

class KeyGen{
	String Key="";
	 void generateKey() {
		Random r=new Random();
		for (int i=0;i<64;i++) {
			Key+=r.nextInt(2);
		}
		System.out.println(Key);
	}
	 
	 int [][]PC1= {
			 {0x39,	0x31,	0x29,	0x21,	0x19,	0x11,	0x9,	},
			 {0x1,	0x3a,	0x32,	0x2a,	0x22,	0x1a,	0x12,	},
			 {0xa,	0x2,	0x3b,	0x33,	0x2b,	0x23,	0x1b,	},
			 {0x13,	0xb,	0x3,	0x3c,	0x34,	0x2c,	0x24,	},
			 {0x3f,	0x37,	0x2f,	0x27,	0x1f,	0x17,	0xf,	},
			 {0x7,	0x3e,	0x36,	0x2e,	0x26,	0x1e,	0x16,	},
			 {0xe,	0x6,	0x3d,	0x35,	0x2d,	0x25,	0x1d,	},
			 {0x15,	0xd,	0x5,	0x1c,	0x14,	0xc,	0x4,	}
	 };
	 /*
	 
	 
	 */
	 int [][]PC2= {
			 {0xe,	0x11,	0xb,	0x18,	0x1,	0x5,	},
			 {0x3,	0x1c,	0xf,	0x6,	0x15,	0xa,	},
			 {0x17,	0x13,	0xc,	0x4,	0x1a,	0x8,	},
			 {0x10,	0x7,	0x1b,	0x14,	0xd,	0x2,	},
			 {0x29,	0x34,	0x1f,	0x25,	0x2f,	0x37,	},
			 {0x1e,	0x28,	0x33,	0x2d,	0x21,	0x30,	},
			 {0x2c,	0x31,	0x27,	0x38,	0x22,	0x35,	},
			 {0x2e,	0x2a,	0x32,	0x24,	0x1d,	0x20,	}
	 };
	 int []ROT={0x1,	0x1,	0x2,	0x2,	0x2,	0x2,	0x2,	0x2,	0x1,	0x2,	0x2,	0x2,	0x2,	0x2,	0x2,	0x1};
	
	 public  String PC2fn(String binseq) {
			String op="";
			StringBuilder d=new StringBuilder(binseq);
			//System.out.println(d.length());
			for (int i=0;i<8;i++) {
				
				for(int j=0;j<6;j++) {
					
					op+=d.charAt(PC2[i][j]-1);
					
				}
				
			}
			return new String(op);
		}

	 public String SHL(String binseq,int n_shl) {
		 //Split Key as 28 bit;
		 String Ck=binseq.substring(0,28);
		 String Dk=binseq.substring(28,56);
		 return shift(Ck,n_shl)+shift(Dk,n_shl);
		 
	 }
	 public String[] PermuteStoreKeys(String [] keys) {
		 //48 bit Keys
		 String [] PermKey=new String[17];
		 for(int i=0;i<17;i++) {
			 PermKey[i]=PC2fn(keys[i]);
		 }
		 return PermKey;
	 }
	 public String[] StoreKeys(String seed) {
		 String cd0=seed;
		 String [] key=new String[17];
		 key[0]=cd0;
		 
		 for(int i=0;i<16;i++) {
			 key[i+1]=SHL(key[i],-ROT[i]);
		 }
		 
		 return key;
		 
	 }
	 public  String shift(String seq,int n) {
		 String shifted="";
		 for (int i=0;i<seq.length();i++) {
			 shifted+=seq.charAt((i-n)%seq.length());
		 }
		 return shifted;
	 }
	 public  String PC1fn(String binseq) {
			//System.out.println(binseq);
			char tmp;
			int ctr=0;
			String op="";
			StringBuilder d=new StringBuilder(binseq);
			//System.out.println(d.length());
			for (int i=0;i<8;i++) {
				
				for(int j=0;j<7;j++) {
					
					op+=d.charAt(PC1[i][j]-1);
					
				}
				
				
			}
			
			//System.out.println(op);
			return new String(op);
		}
}
public class DES {
	/**/

	private static final int initial_permutation_box[][]= {
				{0x3a,	0x32,	0x2a,	0x22,	0x1a,	0x12,	0xa,	0x2 },	
				{0x3c,	0x34,	0x2c,	0x24,	0x1c,	0x14,	0xc,	0x4 },	
				{0x3e,	0x36,	0x2e,	0x26,	0x1e,	0x16,	0xe,	0x6	},
				{0x40,	0x38,	0x30,	0x28,	0x20,	0x18,	0x10,	0x8	},
				{0x39,	0x31,	0x29,	0x21,	0x19,	0x11,	0x9,	0x1	},
				{0x3b,	0x33,	0x2b,	0x23,	0x1b,	0x13,	0xb,	0x3	},
				{0x3d,	0x35,	0x2d,	0x25,	0x1d,	0x15,	0xd,	0x5	},
				{0x3f,	0x37,	0x2f,	0x27,	0x1f,	0x17,	0xf,	0x7	}
	};
	
	
	/*
	
	*/
	private static int P_FINAL[][]={
			{0x10,	0x7,	0x14,	0x15},
			{0x1d,	0xc,	0x1c,	0x11},
			{0x1,	0xf,	0x17,	0x1a},
			{0x5,	0x12,	0x1f,	0xa	},
			{0x2,	0x8,	0x18,	0xe	},
			{0x20,	0x1b,	0x3,	0x9	},
			{0x13,	0xd,	0x1e,	0x6	},
			{0x16,	0xb,	0x4,	0x19}
	};
	/*
	
	*/
	private static final int[][][] SBOX=
	{
				{
					{0xe,	0x4,	0xd,	0x1,	0x2,	0xf,	0xb,	0x8,	0x3,	0xa,	0x6,	0xc,	0x5,	0x9,	0x0,	0x7},
					{0x0,	0xf,	0x7,	0x4,	0xe,	0x2,	0xd,	0x1,	0xa,	0x6,	0xc,	0xb,	0x9,	0x5,	0x3,	0x8	},
					{0x4,	0x1,	0xe,	0x8,	0xd,	0x6,	0x2,	0xb,	0xf,	0xc,	0x9,	0x7,	0x3,	0xa,	0x5,	0x0	},
					{0xf,	0xc,	0x8,	0x2,	0x4,	0x9,	0x1,	0x7,	0x5,	0xb,	0x3,	0xe,	0xa,	0x0,	0x6,	0xd	},
				},
				
				
				{
					{0xf,	0x1,	0x8,	0xe,	0x6,	0xb,	0x3,	0x4,	0x9,	0x7,	0x2,	0xd,	0xc,	0x0,	0x5,	0xa	},
					{0x3,	0xd,	0x4,	0x7,	0xf,	0x2,	0x8,	0xe,	0xc,	0x0,	0x1,	0xa,	0x6,	0x9,	0xb,	0x5	},
					{0x0,	0xe,	0x7,	0xb,	0xa,	0x4,	0xd,	0x1,	0x5,	0x8,	0xc,	0x6,	0x9,	0x3,	0x2,	0xf	},
					{0xd,	0x8,	0xa,	0x1,	0x3,	0xf,	0x4,	0x2,	0xb,	0x6,	0x7,	0xc,	0x0,	0x5,	0xe,	0x9	},
				},
				
				
				{
					{0xa,	0x0,	0x9,	0xe,	0x6,	0x3,	0xf,	0x5,	0x1,	0xd,	0xc,	0x7,	0xb,	0x4,	0x2,	0x8	},
					{0xd,	0x7,	0x0,	0x9,	0x3,	0x4,	0x6,	0xa,	0x2,	0x8,	0x5,	0xe,	0xc,	0xb,	0xf,	0x1	},
					{0xd,	0x6,	0x4,	0x9,	0x8,	0xf,	0x3,	0x0,	0xb,	0x1,	0x2,	0xc,	0x5,	0xa,	0xe,	0x7	},
					{0x1,	0xa,	0xd,	0x0,	0x6,	0x9,	0x8,	0x7,	0x4,	0xf,	0xe,	0x3,	0xb,	0x5,	0x2,	0xc	},
				},
				
				
				{
					{0x7,	0xd,	0xe,	0x3,	0x0,	0x6,	0x9,	0xa,	0x1,	0x2,	0x8,	0x5,	0xb,	0xc,	0x4,	0xf	},
					{0xd,	0x8,	0xb,	0x5,	0x6,	0xf,	0x0,	0x3,	0x4,	0x7,	0x2,	0xc,	0x1,	0xa,	0xe,	0x9	},
					{0xa,	0x6,	0x9,	0x0,	0xc,	0xb,	0x7,	0xd,	0xf,	0x1,	0x3,	0xe,	0x5,	0x2,	0x8,	0x4	},
					{0x3,	0xf,	0x0,	0x6,	0xa,	0x1,	0xd,	0x8,	0x9,	0x4,	0x5,	0xb,	0xc,	0x7,	0x2,	0xe	},
				},
				
				
				{
					{0x2,	0xc,	0x4,	0x1,	0x7,	0xa,	0xb,	0x6,	0x8,	0x5,	0x3,	0xf,	0xd,	0x0,	0xe,	0x9	},
					{0xe,	0xb,	0x2,	0xc,	0x4,	0x7,	0xd,	0x1,	0x5,	0x0,	0xf,	0xa,	0x3,	0x9,	0x8,	0x6	},
					{0x4,	0x2,	0x1,	0xb,	0xa,	0xd,	0x7,	0x8,	0xf,	0x9,	0xc,	0x5,	0x6,	0x3,	0x0,	0xe	},
					{0xb,	0x8,	0xc,	0x7,	0x1,	0xe,	0x2,	0xd,	0x6,	0xf,	0x0,	0x9,	0xa,	0x4,	0x5,	0x3	},
				},
				
				
				{
					{0xc,	0x1,	0xa,	0xf,	0x9,	0x2,	0x6,	0x8,	0x0,	0xd,	0x3,	0x4,	0xe,	0x7,	0x5,	0xb	},
					{0xa,	0xf,	0x4,	0x2,	0x7,	0xc,	0x9,	0x5,	0x6,	0x1,	0xd,	0xe,	0x0,	0xb,	0x3,	0x8	},
					{0x9,	0xe,	0xf,	0x5,	0x2,	0x8,	0xc,	0x3,	0x7,	0x0,	0x4,	0xa,	0x1,	0xd,	0xb,	0x6	},
					{0x4,	0x3,	0x2,	0xc,	0x9,	0x5,	0xf,	0xa,	0xb,	0xe,	0x1,	0x7,	0x6,	0x0,	0x8,	0xd	},
				},
				
				
				{
					{0x4,	0xb,	0x2,	0xe,	0xf,	0x0,	0x8,	0xd,	0x3,	0xc,	0x9,	0x7,	0x5,	0xa,	0x6,	0x1},
					{0xd,	0x0,	0xb,	0x7,	0x4,	0x9,	0x1,	0xa,	0xe,	0x3,	0x5,	0xc,	0x2,	0xf,	0x8,	0x6	},
					{0x1,	0x4,	0xb,	0xd,	0xc,	0x3,	0x7,	0xe,	0xa,	0xf,	0x6,	0x8,	0x0,	0x5,	0x9,	0x2},
					{0x6,	0xb,	0xd,	0x8,	0x1,	0x4,	0xa,	0x7,	0x9,	0x5,	0x0,	0xf,	0xe,	0x2,	0x3,	0xc},
				},
				
				
				{
					{0xd,	0x2,	0x8,	0x4,	0x6,	0xf,	0xb,	0x1,	0xa,	0x9,	0x3,	0xe,	0x5,	0x0,	0xc,	0x7},
					{0x1,	0xf,	0xd,	0x8,	0xa,	0x3,	0x7,	0x4,	0xc,	0x5,	0x6,	0xb,	0x0,	0xe,	0x9,	0x2},
					{0x7,	0xb,	0x4,	0x1,	0x9,	0xc,	0xe,	0x2,	0x0,	0x6,	0xa,	0xd,	0xf,	0x3,	0x5,	0x8},
					{0x2,	0x1,	0xe,	0x7,	0x4,	0xa,	0x8,	0xd,	0xf,	0xc,	0x9,	0x0,	0x3,	0x5,	0x6,	0xb}
				}
				
	
	};
	/*{32   ,  1 ,   2 ,    3,     4,    5},
            {4     ,5    ,6    , 7    , 8    ,9},
            {8    , 9   ,10,    11   , 12  , 13},
           {12    ,13  , 14 ,   15   , 16  , 17},
           {16   , 17 ,  18  ,  19    ,20  , 21},
           {20   , 21  , 22   , 23  ,  24  , 25},
           {24   , 25 ,  26   , 27   , 28 ,  29},
           {28   , 29 ,  30  ,  31 ,   32   , 1}
	 * */
	private static final int[][]ExpansionBox= {
			{0x20,	0x1,	0x2,	0x3,	0x4,	0x5},
			{0x4,	0x5,	0x6,	0x7,	0x8,	0x9},
			{0x8,	0x9,	0xa,	0xb,	0xc,	0xd},
			{0xc,	0xd,	0xe,	0xf,	0x10,	0x11},
			{0x10,	0x11,	0x12,	0x13,	0x14,	0x15},
			{0x14,	0x15,	0x16,	0x17,	0x18,	0x19},
			{0x18,	0x19,	0x1a,	0x1b,	0x1c,	0x1d},
			{0x1c,	0x1d,	0x1e,	0x1f,	0x20,	0x1}
	};
	public  String ExpansionBoxMap(String binseq) {
		//System.out.println(binseq);
		char tmp;
		int ctr=0;
		String op="";
		StringBuilder d=new StringBuilder(binseq);
		//System.out.println(d.length());
		for (int i=0;i<8;i++) {
			
			for(int j=0;j<6;j++) {
				//tmp=d.charAt(ctr);
				//System.out.println(ctr);
				
				op+=d.charAt(ExpansionBox[i][j]-1) ; 
				//d.setCharAt(ExpansionBox[i][j]-1, tmp);
				//System.out.println(ExpansionBox[i][j]-1);
				//ctr++;
			}
			
			
		}
		
		//System.out.println(op);
		return op;
	}
	
	 void PE() {
		for(int i=0;i<8;i++) {
			System.out.print("{");
			for(int j=0;j<6;j++) {
				System.out.print("0x"+Integer.toHexString(ExpansionBox[i][j])+",\t");
			}
			System.out.print("},");
			System.out.println();
		}
	}
	@Deprecated
	
	 void PB() {
		for(int j=0;j<8;j++) {
			System.out.print("{");
			for(int i=0;i<4;i++) {
				System.out.print("0x"+Integer.toHexString(P_FINAL[j][i])+",\t");
			}
			System.out.print("}");
			System.out.println();
		}
	}
	@Deprecated
	 void printSBOX() {
		for (int i=0;i<8;i++) {
			System.out.print("{");
			for(int j=0;j<4;j++) {
				System.out.print("{");
				for(int k=0;k<16;k++) {
					//System.out.print(SBOX[i][j][k]+",\t");
					System.out.print("0x"+Integer.toHexString(SBOX[i][j][k])+",\t");
				}
				System.out.print("},");
				System.out.println();
			}
			System.out.print("},");
			System.out.println();
		}
	}
	/*
	 * xxxxxx-xxxxxx-xxxxxx-xxxxxx-xxxxxx-xxxxxx-xxxxxx-xxxxxx
	 * 6 bit groupded 8 times or split whatever!
	 * 
	 * */
	public  String MapToSBox(String binseq) {
		assert binseq.length()==48;
		int ref;
		String tmp="",tmp1="";
		int l=0;
		int rowno,colno;
		int op;
		String r="",c="";
		String s1="";
		for (int i=0;i<8;i++) {
			tmp=binseq.substring(l, l+6);
			//System.out.println(tmp);
			//rtmp.charAt(0)+tmp.charAt(5);
			//r.concat(tmp.charAt(0)+tmp.charAt(5));
			r+=tmp.substring(0, 1)+tmp.substring(5,6);
			//System.out.println(r);
			rowno=Integer.parseInt(r, 2);
			c+=tmp.substring(1, 5);
			colno=Integer.parseInt(c,2);
			op=SBOX[i][rowno][colno];
			
			tmp1=Integer.toBinaryString(op);
			s1+=String.format("%4s",tmp1).replace(' ', '0');
			
			r="";
			c="";
			l+=6;
		}
		//System.out.println(s1);
		return s1;
	}
	public String PadZerosWithMultiple8(String binseq,int diff) {
		String pad="";
		//int diff=binseq.length()%8;
			
		for(int i=0;i<diff;i++) {
				pad+='0';
				
		}
		return pad+binseq;
	}
	public String PadZeros(String binseq) {
		String pad="";
		if(binseq.length()<64) {
			int diff=64-binseq.length();
			
			for(int i=0;i<diff;i++) {
				pad+='0';
				
			}
		}
		return pad+binseq;
	}
	public  String convertTextToBinary(String plainText) {
		String binseq="";
		
		
		for (int i=0;i<plainText.length();i++) {
			binseq+=String.format("%8s", Integer.toBinaryString(plainText.charAt(i))).replace(" ", "0");
		}
		
		return binseq;
	}
	/*64 bit sequence only */
	public  String InitialPermutation(String binseq) {
		//System.out.println(binseq);
		char tmp;
		int ctr=0;
		String op="";
		StringBuilder d=new StringBuilder(binseq);
		//System.out.println(d.length());
		for (int i=0;i<8;i++) {
			
			for(int j=0;j<8;j++) {
				/*
				tmp=d.charAt(ctr);
				char r=d.charAt(initial_permutation_box[i][j]-1);
				//d.setCharAt(ctr, r);
				//d.setCharAt(initial_permutation_box[i][j]-1,tmp );
				d.setCharAt(initial_permutation_box[i][j]-1, tmp);
				d.setCharAt(ctr, r);
				ctr++;
				*/
				op+=d.charAt(initial_permutation_box[i][j]-1);
				
			}
			
			
		}
		
		//System.out.println(op);
		return new String(op);
	}
	/*32 bit sequence */
	public  String Xor(String s1,String s2) {
		String res="";
		for(int i=0;i<s2.length();i++) {
			if(s1.charAt(i)==s2.charAt(i)) {
				res+='0';
			}
			else {
				res+='1';
			}
		}
		return res;
	}
	public  String FinalPermutation(String binseq) {
		
		char tmp;
		int ctr=0;
		String op="";
		StringBuilder d=new StringBuilder(binseq);
		//System.out.println(d.length());
		for (int i=0;i<8;i++) {
			//System.out.print("{");
			for(int j=0;j<4;j++) {
				
				op+=d.charAt(P_FINAL[i][j]-1);
				
			}
			
			
		}
		
		String out=new String(op);
		return out;
	}

}
