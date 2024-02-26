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
 * 									1. Yet to add comments 
 * 									2. Fixed Byte Sequence Error
 * 									3. Authentication and Establishment of shared Key Done !
 * 
 * */
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ModifiedTCPHeaderAuthClient{
	ECPoint PublicKey;
	ModifiedTCPHeaderAuthClient(ECPoint PublicKey){
		this.PublicKey=PublicKey;
		
	}
	List<Object> getHeader(){
		List<Object> objects = new ArrayList<>();
		objects.add(PublicKey);
		return objects;
	}
	
}
public class dhkecCient {
	
	public static void main (String []args) {
		Socket s1;
		ECPoint curveParam;
		ECPoint generator;
		ECField field;
		EllipticCurve curve;
		
		try {
			s1=new Socket(InetAddress.getLocalHost(),8080);
			OutputStream os = s1.getOutputStream();
			int i=1;
			InputStream is=s1.getInputStream();
			
			while(i!=0) {
				ObjectInputStream p=new ObjectInputStream(is);
				Object tmp=p.readObject();
				List<?> header=null;
				header=(List<?>) tmp;
				
				curveParam=(ECPoint )header.get(1);
				field=(ECField )header.get(5);
				generator=(ECPoint )header.get(3);
				curve=new EllipticCurve(curveParam.x,curveParam.y,field);
				
				BigInteger privKey= BigInteger.valueOf(11);
				ECPoint p11=curve.ScalarMul(generator,privKey);
				
				ModifiedTCPHeaderAuthClient reply=new ModifiedTCPHeaderAuthClient(p11);
				System.out.println(p11.x+""+p11.y);
				ObjectOutputStream oos1 = new ObjectOutputStream(os);
				oos1.writeObject(reply.getHeader());
				ECPoint A_Pub=(ECPoint) header.get(6);
				ECPoint shared_key=curve.ScalarMul(A_Pub,privKey);
				System.out.println("Shared Key:"+shared_key.x+" "+shared_key.y);
				
				i--;
				
				
			}
		}  catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
