package ecc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;

import ecc.EllipticCurve;
public class dhkecCient {
	public static void main (String []args) {
		Socket s1;
		DataInputStream r1,r2;
		PrintStream p1;
		int op;
		ECPoint generator;
		ECPoint generator1;
		ECField field;
		EllipticCurve curve;
		try {
			s1=new Socket(InetAddress.getLocalHost(),8080);
			r1=new DataInputStream(s1.getInputStream());
			byte[ ]tmp=new byte[256];
			int i=2;
			//r2=new BufferedReader(new InputStreamReader(s1.getInputStream()));
			//p1=new PrintStream(s1.getOutputStream());
			while(i!=0) {
				//("Client :");
				System.out.print(r1.readUTF());
				generator=new ECPoint(BigInteger.valueOf(r1.readInt()),BigInteger.valueOf(r1.readInt()));
				//System.out.println(new String(tmp));
				op=r1.readInt();
				i--;
				
				//System.out.println("Server: "+op);
				
			}
		}  catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
