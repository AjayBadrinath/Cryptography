package testss;
import java.math.BigInteger;

import testss.sha256;
/*
 * 																			Implementation Of HMAC-SHA256
 *@author: AjayBadrinath
 *@Date: 18-11-2023
 *
 *Comments Will Be Updated 
 *
 *Currently Untested code..
 * */
 class HMACsha256 {
	private String Key,message;
	final int blockSize=64;
	Long mask=((1L<<32)-1);
	HMACsha256(String key,String message){
		this.Key=key;
		this.message=message;
	}
	
	  String padZeroBitHmac(String seq) {
   	 
   	PreProcess x=new PreProcess(seq);
   	String k1=x.convertTextToBinary(seq);
   	int len=k1.length();
   	 k1+="0";
   	 int k=(512)-len;
   	 System.out.println(k);
   	 for (int i=0;i<k;i++) {k1+="0";}
   	 return k1;
    }
	 String HMAC() {
		 PreProcess S=new PreProcess(Key);
		 
		if (S.convertTextToBinary(Key).length()>blockSize*8) {
			Key= new SHA(Key).shaRoutine();
		}
		else {
			Key=padZeroBitHmac(Key);
			System.out.println(Key);
			S.n_block=1;
			String[][]s=S.Block(Key);
			S.getHash();
			
			String[][] hash=S.sha256hash;
			sha256Schedule w=new sha256Schedule(s);
			w.getshaconst();
			String s1=w.PrepareSchedule(hash,S.n_block);
			Key= new BigInteger(s1,2).toString(16);
			System.out.println(Key);
		}
		
		String OKeyPad="";
		String IKeyPad="";
		
		
		for(int i=0;i<blockSize;i+=8) {
			OKeyPad+=String.format("%8s",Long.toHexString(Long.parseLong(Key.substring(i, i+8),16)^0x5c)).replace(" ","0");
			IKeyPad+=String.format("%8s",Long.toHexString(Long.parseLong(Key.substring(i, i+8),16)^0x36)).replace(" ","0");
		}
		String IKeyPadBin=String.format("%256s", new BigInteger(IKeyPad,16).toString(2)).replace(" ", "0");
		String OKeyPadBin=String.format("%256s", new BigInteger(OKeyPad,16).toString(2)).replace(" ", "0");
		
		return new SHA(OKeyPadBin+(new SHA(IKeyPadBin+S.convertTextToBinary(message)).shaRoutine())).shaRoutine();
		
	}
	
}
 
public class HMAC{
	public static void main(String[]args) {
		HMACsha256 n=new HMACsha256("NNSXS===","KRUGKIDROVUWG2ZAMJZG653OEBTG66BANJ2W24DTEBXXMZLSEB2GQZJANRQXU6JAMRXWO===");
		System.out.println(n.HMAC());
		
	}
}