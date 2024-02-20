package ecc;

import java.math.BigInteger;

public class ECTest {
	public static void main(String[]args) {
		
		ECPoint generator=new ECPoint(BigInteger.valueOf(0),BigInteger.valueOf(11));
		ECPoint generator1=new ECPoint(BigInteger.valueOf(5),BigInteger.valueOf(1));
		ECField field=new ECField(BigInteger.valueOf(17));
		EllipticCurve curve=new EllipticCurve(BigInteger.valueOf(2),BigInteger.valueOf(2),field);
		
		
		//ECPoint p=curve.PointDoubling(generator);
		//ECPoint p=curve.PointAddition_sans_builtin(generator1, generator);
		
		//curve.PointDoubling(generator);
		//ECPoint p=generator;
		//for (int i=1;i<18;i++) {
		//ECPoint p=curve.ScalarMul(generator1, BigInteger.valueOf(13));
		//ECPoint p=curve.PointAddition(generator1, generator);
		ECPoint p =curve.ScalarMul(generator1, BigInteger.valueOf(26));
		//ECPoint p =curve.PointDoubling(generator);
		System.out.println(p.x+","+p.y);
		//}
		//System.out.println(p.x+","+p.y+" "+BigInteger.valueOf(19).testBit(3));
		//System.out.println(BigInteger.valueOf(-5).modInverse(BigInteger.valueOf(17)));
		//System.out.println(BigInteger.valueOf(17).divideAndRemainder(BigInteger.valueOf(-5))[1]);
		//System.out.println(curve.ModInverse(BigInteger.valueOf(-5),BigInteger.valueOf(17)));
	}

}
