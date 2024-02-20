package ecc;
/*
 * 												Elliptic Curves Implementation 
 * @author : Ajay Badrinath
 * @Date   : 17/02/2024
 * 							Version Changelog:
 * 										1.0.2 - 
 * 												1.Fixed Swap Errors While Computing PointDoubling
 * 												2.Yet to Implement Infinity Points 
 * 												3.Fixed Modular inverse Function 
 												4.Added Point Negation								
 * 		
 * 
 * */

import java.math.BigInteger;

public interface EllipticCurveOperations {
	 
	 
	
	BigInteger ModInverse(BigInteger a, BigInteger b);
	
	ECPoint PointAddition(ECPoint point, ECPoint point2);
	ECPoint PointDoubling(ECPoint p1);


	ECPoint ScalarMul(ECPoint point, BigInteger k);
	
}
