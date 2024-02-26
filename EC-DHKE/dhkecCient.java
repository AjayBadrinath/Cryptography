package ecc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import ecc.EllipticCurve;
public class dhkecCient {
	
	public static void main (String []args) {
		Socket s1;
		DataInputStream r1,r2;
		PrintStream p1;
		int op;
		ECPoint curveParam;
		ECPoint generator1;
		ECField field;
		EllipticCurve curve;
		
		try {
			s1=new Socket(InetAddress.getLocalHost(),8080);
			r1=new DataInputStream(s1.getInputStream());
			
			int i=1;
			InputStream is=s1.getInputStream();
			
			while(i!=0) {
				ObjectInputStream p=new ObjectInputStream(is);
				Object tmp=p.readObject();
				List<?> header=null;
				header=(List<?>) tmp;
				System.out.println(header.get(0));
				
				i--;
				
				
			}
		}  catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
