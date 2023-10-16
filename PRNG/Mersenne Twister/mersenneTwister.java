package testss;
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
	
	long []MT;
	long mask=((1L<<32)-1);
	mersenne(long seed){
		this.seed=seed;
		initArray(seed);
	}
	public  void initArray(long seed) {
		MT=new long[N];
		MT[0]=seed&mask;
		for(int i=1;i<N;i++) {
			MT[i]=((69069*(MT[i-1]^(MT[i-1]>>(w-2)))+i))&mask;
		}
		
		
	}
	long genRand() {
		if(idx>=N) {
			twist();
			idx=0;
		}
		/*
		 * MT Standard Definition of Tempering Shifts.
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
	mersenne x=new mersenne(System.currentTimeMillis());
	for(int i=0;i<100;i++) {
		//System.out.println(String.format("%8s",Long.toHexString(x.genRand())).replace(" ", "0"));
		System.out.println(Math.abs((int)x.genRand()%100));
		
	}
	
	}
}
