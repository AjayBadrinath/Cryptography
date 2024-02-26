package ecc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
 class ModifiedTCPHeaderAuth{
	ECPoint curveParam,generator,PublicKey;
	ECField field;
	
	ModifiedTCPHeaderAuth(ECPoint curveParam,ECPoint generator,ECField field,ECPoint PublicKey){
		this.curveParam=curveParam;
		this.field=field;
		this.generator=generator;
		this.PublicKey=PublicKey;
	}
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
public class dhkeServer {
	
	public static void main(String[]args) {
		ECPoint point;
		ECPoint generator1=new ECPoint(BigInteger.valueOf(5),BigInteger.valueOf(1));
		ECField field=new ECField(BigInteger.valueOf(17));
		EllipticCurve curve=new EllipticCurve(BigInteger.valueOf(2),BigInteger.valueOf(2),field);
		int op1;
		Socket s1;
		ServerSocket serv;
		DataOutputStream r2;
		OutputStream r1;
		PrintStream p1;
		try {
			serv=new ServerSocket(8080);
			
			s1=serv.accept();
			r2=new DataOutputStream(s1.getOutputStream());
			r1=s1.getOutputStream();
			int i=5;
			OutputStream os = s1.getOutputStream();
			List<Object> objects = new ArrayList<>();
			ECPoint curveParam=new ECPoint(BigInteger.valueOf(2),BigInteger.valueOf(2));
			//List <ObjectOutputStream>s;
			//Random s=new Random();
			//s.nextInt(10);
			//r2.writeUTF(null);
			//p1=new PrintStream(s1.getOutputStream());
			/*
			objects.add("Chosen Curve Parameter a-:"+2+"b-:"+2+"\n");
			ECPoint curveParam=new ECPoint(BigInteger.valueOf(2),BigInteger.valueOf(2));
			objects.add(curveParam);
			objects.add("Chosen Curve Generator Point:("+5+","+1+")\n");
			objects.add(generator1);
			objects.add("Chosen Field:Z:"+17+"\n");
			objects.add(field);
			ECPoint p=curve.ScalarMul(generator1, BigInteger.valueOf(17));
			objects.add(p);
			*/
			ECPoint p=curve.ScalarMul(generator1, BigInteger.valueOf(17));
			ModifiedTCPHeaderAuth auth=new ModifiedTCPHeaderAuth(curveParam,generator1,field,p);
			
			while(true) {    
				List <Object> o=auth.getHeader();
				ObjectOutputStream oos1 = new ObjectOutputStream(os);
				oos1.writeObject(o);
				
				//r2.writeBytes();
				/*
				r2.writeUTF("Chosen Curve Parameter a-:"+2+"b-:"+2+"\n");
				
				
				ECPoint curveParam=new ECPoint(BigInteger.valueOf(2),BigInteger.valueOf(2));
				
				oos1.writeObject(curveParam);
				objects.add(curveParam);
				
				
				r2.writeUTF("Chosen Curve Generator Point:("+5+","+1+")\n");
				oos1.writeObject(generator1);
				
				
				r2.writeUTF("Chosen Field:Z:"+17+"\n");
				
				oos1.writeObject(field);
				
				System.out.println("Chosen Private key:"+17);
				///
				
				ECPoint p=curve.ScalarMul(generator1, BigInteger.valueOf(17));
				
				
				oos1.writeObject(p);
				
				i--;
				
				*/
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
