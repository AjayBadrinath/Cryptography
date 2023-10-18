package testss;
/*
 * @author : Ajay Badrinath
 * @date   : 16-10-23
 * */
/*
 * Mersenne Class containing constants , generating function , seed , and twisting 
 * 
 * */
class mersenne{
	/*
	 * Mersenne Constants
	 * 
	 * */
	private final int N=624;
	private final int M=397;
	private final long w=32;
	private final long u=11;
	private final long s=7;
	private final long t=15;
	private final long l=18;
	private final long b=0x9d2c5680;
	private final long c=0xefc60000;
	private final long UpperMask=0x80000000;
	/*
	 * Global index to track the index of global array, fetch new Random Numbers only when exhausting 623 indices.
	 * 
	 * */
	int idx=N+1;
	long seed;
	/*
	 * MT - 1x624 dimensional state Array
	 * Access generated number Exhaustively in the array
	 * */
	long []MT;
	long mask=((1L<<32)-1);	// MT Standard is for 32 bits only so performing k&mask -->32 bit long 
	
	/*
	 * Constructor for initializing the state array with the seed.
	 * */
	mersenne(long seed){
		this.seed=seed;
		initArray(seed);
	}
	/*
	 * The seed function seeds the MT array and fill up the entire array with seed generated from MT[i-1]  
	 * */
	public  void initArray(long seed) {
		MT=new long[N];
		MT[0]=seed&mask;
		for(int i=1;i<N;i++) {
			MT[i]=((69069*(MT[i-1]^(MT[i-1]>>(w-2)))+i))&mask;
		}
		
		
	}
	/*
	 * Function to generate Random Numbers from the initialized state array
	 * */
	long genRand() {
		if(idx>=N) {
			twist(); // If the index Exceed the generated state array then perform twist operation
			idx=0;
		}
		/*
		 * MT Standard Definition of Tempering Shifts.
		 * Tempering shift done to reduce preimage attacks 
		 * */
		long y=MT[idx++];
		y=(y^(y>>u))&mask;
		y=(y^((y<<s)&b))&mask;
		y=(y^((y<<t)&c))&mask;
		y=(y^(y>>l))&mask;
		return y&mask;
	}
	/*
	void twist() {
		long tmp,t1;
		for(int i=0;i<N;i++) {
			tmp=(MT[i]&UpperMask)+(MT[(i+1)%N])&mask;
			t1=tmp>>1;
			if(tmp%2!=0) {
				tmp^=a;
			}
			MT[i]=MT[(i+M)%N]^t1;
		}
	}
	*/
	/*
	 * Function Twist to Ensure Huge period (2^some Mersenne prime-1)
	 * */
	void twist() {
		long tmp,t1;
		int j;
		
		long mag[]= {0x0,0x9908b0df};
		for(j=0;j<N-M;j++) {
			tmp=(MT[j]&UpperMask)+(MT[(j+1)%N])&mask;
			t1=tmp>>1;
			MT[j]=MT[j+M]^t1^mag[(int)tmp&0x1];
			
		}
		for(;j<N-1;j++) {
			tmp=(MT[j]&UpperMask)+(MT[(j+1)%N])&mask;
			t1=tmp>>1;
			MT[j]=MT[j+(M-N)]^t1^mag[(int)tmp&0x1];
		}
		tmp=(MT[N-1]&UpperMask)|(MT[0]&mask);
		t1=tmp>>1;
		MT[N-1]=MT[M-1]^t1^mag[(int)tmp&0x1];
		
		
	}
	
	
	
}
public class mersenneTwister {
	public static void main(String[]args) {
		//initialise and set the seed as some instance of curr time -> Ensures non repetition
	mersenne x=new mersenne(System.currentTimeMillis());
	/*Typecast 32-bit long into int and bound by (abs,100) to generate random numbers from 1-100*/
	for(int i=0;i<100;i++) {
		//System.out.println(String.format("%8s",Long.toHexString(x.genRand())).replace(" ", "0"));
		System.out.println(Math.abs((int)x.genRand()%100));
		
	}
	
	}
}
