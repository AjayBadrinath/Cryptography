package testss;

import java.math.BigInteger;
 

class ECCurveEquation {
	//y**2=x**3+ax+b
	
}
 class ECField implements AlgebraicStructures{
	private BigInteger p;
	ECField(BigInteger p){
		if (p.signum()<1) {
			throw new  IllegalArgumentException("Prime Field P Should be Positive. ");
		}
		this.p=p;
	}
	public BigInteger GetField() {
		return p;
		
	}
	public  int getFieldSize() {
		return p.bitLength();
		
	}
	/**
	 * For Low level Implementations .... Bit Ratios 
	 * @return 
	 *
	 */
	public double getFieldDensity() {
		return (double)p.bitCount()/(double)p.bitLength();
		
	}

}
 
class ECPoint implements EllipticCurveOperations {
	BigInteger x;
	BigInteger y;
	public ECPoint(BigInteger x,BigInteger y){
		this.x=x;
		this.y=y;
		
	}
	
}
 
public class EllipticCurve implements EllipticCurveOperations{
	private BigInteger a,b;
	private ECField prime;
	
	public EllipticCurve(BigInteger a,BigInteger b, ECField prime) {
		if(!(a.pow(3).multiply(BigInteger.valueOf(4))).add(b.pow(2).multiply(BigInteger.valueOf(27))).equals(BigInteger.ZERO)) {
		this.a=a;
		this.b=b;
		this.prime=prime;		}
		else {
			System.out.println("Curve Defined must be strictly Non Singular : -> Change Curve Parameters...");
			return;
		}
	}
	
	
	@Override
	public ECPoint PointAddition(ECPoint point,ECPoint point2) {
			BigInteger lambda,Numerator,Denominator,X,Y;
			if (point.equals(point2)) {
				//Breaking my unreadable code into legible parts.......
				//lambda=((point.x.pow(2)).multiply(BigInteger.valueOf(3))).add(a).multiply(this.ModInverse(point.y.multiply(BigInteger.valueOf(2)), prime.GetField()));
				Numerator=((point.x.pow(2)).multiply(BigInteger.valueOf(3))).add(a);
				Denominator=(point.y.multiply(BigInteger.valueOf(2)));
				lambda=Numerator.multiply(this.ModInverse(Denominator, prime.GetField())).mod(prime.GetField());
				
			}else {
				//lambda=(point2.y.subtract(point.y)).divide((point2.x.subtract(point.x)));
				Numerator=((point2.y.subtract(point.y)));
				Denominator=((point2.x.subtract(point.x)));
				lambda=Numerator.multiply(this.ModInverse(Denominator, prime.GetField())).mod(prime.GetField());
			}
			
			X=lambda.pow(2).subtract(point.x.subtract(point2.x)).mod(prime.GetField());
			Y=(lambda.multiply((point.x.subtract(X))).subtract(point.y)).mod((prime.GetField()));
			ECPoint point_new=new ECPoint(X,Y);
			return point_new;
			
		
	}

	@Override
	public ECPoint PointDoubling(ECPoint p1) {
		
		return this.PointAddition(p1, p1);
	}

	@Override
	public BigInteger ScalarMul() {
		// yet to implement !
		return null;
	}

	public BigInteger ModInverse(BigInteger a,BigInteger b) {
		// Finding Modular Inverse using Extended Euclidean Algorithm  ik there exists a BigInteger function that finds mod inv .. but whats the fun in that?
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
		
		return _s1;
	}

}
