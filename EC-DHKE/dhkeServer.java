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
import java.util.Random;

public class dhkeServer {
	
	public static void main(String[]args) {
		ECPoint generator=new ECPoint(BigInteger.valueOf(0),BigInteger.valueOf(11));
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
			
			//Random s=new Random();
			//s.nextInt(10);
			//r2.writeUTF(null);
			//p1=new PrintStream(s1.getOutputStream());
			while(true) {
				//r2.writeBytes();
				r2.writeUTF("Chosen Curve Parameter a-:"+2+"b-:"+2);
				r2.writeInt(2);
				r2.writeInt(2);
				r2.writeUTF("Chosen Curve Generator Point:("+5+","+1+")");
				r2.writeInt(5);
				r2.writeInt(1);
				r2.writeUTF("Chosen Field:Z:"+17);
				r2.writeInt(17);
				System.out.println("Chosen Private key:"+17);
				ECPoint p=curve.ScalarMul(generator1, BigInteger.valueOf(17));
				OutputStream os = s1.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(p);
				ObjectInputStream is=new ObjectInputStream(s1.getInputStream());
				ECPoint p2=(ECPoint) is.readObject();
				
				
				
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
