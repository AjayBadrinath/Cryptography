package ecc;

import java.math.BigInteger;

public class ECTest {
	public static void main(String[]args) {
		
		ECPoint generator=new ECPoint(BigInteger.valueOf(5),BigInteger.valueOf(1));
		ECPoint generator1=new ECPoint(BigInteger.valueOf(0),BigInteger.valueOf(11));
		ECField field=new ECField(BigInteger.valueOf(17));
		EllipticCurve curve=new EllipticCurve(BigInteger.valueOf(2),BigInteger.valueOf(2),field);
		curve.PointDoubling(generator);
		//for (int i=0;i<19;i++) {
		ECPoint p=curve.ScalarMul(generator, BigInteger.valueOf(18));
		//ECPoint p=curve.PointAddition(generator1, generator);
		System.out.println(p.x+","+p.y);
		//}
	}

}
