package testss;

import java.math.BigInteger;

public class DESImpl {
	
	static int [][]IP_inv= {
			{0x28,	0x8,	0x30,	0x10,	0x38,	0x18,	0x40,	0x20,	},
			{0x27,	0x7,	0x2f,	0xf,	0x37,	0x17,	0x3f,	0x1f,	},
			{0x26,	0x6,	0x2e,	0xe,	0x36,	0x16,	0x3e,	0x1e,	},
			{0x25,	0x5,	0x2d,	0xd,	0x35,	0x15,	0x3d,	0x1d,	},
			{0x24,	0x4,	0x2c,	0xc,	0x34,	0x14,	0x3c,	0x1c,	},
			{0x23,	0x3,	0x2b,	0xb,	0x33,	0x13,	0x3b,	0x1b,	},
			{0x22,	0x2,	0x2a,	0xa,	0x32,	0x12,	0x3a,	0x1a,	},
			{0x21,	0x1,	0x29,	0x9,	0x31,	0x11,	0x39,	0x19,	}
	};
	 
	public static String ipinvfn(String binseq) {
		
		
		String op="";
		StringBuilder d=new StringBuilder(binseq);
		System.out.println(d.length());
		for (int i=0;i<8;i++) {
			
			for(int j=0;j<8;j++) {
				
				op+=d.charAt(IP_inv[i][j]-1);
				
			}
			
			
		}
		return new String(op);
	}
	public static String  DESFunction(DES fn,String binseq1,String key) {
		String binseq=binseq1;
		String cipher=(binseq.substring(32,64)+fn.Xor(binseq.substring(0,32),fn.FinalPermutation(fn.MapToSBox(fn.Xor(fn.ExpansionBoxMap(binseq.substring(32,64)), key)))));
		//System.out.println(cipher);
		return cipher;
		
	}
	public static String DESDecrypt(DES fn,String cipher1,String key) {
		String binseq=cipher1.substring(32, 64)+cipher1.substring(0, 32);
		String decrypt=(fn.Xor(binseq.substring(0,32),fn.FinalPermutation(fn.MapToSBox(fn.Xor(fn.ExpansionBoxMap(binseq.substring(32,64)), key))))+binseq.substring(32,64));
		//System.out.println(decrypt);
		return decrypt;
	}
	public static String [] DESRounds(DES fn,String binseq,String []keyseq) {
		String [] RoundsCipher=new String[16];
		for (int  i=1;i<17;i++) {
			RoundsCipher[i-1]=DESFunction(fn,binseq,keyseq[i]);
			binseq=RoundsCipher[i-1];
			
		}
		return RoundsCipher;
	}
	public static String[]  DESDecRound(DES fn, String binseq,String[]keyseq) {
		String [] decCipher=new String[16];
		for(int i=16;i>0;i--) {
			decCipher[16-i]=DESDecrypt(fn,binseq,keyseq[i]);
			binseq=decCipher[16-i];
		}
		for(String i:decCipher) {
			System.out.println(i);
		}
		//System.out.println(decCipher[15]);
		return decCipher;
	}
	public static String Decrypt(DES s1,String Cipher) {
		String s2=s1.InitialPermutation(Cipher);
		KeyGen s=new KeyGen();
		String cipher=s2.substring(32,64)+s2.substring(0,32);
		String []keys=s.PermuteStoreKeys(s.StoreKeys(s.PC1fn("0000000100100011010001010110011110001001101010111100110111101111")));
		String []all_ciphers=DESDecRound(s1,cipher,keys);
		return ipinvfn(all_ciphers[15]);
	}
	public static  String Encrypt(DES s1,String Message) {
		KeyGen s=new KeyGen();
		String []keys=s.PermuteStoreKeys(s.StoreKeys(s.PC1fn("0000000100100011010001010110011110001001101010111100110111101111")));
		String []cipher=DESRounds(s1,s1.InitialPermutation(Message),keys);
		for(String k:cipher) {
			System.out.println(k);
		}
		System.out.println("=======");
		String tmp=ipinvfn(cipher[15].substring(32,64)+cipher[15].substring(0,32));
		BigInteger i=new BigInteger(tmp,2);
		
		System.out.println(i.toString(16));
		return ipinvfn(cipher[15].substring(32,64)+cipher[15].substring(0,32));
		
	}
	
	
	public static void main(String[]args) {
		
		DES s1=new DES();
		
		String ToBeEncrypted="0000000100100011010001010110011110001001101010111100110111101111";
		/*
		String message="Hello World!!";
		String bin=s1.convertTextToBinary(message);
		
		//String bin="0100";
		if(bin.length()<64) {
			bin=s1.PadZeros(bin);
		}
		else {
			int i=0;
			StringBuilder s2=new StringBuilder(bin);
			//System.out.println(bin.length()-bin.length()%64);
			String tmp=bin.substring(bin.length()-bin.length()%64,bin.length());
			System.out.println(tmp);
			s2.delete(bin.length()-bin.length()%64,bin.length());
			
			s2.append(s1.PadZeros(tmp));
			//bin.replace(tmp, s1.PadZeros( bin.substring(bin.length()-bin.length()%64,bin.length())));
			String rep=s2.toString();
			System.out.println(bin.substring(64,104));
			BigInteger x=new BigInteger(rep.substring(64, 128),2);
			System.out.println(s1.PadZerosWithMultiple8(x.toString(2),bin.substring(64,104).length()-x.toString(2).length()));
			
			//for (int l=0;i<rep.length();l+=8) {
			//	int k=Integer.parseInt((rep.substring(l,l+8)),2);
			//	System.out.println((char)k);
			//}
			
		}
		
		*/
		
		/*Example from https://simewu.com/des/*/
		/*
		 * Will soon document the code  and apply it to plain text 
		 * 
		 * this is just an Example
		 * */
		
		String cipher=Encrypt(s1,ToBeEncrypted);
		String decrypted=Decrypt(s1,cipher);
		System.out.println("Message:"+ToBeEncrypted);
		System.out.println("Cipher:\t"+cipher);
		System.out.println("Plain Text [Original Message] :\t"+decrypted);
		
		//System.out.println(Integer.parseInt("00001011",2));
		//DESDecrypt(s1,"1011101010001000111101101001100101110011001101001010010000011111","000010100000001000000111100110110100000010100001");
		//Decrypt(s1,"0101011011001100000010011110011111001111110111000100110011101111");
		
		//System.out.println(Decrypt(s1,Encrypt(s1,("0000000100100011010001010110011110001001101010111100110111101111"))));
		//System.out.println(s1.InitialPermutation("0101011011001100000010011110011111001111110111000100110011101111"));
		//Encrypt(s1,("0000000100100011010001010110011110001001101010111100110111101111"));
	}
	
}
