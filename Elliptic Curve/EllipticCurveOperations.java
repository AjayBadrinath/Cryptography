package testss;

import java.math.BigInteger;

public interface EllipticCurveOperations {
	 
	public BigInteger ScalarMul();
	
	
	BigInteger ModInverse(BigInteger a, BigInteger b);
	
	ECPoint PointAddition(ECPoint point, ECPoint point2);
	ECPoint PointDoubling(ECPoint p1);


	ECPoint ScalarMul(ECPoint point, BigInteger k);
	
}
