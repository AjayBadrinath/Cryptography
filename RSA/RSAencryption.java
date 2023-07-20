package pasman;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSAencryption {
	/*
	 * Stop!!
	 * The below rsa(); function is only intended for one time use ..DO NOT RUN AGAIN as all the public and private key will be overridden!
	 * Use with caution!
	 * */
	public static void rsa() {
		try {
		KeyPairGenerator key=KeyPairGenerator.getInstance("RSA");
		SecureRandom r=new SecureRandom();
		key.initialize(2048, r);
		KeyPair pair=key.generateKeyPair();
		PublicKey pub=pair.getPublic();
		PrivateKey pri=pair.getPrivate();
		//String Pubs,pris;1
		
		//Pubs=Base64.getEncoder().encodeToString(pub.getEncoded());
		//pris=Base64.getEncoder().encodeToString(pri.getEncoded());
		FileOutputStream f=new FileOutputStream("pub.key");
		FileOutputStream f1=new FileOutputStream("priv.key");
		f.write(pub.getEncoded());
		f1.write(pri.getEncoded());
		f.close();
		f1.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/*Encryption Functions based off private key YES IT CAN BE DONE!!!! BUT INSECURE THOUGH!*/
	public static byte[] encrypt(String val)  {
		try {
		Cipher encrypt=Cipher.getInstance("RSA");
		FileInputStream f=new FileInputStream("priv.key");
		byte[]priv=new byte[256];
		priv=f.readAllBytes();
		f.close();
		KeyFactory kf=KeyFactory.getInstance("RSA");
		EncodedKeySpec privk=new PKCS8EncodedKeySpec(priv);
		PrivateKey pk=kf.generatePrivate(privk);
		//PrivateKey pk=(PrivateKey) privk;
		encrypt.init(Cipher.ENCRYPT_MODE, pk);
		byte[]eval=new byte[256];
		eval=encrypt.doFinal(val.getBytes());
		return eval;
		//return Base64.getEncoder().encodeToString(eval);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/*Decrypt using Public Key  same goes here check : https://crypto.stackexchange.com/questions/2123/rsa-encryption-with-private-key-and-decryption-with-a-public-key*/
	public static String decrypt(String Encryptedval) {
		try {
		Cipher c=Cipher.getInstance("RSA");
		byte []encmsg=new byte[256];
		 encmsg=Base64.getDecoder().decode(Encryptedval);
		FileInputStream f1=new FileInputStream("pub.key");
		byte[]pubkey=new byte[256];
		pubkey=f1.readAllBytes();
		f1.close();
		KeyFactory kf=KeyFactory.getInstance("RSA");
		EncodedKeySpec puk=new X509EncodedKeySpec(pubkey);
		PublicKey pk=kf.generatePublic(puk);
		c.init(Cipher.DECRYPT_MODE, pk);
		byte[] decr=new byte[256];
	    decr=c.doFinal(encmsg);
		return new String(decr);
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
