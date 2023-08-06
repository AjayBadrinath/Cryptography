package testss;

import java.math.BigInteger;

public class DESImpl {
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
		/*Example from https://simewu.com/des/*/
		/*
		 * Will soon document the code well and apply it to plain text 
		 * this is just an Example
		 * */
		String cipher=Encrypt(s1,ToBeEncrypted);
		String decrypted=Decrypt(s1,cipher);
		System.out.println("Message:"+ToBeEncrypted);
		System.out.println("Cipher:\t"+cipher);
		System.out.println("Plain Text /Original Message:\t"+decrypted);
		//DESDecrypt(s1,"1011101010001000111101101001100101110011001101001010010000011111","000010100000001000000111100110110100000010100001");
		//Decrypt(s1,"0101011011001100000010011110011111001111110111000100110011101111");
		
		//System.out.println(Decrypt(s1,Encrypt(s1,("0000000100100011010001010110011110001001101010111100110111101111"))));
		//System.out.println(s1.InitialPermutation("0101011011001100000010011110011111001111110111000100110011101111"));
		//Encrypt(s1,("0000000100100011010001010110011110001001101010111100110111101111"));
	}
}
