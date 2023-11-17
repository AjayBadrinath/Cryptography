/*
 * 
 * @title  :  							NIST FIPS-180.4 2015 Implementation of SHA 256 Algorithm  For Single Block Messages 
 * @author : Ajay Badrinath
 * @version:2.0    - Added Multi Block Support .....  Changed Revision 18-11-2023;
 * 
 * Version -> Comments 
 * 
 * 				1. Added support for Multi Block Messages .
 * 				2. Refactor code and make it more modular
 * 				3. Adaptible to other modules ..
 * 				4. Bug Fixes ..
 * */
package testss;

import java.math.BigInteger;
/*
 * Class for Pre Processing 
 * 			-> Input Sanitization 
 * 			-> Convert ascii- > bin
 * 			-> Padding as per NIST Standard 
 * 			-> Convert Each message into 32 bit blocks of 16 numbers 
 * 			-> Compute Hash For H 
 * */
class PreProcess{
	
	/*
	 * Pre Processing Pipeline
	 * 	convertTextToBinary->padZeroBit->Block->getHash->sha256hash
	 * 
	 * */
	
	
	String message;
	 String sha256hash[][];
	 int n_block;
	PreProcess(String message){
		this.message=message;
	}
	
	/*
	 * Function to convert ASCII Text to 8 bit binary sequence 
	 * @param plainText - Initial ASCII String 
	 * @return - String with binary sequence
	 * */
     String convertTextToBinary(String plainText) {
		String binseq="";
		
		
		for (int i=0;i<plainText.length();i++) {
			binseq+=String.format("%8s", Integer.toBinaryString(plainText.charAt(i))).replace(" ", "0");
		}
		
		return binseq;
	}
     
     /*
      * Function to Pad Zeros based on NIST Standard @link{https://csrc.nist.gov/files/pubs/fips/180-4/upd1/final/docs/fips180-4-draft-aug2014.pdf}
      * @param - String which is a binary stream 
      * @returns - binary string of length 512 bits
      * 
      * */
     String padZeroBit(String seq) {
    	 int len=seq.length();
    	 //k+l+1=448%512;
    	 // Rearranging k=(448%512)-l-1;
    	 seq+="1";
    	 int l=(len+1)%512;
    	 
    	 //int k=(448*n_block-len-1)%(512*n_block);
    	 int k;
    	 /*To Ensure k>0*/
    	 if(l<=448) {
    		 k=448-l;
    	 }else {
    		 k=512-(l-448);
    	 }
    	 for (int i=0;i<k;i++) {seq+="0";}
    	 seq+=String.format("%64s",Integer.toBinaryString(len)).replace(" ", "0");
    	 n_block=(int)seq.length()/512;
    	 //System.out.println(seq.length()/512);
    	 return seq;
     }
     
     /*
      * Helper function to Split 512 bitstream into blocks of each 32 bit length (4 bytes)
      * @param - Padded String of length 512
      * @return - Array of Strings of 32 bit blocks each
      */
      
     String[][] Block(String input) {
    	 //512/16=32bit word in each block
    	 String[][]blocks=new String[n_block][16];
    	 for (int i=0;i<n_block;i++) {
    		 String sub=input.substring(i*512,(i+1)*512);
    		 for (int j=0;j<sub.length()/32;j++) {
    	    		 blocks[i][j]=sub.substring(j*32,j*32+32);
    	    	 }
    	 }
    	 //String [] blocks=new String [input.length()/32];
    	 for (int i=0;i<input.length()/32;i++) {
    		// blocks[i]=input.substring(i*32,i*32+32);
    	 }
    	 return blocks;
     }
     
     /*
      * Important Function for SHA256 implementation .
      * As per NIST Standard it mentions to take first 8 primes for {H0->H7} and apply sqrt and take 1st 32 bits in hex 
      * Void function as this should not be messed with 
      * Why is this function Here? 
      * 	1. Due to Shannon Entropy  Increase the confusion/diffusion in by taking primes and apply f(primes)  in multiple iterations increase rate of diffusion increases 
      * Sets the sha256hash native to class -> not using private variable where i should have but since this is a poc this works!!!
      * But this will be revised soon to new version and support for multiple blocks will be added in other iteration >443 bits
      * */
     void  getHash() {
    	 // According to SHA standards to get the Hash 0-7 we take the square root of first 8 primes and take the fractional part of the binary representaion in hex
    	 int []primes= {2,3,5,7,11,13,17,19};
    	 String[][]hashSeq=new String[n_block][8];
    	 int idx=0;
    	 double x;
    	 long bitexpr;// store square root
    	 //for (int j=0;j<n_round;j++) {
    	 for (int i: primes) {
    		 x=Math.sqrt(i)%1;

    		 bitexpr=(long)(x*Math.pow(2, 32));

    		 hashSeq[0][idx++]=String.format("%32s", Long.toBinaryString( bitexpr)).replace(" ", "0");
    		 /*   Absolute PAIN  AS IEEE754 is not used for fpu repr  */
    		 /*Previous attempts at using ieee float format failed miserably
    		
    		  */
    	 }
    	 idx=0;
    	  
    	 //}
    	 for (int i=1;i<n_block;i++) {
    		 for(int j=0;j<8;j++) {
    			 hashSeq[i][j]="";
    		 }
    	 }
    	 sha256hash=hashSeq;
     }
   
    	 
    	 
     
}

/*
 * This class schedules the sha256 algorithm for the given string 
 * 		->Compute SHA round constant k[0 ... 64]
 * 		->Implements various SHA256 native Functions [Sigma,SIGMA][0..1] , Choose , Maj ...
 * 		->Non native general functions like shift -right rotate-right 
 * 		-> Functions to compute hash
 * 
 * */

/*
 * 
 * Sha 256 Schedule Pipeline
 * 
 * Block->getshaconst->sha256hash[hash]->PrepareSchedule(hash)->256bit hash
 * 
 * */
class sha256Schedule{
	
		String[] shaconst;
		String [] MessageSchedule;
		String [][] Block;
		sha256Schedule(String [][]block){
			Block=block;
		}
		
		/*
		 * Function to compute SHA round Key constant based off cbrt(prime) as per NIST standard 
		 * Why though ? 
		 * 			This ensures that there are no assumptions in the implementation about backdoors -> first k primes don't have any backdoor property that breaks this algorithm
		 * This function sets the shaconst [] variable for future use..
		 * 
		 * */
		
	  void getshaconst(){
	    	 /*Nah i am not wasting compute time by generating first 64 primes so get the prime from the @link{https://prime-numbers.info/}  */
	    	 int [] prime64= { 2, 3, 5, 7, 11, 13, 17, 19, 
	    			 		   23, 29, 31, 37, 41, 43, 47, 53,
	    			           59, 61, 67, 71, 73, 79, 83, 89,
	    			           97, 101, 103, 107, 109, 113, 127,
	    			           131, 137, 139, 149, 151, 157, 163, 
	    			           167, 173, 179, 181, 191, 193, 197, 
	    			           199, 211, 223, 227, 229, 233, 239,
	    			           241, 251, 257, 263, 269, 271, 277,
	    			           281, 283, 293, 307, 311};
	    	 
	    	 String shaconstSeq[]=new String[64];
	    	 int seqidx=0;double x;long bitrepr;
	    	 
	    	 /*Enumerate primes -> take cuberoot in long fmt -> reduce to first 32 bits . Integer also holds 32 bits but there are chances of overflow 
	    	  * So to keep the code base uniform for all representation mostly we will stick to long which is 64 bit and gives us leverage to 
	    	  * operate freely in our tiny 32 bit space without having to worry about overflows .
	    	  * This is important as in the future we will perform addition mod 2**32 which will tend to overflow so Int is not suitable here*/
	    	 
	    	 for (int i: prime64) {
	    		 x=Math.cbrt(i)%1;
	    		 bitrepr=(long)(x*Math.pow(2, 32));
	    		 //System.out.println(String.format("%8s", Long.toHexString( bitrepr)).replace(" ", "0"));
	    		 //shaconstSeq[seqidx++]=String.format("%8s", Long.toHexString( bitrepr)).replace(" ", "0");
	    		 shaconstSeq[seqidx++]=String.format("%32s", Long.toBinaryString( bitrepr)).replace(" ", "0");
	    	 }
	    	 
	    	 shaconst=shaconstSeq;
	  }
	  
	   /*
	    * Native function for  SHA256  
	    * Rotate Right 7 ^Rotate Right 18^Shift right 3
	    * @param - 32 bit binary sequence of a sub-round
	    * @return - 32 bit transformed  binary sequence due to multiple shr/ror convulutions.
	    * */
	      String Sigma0(String x) {
	    	long ROR=ror(Long.parseLong(x,2),7);
	    	long ROR2=ror(Long.parseLong(x,2),18);
	    	
	    	long shr=shr(Long.parseLong(x, 2),3);
	    	//long c=(long) ((ROR+ROR2+shr)%Math.pow(2, 32));
			//long c= (long) ((ROR+ROR2+shr)%Math.pow(2, 32));
	    	long c=(ROR^ROR2^shr);
	    	return String.format("%32s",Long.toBinaryString(c)).replace(" ", "0");
	    }
	      
	      /*
		    * Native function for  SHA256 
		    * Rotate Right 17 ^Rotate Right 19^Shift right 10
		    * As per NIST Std .
		    * @param - 32 bit binary sequence of a sub-round
		    * @return - 32 bit transformed  binary sequence due to multiple shr/ror convulutions.
		    * */
	    static String Sigma1(String x) {
	    	long ROR=ror(Long.parseLong(x,2),17);
	    	
	    	long ROR2=ror(Long.parseLong(x,2),19);
	    	
	    	
	    	long shr=shr(Long.parseLong(x,2),10);
	    	
			long c= ((ROR^ROR2^shr));
	    	//long c=(long) ((ROR+ROR2+shr)%Math.pow(2, 32));
	    	return String.format("%32s",Long.toBinaryString(c)).replace(" ", "0");
	    }
	    
	    /*
	     * Helper function to shift bits to k dist 
	     * mask (1L<<32)-1 Ensures that  our domain is 32 bits by performing & to x
	     * Definition (x>>>dist | x<<w-dist) w=domain
	     * @param - long x to shift
	     * @param - distance to shift
	     * @return - shifted bits of long type
	     * */
	    static long shr(long x,int dist) {
	    	long mask=(1L<<32)-1;
	    	return (x>>>dist)&mask;
	    }
	    
	    /*
	     * Helper function to rotate bits 
	     * @param - long x to rotate
	     * @param - distance to rotate
	     * @return - rotated bits of long type
	     * */
	    static long ror(long x,int dist) {
			long mask=(1L<<32)-1;
			return (x>>>dist| x<<32-dist) &mask;
		}
	    
	    /*
	     * Native function in SHA256 
	     * Perform RotateRight 2 ^RotateRight 13^RotateRight 22
	     * Used in computing t1
	     * @param - binary String 
	     * 
	     * @return - 32 bit transformed Binary String.
	     * */
	    
	    static String SIGMA0(String x) {
	    	long ROR=ror(Long.parseLong(x,2),2);
	    	
	    	long ROR2=ror(Long.parseLong(x,2),13);
	    	
	    	long ROR3=ror(Long.parseLong(x,2),22);
	    	
			long c= ((ROR^ROR2^ROR3));
	    	return String.format("%32s",Long.toBinaryString(c)).replace(" ", "0");
	    	
	    }
	    
	    /*
	     * Native function in SHA256 
	     * Perform RotateRight 2 ^RotateRight 13^RotateRight 22
	     * used in computing t1
	     * @param - binary String 
	     * @return - 32 bit transformed Binary String.
	     * */
	    
	    static String SIGMA1(String x) {
	    	long ROR=ror(Long.parseLong(x,2),6);
	    	
	    	long ROR2=ror(Long.parseLong(x,2),11);
	    	
	    	
	    	long ROR3=ror(Long.parseLong(x,2),25);
	    	
			long c= ((ROR^ROR2^ROR3));
	    	//long c=(long) ((ROR+ROR2+ROR3)%Math.pow(2, 32));
	    	return String.format("%32s",Long.toBinaryString(c)).replace(" ", "0");
	    }
	    
	    /*
	     * Native function in SHA256 
	     * Perform choice operation by chooses whether to take a bit from the first word or the second word, based on the corresponding bit in the third word. 
	     * 
	     * This function contributes to the security of SHA-256 by helping to thoroughly mix the input data
	     * used in computing t1
	     * @param - binary String 1
	     * @param - binary String 2
	     * @param - binary String 3
	     * @return - 32 bit  Binary String.
	     * */
	    static String Choose(String x,String y, String z)
	    
	    {
	    	long x1=Long.parseLong(x,2)&((1L<<32)-1);
	    	long y1=Long.parseLong(y,2)&((1L<<32)-1);
	    	long z1=Long.parseLong(z,2)&((1L<<32)-1);
	    	long c= ((x1&y1)^(~x1&z1))&((1L<<32)-1);
	    	return String.format("%32s",Long.toBinaryString(c)).replace(" ", "0");
	    }
	    
	    /*
	     * Native function to SHA256
	     * Function that ensure SAC Strict Avalance criterion -> Choose max bits in 1,2,3.
	     * @param - binary String 1
	     * @param - binary String 2
	     * @param - binary String 3
	     * @return - 32 bit  Binary String. 
	     * 
	     * */
	    static String Majority(String x, String y, String z) {
	    	long x1=Long.parseLong(x,2)&((1L<<32)-1);
	    	long y1=Long.parseLong(y,2)&((1L<<32)-1);
	    	long z1=Long.parseLong(z,2)&((1L<<32)-1);
	    	long c=((x1&y1)^(x1&z1)^(y1&z1))&((1L<<32)-1);
	    	return String.format("%32s",Long.toBinaryString(c)).replace(" ", "0");
	    }
	    
	    /*
	     * Function to perform SHA iteration per block
	     * Currently support is for only 1 block 
	     * Workings are documented inside the function.
	     * @param - HashSequence - initial sequence 
	     * @return  - String of length 256 which is the SHA(message).
	     * */
	   
	  String PrepareSchedule(String [][]HashSeq,int n_round) {
		  /* This w hold 64 bit binsry sequence which will be used for each round */
		  String []w=new String[64];
		  long mask=((1L<<32)-1);/* mask for ensuring 32 bit operations on long sets all numbers to right of 33rd bit as 1 so & with long x will give only domain from [33..64] that is 32 bits*/
		  /*initally the w[0..16] is Block[i]*/
		  String a,b,c,d,e,f,g,h;
		  int idx=0;
		  long t1,t2;
		  for (String i:Block[0]){w[idx++]=i;}
		  for (int j=idx;j<64;j++) {
				 // w[j]=Sigma1(w[j-2])+w[j-7]+Sigma0(w[j-15])+w[j-16];
				  w[j]=String.format("%32s",(Long.toBinaryString( ((long)(((Long.parseLong(Sigma1(w[j-2]),2)&mask)+(Long.parseLong(w[j-7],2)&mask)+(Long.parseLong(Sigma0(w[j-15]),2)&mask)+(Long.parseLong(w[j-16],2)&mask))%Math.pow(2,32)))))).replace(" ", "0");
			  }
		  
		  a=HashSeq[0][0];
		  b=HashSeq[0][1];
		  c=HashSeq[0][2];
		  d=HashSeq[0][3];
		  e=HashSeq[0][4];
		  f=HashSeq[0][5];
		  g=HashSeq[0][6];
		  h=HashSeq[0][7];
		  for (int t=0;t<64;t++) {
			  //t1=h+SIGMA1(e)+Choose(e,f,g)+shaconstSeq[t]+w[t];
			  
			 // System.out.println(Long.toHexString(Long.parseLong(a,2)));
			  t1=(long) ((((Long.parseLong(h,2)&mask)+(Long.parseLong(SIGMA1(e),2)&mask)+(Long.parseLong(Choose(e,f,g),2)&mask)+(Long.parseLong(shaconst[t],2)&mask)+(Long.parseLong(w[t],2)&mask)))%Math.pow(2, 32));
			  //t2=SIGMA0(a)+Maj(a,b,c)
			  t2=(long) (((Long.parseLong(SIGMA0(a),2)&mask)+(Long.parseLong(Majority(a,b,c),2)) )%Math.pow(2,32));
			  h=g;
			  g=f;
			  f=e;
			  //e=d+t1
			  e=String.format("%32s",  Long.toBinaryString((long) ((((Long.parseLong(d, 2)&mask)+t1))%Math.pow(2, 32)))).replace(" ", "0");
			  d=c;
			  c=b;
			  b=a;
			  //a=t1+t2
			  a= String.format("%32s", Long.toBinaryString((long) (((t1+t2))%Math.pow(2, 32)))).replace(" ", "0");
		  }
		  /*After intermediate computation of sha variables add variables to corresponding i-1th hash and set it as ith hash*/
		HashSeq[0][0]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(a,2)+Long.parseLong(HashSeq[0][0],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][1]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(b,2)+Long.parseLong(HashSeq[0][1],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][2]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(c,2)+Long.parseLong(HashSeq[0][2],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][3]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(d,2)+Long.parseLong(HashSeq[0][3],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][4]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(e,2)+Long.parseLong(HashSeq[0][4],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][5]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(f,2)+Long.parseLong(HashSeq[0][5],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][6]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(g,2)+Long.parseLong(HashSeq[0][6],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[0][7]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(h,2)+Long.parseLong(HashSeq[0][7],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		/*Concatenate H0|H1|...|H7 and return Hash*/
		  for (int n=1;n<n_round;n++) {
			  
			  idx=0;
		  for (String i:Block[n]){w[idx++]=i;}
		  /*then for 16<=w<=64*/
		  //W(t)=Sigma1W(t-2)+W(t-7)+Sigma0W(t-15)+W(t-16)
		  for (int j=idx;j<64;j++) {
			 // w[j]=Sigma1(w[j-2])+w[j-7]+Sigma0(w[j-15])+w[j-16];
			  w[j]=String.format("%32s",(Long.toBinaryString( ((long)(((Long.parseLong(Sigma1(w[j-2]),2)&mask)+(Long.parseLong(w[j-7],2)&mask)+(Long.parseLong(Sigma0(w[j-15]),2)&mask)+(Long.parseLong(w[j-16],2)&mask))%Math.pow(2,32)))))).replace(" ", "0");
		  }
		  
		  // Since our block is  1 we will take i=0
		  
		  // For ith iteration we take i-1 th hash  
		  /*In SHA 1 we manipulate only a,b,c,d,e,f,g,h to generate hash*/
		  
		  a=HashSeq[n-1][0];
		  b=HashSeq[n-1][1];
		  c=HashSeq[n-1][2];
		  d=HashSeq[n-1][3];
		  e=HashSeq[n-1][4];
		  f=HashSeq[n-1][5];
		  g=HashSeq[n-1][6];
		  h=HashSeq[n-1][7];
		  /*This loop compute intermediate hashes for ith block*/
		  
		  
		  for (int t=0;t<64;t++) {
			  //t1=h+SIGMA1(e)+Choose(e,f,g)+shaconstSeq[t]+w[t];
			  
			 // System.out.println(Long.toHexString(Long.parseLong(a,2)));
			  t1=(long) ((((Long.parseLong(h,2)&mask)+(Long.parseLong(SIGMA1(e),2)&mask)+(Long.parseLong(Choose(e,f,g),2)&mask)+(Long.parseLong(shaconst[t],2)&mask)+(Long.parseLong(w[t],2)&mask)))%Math.pow(2, 32));
			  //t2=SIGMA0(a)+Maj(a,b,c)
			  t2=(long) (((Long.parseLong(SIGMA0(a),2)&mask)+(Long.parseLong(Majority(a,b,c),2)) )%Math.pow(2,32));
			  h=g;
			  g=f;
			  f=e;
			  //e=d+t1
			  e=String.format("%32s",  Long.toBinaryString((long) ((((Long.parseLong(d, 2)&mask)+t1))%Math.pow(2, 32)))).replace(" ", "0");
			  d=c;
			  c=b;
			  b=a;
			  //a=t1+t2
			  a= String.format("%32s", Long.toBinaryString((long) (((t1+t2))%Math.pow(2, 32)))).replace(" ", "0");
		  }
		  /*After intermediate computation of sha variables add variables to corresponding i-1th hash and set it as ith hash*/
		HashSeq[n][0]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(a,2)+Long.parseLong(HashSeq[n-1][0],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][1]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(b,2)+Long.parseLong(HashSeq[n-1][1],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][2]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(c,2)+Long.parseLong(HashSeq[n-1][2],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][3]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(d,2)+Long.parseLong(HashSeq[n-1][3],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][4]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(e,2)+Long.parseLong(HashSeq[n-1][4],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][5]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(f,2)+Long.parseLong(HashSeq[n-1][5],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][6]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(g,2)+Long.parseLong(HashSeq[n-1][6],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		HashSeq[n][7]=String.format("%32s", Long.toBinaryString((long) (((Long.parseLong(h,2)+Long.parseLong(HashSeq[n-1][7],2))&mask)%Math.pow(2, 32)))).replace(" ", "0") ;
		/*Concatenate H0|H1|...|H7 and return Hash*/
		  }
		String finalHash="";
		for (int i=0;i<8;i++) {
			finalHash+=HashSeq[n_round-1][i];
		}
		return finalHash;
	  }
	   
	    	 
}
class SHA{
	String m;
	SHA(String message){
		this.m=message;
		
	}
	
	public String shaRoutine() {
		PreProcess x=new PreProcess(m);
		String binseq=x.convertTextToBinary(x.message);
		String[][]s=x.Block( x.padZeroBit(binseq));
		x.getHash();
		String[][] hash=x.sha256hash;
		sha256Schedule w=new sha256Schedule(s);
		w.getshaconst();
		String s1=w.PrepareSchedule(hash,x.n_block);
		return new BigInteger(s1,2).toString(16);
		
	}
}

/*
 * Main class to test sha implementation 
 * */
public class sha256 {
	/*
	 * Main function to test our implementation.
	 * */
	public static void main (String[]args) {
		/*Create new Instance of shaRoutine */
		
		
		String x1="a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?a?";
		System.out.println("SHA-256 Hash: "+new SHA().shaRoutine("ajay"));

		
	}
}
