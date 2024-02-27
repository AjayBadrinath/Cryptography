package ecc;
/*
 * 											Elliptic Curve Diffie Hellman Key Exchange Protocol Client
 * 
 * @author : AjayBadrinath
 * @date   : 26-02-2024
 * 
 * Version : 1.0.2
 * 
 * 					Version Changelog:
 * 									 
 * 									1. Fixed Byte Sequence Error
 * 									2. Authentication and Establishment of shared Key Done !
 * 
 * */
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * Class To Provide TCP Header For Key Exchange . Comment Out String if not needed.....
 * Header Format(Not Including String):
 * 			1.curveParameter
 * 			2.Primitive Point Generator
 * 			3.Field
 * 			4.Computed Public Key aP=Pub_key_a
 * DO NOT PARSE IN WRONG ORDER 
 */
 class ModifiedTCPHeaderAuth {
	ECPoint curveParam,generator,PublicKey;
	ECField field;
	
	ModifiedTCPHeaderAuth(ECPoint curveParam,ECPoint generator,ECField field,ECPoint PublicKey){
		this.curveParam=curveParam;
		this.field=field;
		this.generator=generator;
		this.PublicKey=PublicKey;
	}
	/**
	 * Function to set header parameters to send via tcpstream.
	 * @return List <Object>
	 */
	List<Object> getHeader(){
		List<Object> objects = new ArrayList<>();
		objects.add("Chosen Curve Parameter a-:"+2+"b-:"+2+"\n");
		objects.add(curveParam);
		objects.add("Chosen Curve Generator Point:("+5+","+1+")\n");
		objects.add(generator);
		objects.add("Chosen Field:Z:"+17+"\n");
		objects.add(field);
		objects.add(PublicKey);
		return objects;
	}
	
}
/**
 * Main Class Server.
 */
public class dhkeServer {
	/**
	 * Entry Point for the Server.
	 * @param None
	 */
	public static void main(String[]args) {
		/**
		 * Can Choose any Curve Parameter .. Can Be Random too.
		 * 
		 * 
		 */
		ECPoint point;
		ECPoint generator1=new ECPoint(BigInteger.valueOf(5),BigInteger.valueOf(1));
		ECField field=new ECField(BigInteger.valueOf(17));
		EllipticCurve curve=new EllipticCurve(BigInteger.valueOf(2),BigInteger.valueOf(2),field);
		Socket s1;
		ServerSocket serv;

		/**
		 * Open Server Connection .
		 */
		try {
			serv=new ServerSocket(8080);
			s1=serv.accept();
			int i=1;
			/**
			 * 
			 * Opening Output Stream / InputStream for Key Exchange.
			 * 
			 */
			OutputStream os = s1.getOutputStream();
			InputStream is=s1.getInputStream();
			ECPoint curveParam=new ECPoint(BigInteger.valueOf(2),BigInteger.valueOf(2));
			BigInteger privKey= BigInteger.valueOf(7);
			ECPoint p=curve.ScalarMul(generator1,privKey);
			
			ModifiedTCPHeaderAuth auth=new ModifiedTCPHeaderAuth(curveParam,generator1,field,p);
			
			while(i!=0) {  
				/**
				 * Generate Header 
				 */
				List <Object> o=auth.getHeader();
				ObjectOutputStream oos1 = new ObjectOutputStream(os);
				/**
				 * 
				 * Send header to tcp socket.
				 * 
				 */
				oos1.writeObject(o);
				/**
				 * 
				 * Recieve Client Public Key bP=Client_Pub.
				 * 
				 */
				ObjectInputStream p3=new ObjectInputStream(is);
				List<?>B=(List<?>) p3.readObject();

				/**
				 * Establish Shared key(Shared Secret) .. Must be the same at this point on the client and server 
				 * Proof Of correctness in readme...
				 * 
				 */
				ECPoint B_Pub=(ECPoint) B.get(0);


				ECPoint shared_key=curve.ScalarMul(B_Pub,privKey);
				System.out.println("Shared Key:"+shared_key.x+" "+shared_key.y);
				
				i--;
				/**
				 * Close Stream after handshake.
				 */
				oos1.close();
				p3.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
