package testss;
/*
 * @author: AjayBadrinath
 * @date  :01-10-2023
 */
/*
 * 
 * Currently the code is under maintenance I am yet to comment it ....
 * Plus One Way hashing works but not consistent with existing implementations of md5 but works though ... bet it is on the endianness of the bits.
 * Need to fix the Hash for consistency with contemporary md5 implementation.
 * Note to myself:
 * 1.Comments will have to be added
 * 2.Fix the hash for consistency.
 */
class Preprocess1{
	String message;
	   private  int[][] shift;
	   private long [] word_buf;
	   private long [][]kTable;
	static   private  int [][]md_buffer;
	AuxillaryFunction f;
	Preprocess1(String message){
		this.message=message;
		KTable();
		MdBuffer();
		InitializeWordBuffer();
		ShiftAmt();
		f=new AuxillaryFunction();
		
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
      * Function to Pad Zeros
      * @param - String which is a binary stream 
      * @returns - binary string of length 512 bits
      * 
      * */
     String padZeroBit(String seq) {
    	 int len=(int) (seq.length()%Math.pow(2, 64));
    	 //k+l+1=448%512;
    	 // Rearranging k=(448%512)-l-1;
    	 seq+="1";
    	 int k=(448%512)-len-1;
    	 System.out.println(k);
    	 for (int i=0;i<k;i++) {seq+="0";}
    	 seq+=String.format("%64s",Integer.toBinaryString(len)).replace(" ", "0");
    	 return seq;
     }
     
     /*
      * Helper function to Split 512 bitstream into blocks of each 32 bit length (4 bytes)
      * @param - Padded String of length 512
      * @return - Array of Strings of 32 bit blocks each
      */
      
     String[] Block(String input) {
    	 //512/16=32bit word in each block
    	 
    	 String [] blocks=new String [input.length()/32];
    	 for (int i=0;i<input.length()/32;i++) {
    		 blocks[i]=input.substring(i*32,i*32+32);
    	 }
    	 return blocks;
     }
     
       static private void MdBuffer() {//2**32
    	 int [][] buffer= {
    			 {0x01,0x23,0x45,0x67},
    			 {0x89,0xab,0x4,0x67},
    			 {0xfe,0xdc,0xba,0x98},
    			 {0x76,0x54,0x32,0x10}
    	 };
    	 setMd_buffer(buffer);
    	
     }
       long [] FlattenArray(long[][]arr) {
    	   int len=arr[0].length*arr.length;
    	   long mask=(1L<<32)-1;
    	   System.out.println(arr[0].length);
    	   long [] arr1=new long[len];
    	   int c=0;
    	   for (int i=0;i<arr.length;i++) {
    		   for (int j=0;j<arr[0].length;j++) {
    			   //System.out.println(i+"\t"+j);
    			   arr1[c++]=arr[i][j]&mask;
    		   }
    	   }
    	   return arr1;
    	   
       }
       int [] FlattenArray1(int[][]arr) {
    	   int len=arr[0].length*arr.length;
    	   System.out.println(arr[0].length);
    	   int [] arr1=new int[len];
    	   int c=0;
    	   for (int i=0;i<arr.length;i++) {
    		   for (int j=0;j<arr[0].length;j++) {
    			   //System.out.println(i+"\t"+j);
    			   arr1[c++]=arr[i][j];
    		   }
    	   }
    	   return arr1;
       }
        private  void KTable() {
    	  long [][]k=new long[16][4];
    	  long mask=(1L<<32)-1;
    	  for (int i=0;i<16;i++) {
    		  for (int j=0;j<4;j++) {
    			  k[i][j]=(long)((Math.abs(Math.sin((double)(i*4+j+1))))*Math.pow(2, 32))&mask;
    		  }
    	  }
    	  setkTable(k);
    	  
       }
    	/* 
    	  for(long []i:k) {
    		  for (long j:i) {
    		  System.out.print(Long.toHexString(j)+"\t");
    		  
    		  }
    		  System.out.println();
    	  }
    	  
    	  //System.out.println(Long.toHexString(k[0][0]));
      }
   */
    	  /* Obtained from :https://cs.indstate.edu/~fsagar/doc/paper.pdf
    	   * s[ 0..15] = {7,12,17,22,7,12,17,22,7,12,17,22,7,12,17,22}
			s[16..31] = {5,9,14,20,5,9,14,20,5,9,14,20,5,9,14,20}
			s[32..47] = {4,11,16,23,4,11,16,23,4,11,16,23,4,11,16,23 }
			s[48..63] = {6,10,15,21,6,10,15,21,6,10,15,21,6,10,15,21 }
    	   * */
    	   private  void ShiftAmt() {
    		 int shift[][] ={
    				 {7,12,17,22,7,12,17,22,7,12,17,22,7,12,17,22},
    				 {5,9,14,20,5,9,14,20,5,9,14,20,5,9,14,20},
    				 {4,11,16,23,4,11,16,23,4,11,16,23,4,11,16,23 },
    				 {6,10,15,21,6,10,15,21,6,10,15,21,6,10,15,21 }
    		 
    		 };
    		 
    		 setShift(shift);
    		 
    		 
    	 }
    	  
    	  
    	 /*
    	  * These values initialised are the pre-computed buffers in md5 standard
    	  * change  access modifers later
    	  * */ 
    	   long rol(long x,int dist) {
  			long mask=(1L<<32)-1;
  			return (x<<dist| x>>(32-dist)) &mask;
  		}
    	  String PerformSchedule(String [] block) {
    		  long mask=(1L<<32)-1;
    		  long a=getWord_buf(0)&mask,b=getWord_buf(1)&mask,c=getWord_buf(2)&mask,d=getWord_buf(3)&mask;
    		  long []ktable=FlattenArray(getkTable());
    		  int[] shift=FlattenArray1(getShift());
    		 
    		  
    		  int idx=0,g;
    		  long F,tmp;
    		  for (int i=0;i<64;i++) {
    			  
    			 if(i>=0 &&i<=15) {
    				F= f.FFunction(b,c,d)&mask;
    				//System.out.println(Long.toHexString(F)+"====[][][]");
    				idx=0;
    				g=i;
    				
    			 }else if(i>=16&&i<=31) {
    				 F=f.GFunction(b, c, d)&mask;
    				 idx=1;
    				 g=(5*i+1)%16;
    				 
    			 }else if(i>=32&&i<=47) {
    				 F=f.HFunction(b, c, d)&mask;
    				 idx=2;
    				 g=(3*i+5)%16;
    			 }else {
    				 F=f.IFunction(b, c, d)&mask;
    				 idx=3;
    				 g=(7*i)%16;
    				 
    			 }
    			//F=(F+a+ktable[i]+Long.parseLong(block[g],2)&mask)&mask;
    			//F = (F + a + ktable[i] + Long.parseLong(block[g],2)) & mask;
    			// tmp=(d);d=(c);c=(b);b=(b+rol(F, (int)shift[i]));a=tmp;
    			 //
    			 // tmp=d;d=c;;c=b;b=rol((a+F+getkTable(i%16,i%4)+Long.parseLong(block[g],2)),getShift(idx,i%16));// change k , m later almost done.,,....getShift(idx*16,i%16)s[idx*16][i%16]
    			 tmp=d&mask;d=c&mask;c=b&mask;b+=rol((a&mask+F&mask+ktable[i]&mask+Long.parseLong(block[g],2)&mask),(int)shift[i])&mask;
    			 a=tmp&mask;
    			 
    		  }
    		 //System.out.println(Long.toHexString(getWord_buf(1)));
    		  
    		  long []buf={(getWord_buf(0)+a),(getWord_buf(1)+b),(getWord_buf(2)+c),(getWord_buf(3)+d)};
    		  
    		  //System.out.println(getWord_buf(0));
    		  setWord_buf(buf);
    		  for(long i:word_buf) {
    			  System.out.println(Long.toHexString(i));
    		  }
    		  //System.out.println(Long.toHexString(getWord_buf(1)));
    		  String h1=String.format("%8s",Long.toHexString(getWord_buf(0))).replace(" ", "0");
    		  String h2=String.format("%8s",Long.toHexString(getWord_buf(1))).replace(" ", "0");
    		  String h3=String.format("%8s",Long.toHexString(getWord_buf(2))).replace(" ", "0");
    		  String h4=String.format("%8s",Long.toHexString(getWord_buf(3))).replace(" ", "0");
    		 String hash=h1+h2+h3+h4;
    		  /*
    		  String hash=String.format("%32s", Long.toHexString(getWord_buf(0))
    				  +Long.toHexString(getWord_buf(1))
    				  +Long.toHexString(getWord_buf(2))
    				  +Long.toHexString(getWord_buf(3))).replace(" ", "0");
    				  */
    		  return hash;
    		  
    	  }
    	   void InitializeWordBuffer(){
    		  long mask=(1L<<32)-1;
    		  /*
    		   * {0x01,0x23,0x45,0x67},
    			 {0x89,0xab,0x4,0x67},
    			 {0xfe,0xdc,0xba,0x98},
    			 {0x76,0x54,0x32,0x10}
    		   * */
    		  long [] word_buf= {0x67452301&mask,0xefcdab89&mask,0x98badcfe&mask,0x10325476&mask};
    		  //long[] word_buf= {0x01234567&mask,0x89ab0467&mask,0xfedcba98&mask,0x76543210&mask};
    		  setWord_buf(word_buf);
    	  }

		public static int [][] getMd_buffer() {
			return md_buffer;
		}

		public static void setMd_buffer(int [][] md_buffer) {
			Preprocess1.md_buffer = md_buffer;
		}

		public   long[][]  getkTable() {
			
			return kTable;
		}

		public  void setkTable(long [][] kTable) {
			this.kTable = kTable;
		}

		public  long  getWord_buf(int i) {
			long mask=(1L<<32)-1;
			return word_buf[i]&mask;
		}

		public  void setWord_buf(long [] word_buf) {
			this.word_buf = word_buf;
		}

		public  int[][] getShift() {
			return shift;
		}

		public  void setShift(int[][] shift2) {
			shift = shift2;
		}
    	  
    	 
     
}
class AuxillaryFunction{
	long mask=(1L<<32)-1;
	long FFunction(long b,long c,long d) {
		return ((b&c)|((~b)&d));
	}
	long GFunction(long b,long c,long d) {
		return ((b&d)|(c&(~d)));
	}
	long HFunction(long b,long c,long d) {
		return (b^c^d);
	}
	long IFunction(long b,long c,long d) {
		return (c^(b|(~d)));
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
public class md5 {
	public static void main(String[]args) {
	Preprocess1 s1=new Preprocess1("");
	String[]s=s1.Block(s1.padZeroBit(s1.convertTextToBinary(s1.message) ));
	
	String h=s1.PerformSchedule(s);
	System.out.println(h);
	//System.out.println(Long.toHexString(s1.getWord_buf(0)));
//	for (String i:s){
		//System.out.println(Long.parseLong(i,2));
	//}
	/*
	for(long []i:s1.getkTable()) {
		  for (long j:i) {
		  System.out.print(Long.toHexString(j)+"\t");
		  
		  }
		  System.out.println();
	  }
	  	*/
	}

	
	
}
