package testthreads;
import java.math.*;
import javax.crypto.Mac;
import java.lang.*;
import java.util.Base64;
/*
 * Implementation of the paper   A Fast Recovery of Encrypted Message Database of WeChat On GPU
 * 								-Fei Yu*, Yu Shi, Hao Yin

 * Very Old Unused Code Just wanted to add the same to the collection ....
 * 
 * 
 * @date : 13/04/23
 * 
 * 
 * 
 * */
import javax.crypto.spec.SecretKeySpec;

import at.favre.lib.bytes.Bytes;


/*
 * Certain Specifications in the paper and abberevations used in the code for better clarity
 * klen -: Key Length
 * hlen -: HMAC - ->output length
 * 
 * CEIL () -: Ceiling Function 
 * LEFT (l,d) -: Truncation Function which truncate l bytes of d from left.
 *  
 * */
 /*
  * Afore mentioned Parameters in the paper:
  * 
  * Hash --SHA1
  * klen -- 32
  * hlen -- 20
  * iteration -- 4000
  * 
  * */

class HMAC implements Crypto{
	private String salt,passwd;
	private Integer iteration,klen,hlen;
	HMAC (String salt,String passwd,Integer iteration,Integer klen,Integer hlen){
		this.salt=salt;
		this.passwd=passwd;
		this.iteration=iteration;
		this.klen=klen;
		this.hlen=hlen;
	}
	public Double ComputeCeil() {
		double r=Math.ceil((double)klen/hlen);
		System.out.println(r);
		return r;
	}
	//public byte[] Xor(byte[]arr1,byte[]arr2) {
		
	//}
	public byte[] Hmac(byte[]secretkey,byte[]message) {
		byte hmac[]=null;
		try {
			SecretKeySpec spec=new SecretKeySpec(secretkey,"Hmacsha1");
			
		Mac x=Mac.getInstance("Hmacsha1");
		x.init(spec);
		hmac=x.doFinal(message);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return hmac;
	}
	public void computeIteration()
	{
		
		byte []t=null;
		byte[] u=null;
		byte []out=null;
		Bytes T=Bytes.allocate(klen);
		Bytes U=Bytes.allocate(klen);
		Bytes Out=Bytes.allocate(klen);
		byte[] key=salt.getBytes();
		byte [] msg=passwd.getBytes();
		double r=ComputeCeil();
		//String c=Base64.getEncoder().encodeToString(Hmac("123".getBytes(),"helo".getBytes()));
		//System.out.println(String.format("Hex: %064x", new BigInteger(1, Hmac(key,msg))));
		for(int i=1;i<(int)r;i++) {
			
			
			t=Hmac(key,msg);
			T=Bytes.from(t);
			u=t;
			U=T;
			
			for (int j=1;j<iteration;j++) {
				t=Hmac(msg,t);
				T=Bytes.from(t);
				U=U.xor(T);
				//System.out.println(Base64.getEncoder().encodeToString(U.array()));
				
			}
			//System.out.println(U+"d");
			if (i==1) {
				Out=U;
				continue;
			}
			Out=Out.append(U);
			
		}
		System.out.println(Base64.getEncoder().encodeToString(Out.array()));
	}
	@Override
	public void get(String key, String salt) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void set() {
		// TODO Auto-generated method stub
		
	}
	
}
public class PBKDF2 {

	public static void main(String[]args) {
		System.out.println("Mvn build success");
		System.out.println("Key: Hello World This is a test for PBKDF2 Key Derivation Function\nSalt : 9019248\nn_iter: 4000\nklen :32\nhlen : 20\n");
		HMAC s=new HMAC("9019248","Hello World This is a test for PBKDF2 Key Derivation Function!",600000,32,20);
		//Double r=s.ComputeCeil();
		s.computeIteration();
		//double val=(double)20/32;
		//System.out.println(Math.ceil(val));
	}
}
