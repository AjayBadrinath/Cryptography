package ecc;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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
		Socket s1;
		ServerSocket serv;
		try {
			serv=new ServerSocket(8080);
			
			s1=serv.accept();
			int i=1;
			OutputStream os = s1.getOutputStream();
			InputStream is=s1.getInputStream();
			ECPoint curveParam=new ECPoint(BigInteger.valueOf(2),BigInteger.valueOf(2));
			BigInteger privKey= BigInteger.valueOf(7);
			ECPoint p=curve.ScalarMul(generator1,privKey);
			
			ModifiedTCPHeaderAuth auth=new ModifiedTCPHeaderAuth(curveParam,generator1,field,p);
			
			while(i!=0) {  
				
				List <Object> o=auth.getHeader();
				ObjectOutputStream oos1 = new ObjectOutputStream(os);
				oos1.writeObject(o);
				
				ObjectInputStream p3=new ObjectInputStream(is);
				List<?>B=(List<?>) p3.readObject();
				
				ECPoint B_Pub=(ECPoint) B.get(0);
				ECPoint shared_key=curve.ScalarMul(B_Pub,privKey);
				System.out.println("Shared Key:"+shared_key.x+" "+shared_key.y);
				
				i--;
				
				oos1.close();
				p3.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
