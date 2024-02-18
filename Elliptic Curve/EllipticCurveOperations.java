package ecc;

import java.math.BigInteger;

public interface EllipticCurveOperations {
	 
	 
	
	BigInteger ModInverse(BigInteger a, BigInteger b);
	
	ECPoint PointAddition(ECPoint point, ECPoint point2);
	ECPoint PointDoubling(ECPoint p1);


	ECPoint ScalarMul(ECPoint point, BigInteger k);
	
}
