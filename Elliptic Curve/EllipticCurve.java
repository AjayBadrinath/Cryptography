package ecc;
/*
 * 												Elliptic Curves Implementation 
 * @author : Ajay Badrinath
 * @Date   : 17/02/2024
 * 							Version Changelog:
 * 								
 * 										1.0.4 - 
 * 												1.Fixed Swap Errors While Computing PointDoubling
 * 												2.Added Support for Point at inf ( does have side-effects!!!)
 * 												3.Fixed Modular inverse Function 
 												4.Added Point Negation	
 												5.Remove Brute Force Aproach on Additive Inverse with a clean implementation.
 												6.Added Serialization For ECField and ECPoint Classes .
 												
 								
 							To Fix: 
 												1.Point One After Infinty behaves differently but does not fail. Otherwise Passed all test.	
 															
 * 		
 * 
 * */


import java.io.Serializable;
import java.math.BigInteger;


/*
 * This class is currently empty and is a placeholder for supporting Strong Curves such as Curve 448/Curve 25519
 * 
 * Currently Supported Curve : Weistrass Curve 
					
 * 		
 * 
 * */
/*
class ECCurveEquation {
	//y**2=x**3+ax+b
	
}
*/

/**
 *
 *    Defining   Elliptic Curve Field over Some Prime 
 *    
 *
 */

 class ECField implements Serializable , AlgebraicStructures {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigInteger p;
	// Check for Prime Not Negative as we deal with only Prime Fields.
	ECField(BigInteger p){
		
		if (p.signum()<1) {
			throw new  IllegalArgumentException("Prime Field P Should be Positive. ");
		}
		this.p=p;
	}
	public BigInteger GetField() {
		return p;
		
	}
	/*
	 * By Hasse's Theorem The Number of points on the curve is Relative to the Prime Field Strength.
	 * */
	public BigInteger getSizeOfField() {
		BigInteger ul=p.add(BigInteger.ONE).subtract(BigInteger.TWO.multiply(p.sqrt()));
		BigInteger ll=p.add(BigInteger.ONE).add(BigInteger.TWO.multiply(p.sqrt()));
		return ul.subtract(ll).abs();
	}
	public  int getFieldSize() {
		return p.bitLength();
		
	}
	/**
	 * For Low level Implementations .... Bit Ratios . Not of Much Relevance but has to be considered for performance Issues.....
	 * @return 
	 *
	 */
	public double getFieldDensity() {
		return (double)p.bitCount()/(double)p.bitLength();
		
	}

}
 
 /**
  * 
  * ECPoint Class :
  * 		 This Class is a Member Class that Encapsulates 2-D Elliptic Curve points (x,y) . Helps to Perform Group Operations on Points.
  * 
  *
  */
class ECPoint implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2850362574454304355L;
	BigInteger x;
	BigInteger y;
	public ECPoint(BigInteger x,BigInteger y){
		this.x=x;
		this.y=y;
		
	}
	
}

/*
 * Main Class Implementing EllipticCurveOperations Interface .
 * 
 *  Here I am Implementing only the Weistrass Curve of form y^2=x^3 + ax+b mod p (where a,b are curve parameters) and p belong to Prime Field Defined 
 *  
 *  Default Constructor Checks for NonSingular Curves Condition 
 * 	EC Group Operations:
 * 				1.Point Addition 
 * 				2.Point Doubling
 * 				3.Scalar Multiplication
 * 				4.Point Negation
 * 
 * */
public class EllipticCurve implements EllipticCurveOperations{
	private BigInteger a,b;
	private ECField prime;
	public ECPoint INFINITY;
	
	public EllipticCurve(BigInteger a,BigInteger b, ECField prime) {
		if(!(a.pow(3).multiply(BigInteger.valueOf(4))).add(b.pow(2).multiply(BigInteger.valueOf(27))).equals(BigInteger.ZERO)) {
		this.a=a;
		this.b=b;
		this.prime=prime;	
		INFINITY=new ECPoint(BigInteger.ZERO,BigInteger.ZERO);
		}
		else {
			System.out.println("Curve Defined must be strictly Non Singular : -> Change Curve Parameters...");
			return;
		}
	}
	
	
	@Override
	/*
	 * Function That Define Point Addition Group Operation 
	 * 
	 * */
	public ECPoint PointAddition(ECPoint point,ECPoint point2) {
			BigInteger lambda,Numerator,Denominator,X,Y;
			if (point.equals(point2)) {
				//Breaking my unreadable code into legible parts.......
				//lambda=((point.x.pow(2)).multiply(BigInteger.valueOf(3))).add(a).multiply(this.ModInverse(point.y.multiply(BigInteger.valueOf(2)), prime.GetField()));
				Numerator=(point.x.pow(2)).multiply(BigInteger.valueOf(3)).add(a).mod(prime.GetField());
				Denominator=(point.y.multiply(BigInteger.valueOf(2)));
				lambda=Numerator.multiply(ModInverse(Denominator, prime.GetField())).mod(prime.GetField());
				
			}else {
				//lambda=(point2.y.subtract(point.y)).divide((point2.x.subtract(point.x)));
				Numerator=((point2.y.subtract(point.y))).mod(prime.GetField());
				Denominator=((point2.x.subtract(point.x)));
				lambda=Numerator.multiply(ModInverse(Denominator, prime.GetField())).mod(prime.GetField());
			}
			
			X=(lambda.pow(2).subtract(point.x).subtract(point2.x)).mod(prime.GetField());
			Y=(lambda.multiply(point.x.subtract(X)).subtract(point.y)).mod(prime.GetField());
			ECPoint point_new=new ECPoint(X,Y);
			if((AdditiveModInverse(point2.y).equals(point.y))&&(point2.x.equals(point.x))) {
				
				return INFINITY;
				
				}
			return point_new;
			
		
	}
	/*
	 * Function to compute Point Reflection On Elliptic Curve.
	 * 
	 * 
	 * */
	public ECPoint PointInverse(ECPoint p) {
		p.y=p.y.multiply(BigInteger.valueOf(-1)).add(prime.GetField());
		return p;
		
	}
	
	@Override
	
	/*
	 * Function That Define Point Doubling Group Operation (Only Slope Changes)
	 * 
	 * */
	public ECPoint PointDoubling(ECPoint p1) {
		if(p1.equals(INFINITY)) {
			
			return INFINITY;
			
			}
		return this.PointAddition(p1, p1);
	}

	@Override
	/*
	 * Function That Define Scalar Multiplication Group Operation 
	 * Done Using Fast Double and Add method .Singular Loop 
	 * 
	 * */
	public ECPoint ScalarMul(ECPoint point,BigInteger k) {

		ECPoint result=point;
		
		
		for (int i=k.bitLength()-2;i>=0;i--){
			
			
			//System.out.println("Point Doubling "+result.x+" "+result.y);
			
			result=PointDoubling(result);
			System.out.println("Point Doubling "+result.x+" "+result.y);
			
			if(k.testBit(i)) {
				if(result.equals(INFINITY)) {
					//result=point;
					
				}
				System.out.println("Point Addition"+result.x+" "+result.y);
				result=PointAddition(point,result);
				System.out.println("Point Addition"+result.x+" "+result.y);
				//System.out.println("Param"+" "+point.x+" "+point.y);

			}
			
		}
		return result;
	}
	public ECPoint ScalarMulRec(ECPoint point,BigInteger k) {
		if (k.equals(BigInteger.ZERO)) {
			return null;
		}
		else if(k.equals(BigInteger.ONE)) {
			return point;
		}
		 
		else {
			ECPoint result = ScalarMulRec(PointDoubling(point), k.divide(BigInteger.TWO));
		
        if (k.mod(BigInteger.valueOf(2)).equals(BigInteger.ONE)) {
            result = PointAddition(result, point);
        }
        return result;
		}
	}
	/*
	 * Function to Find Modular Inverse Within a Field Fp Using Extended Euclidean Algorithm.
	 *  Changes:
	 *  	1. Extended Support for -ve Numerator and Error check for bounds on Denominator sign.
	 *  	2. Fixed Stray negation Error that causes inconsistency while computing points on curve (Especially Odd Point 2*kP+P)
	 *  	3. Consistent
	 * */
	public BigInteger ModInverse(BigInteger a,BigInteger b) {

		// Finding Modular Inverse using Extended Euclidean Algorithm  ik there exists a BigInteger function that finds mod inv .. but whats the fun in that?
		if ((b.compareTo(BigInteger.ZERO)==0) || (b.compareTo(BigInteger.ZERO)==-1)) {
			System.out.println("Moduland Must not be zero");
			return null;
		}
		BigInteger _q=BigInteger.ZERO;
		BigInteger _rem=BigInteger.ZERO;
		BigInteger A= a;
		BigInteger B=b;
		BigInteger _s1=BigInteger.ONE;
		BigInteger _s2=BigInteger.ZERO;
		BigInteger _s=BigInteger.ZERO;
		BigInteger[] q_r=new BigInteger[2];
	
		
		
		while (!B.equals(BigInteger.ZERO)) {
			
			q_r=A.divideAndRemainder(B);
			_q=q_r[0];
			_rem=q_r[1];
			_s=_s1.subtract(_s2.multiply(_q));
			A=B;
			B=_rem;
			_s1=_s2;
			_s2=_s;
			
		}
		if((a.compareTo(b)==-1 && a.signum()==-1)) {
			return b.subtract(_s1);
		}
		if (_s1.signum()==-1 ) {
			return _s1.add(b);
		}else {
		
		
		return _s1;
		}
	}
	
	/*
	 * Revoke Bruteforce approach /../
	 * 
	 * */
	public BigInteger AdditiveModInverse(BigInteger k) {
		//Raw BruteForce 
		/*
		for (BigInteger i=BigInteger.ZERO;(i.compareTo(prime.GetField())==-1);i=i.add(BigInteger.ONE)) {
			//System.out.println(i);
			if((k.add(i)).mod(prime.GetField()).equals(BigInteger.ZERO)) {
				
				return i;
			}
		}*/
		return k.negate().mod(prime.GetField());
		
	}

}

