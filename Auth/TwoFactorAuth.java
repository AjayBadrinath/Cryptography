
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import de.taimos.totp.TOTP;

public class TwoFactorAuth{
	public static String getRan() {
		SecureRandom rval=new SecureRandom();
		byte []byteArr=new byte[20];
		rval.nextBytes(byteArr);
		Base32 base32=new Base32();
		base32.encode(byteArr);
		return base32.encodeToString(byteArr);
	}
	public static String getTimeBasedOTP(String SecretKey) {
		byte []bytes=new byte[20];
		Base32 base32=new Base32();
		return TOTP.getOTP(Hex.encodeHexString(base32.decode(SecretKey)));
	}
	public static void main(String [] args){
    /*
                    "Instructions to follow \n "
										+ "1.Copy the 32 charecter secret key and write it on a piece of paper\n"
										+ "2.Install any Multifactor Authentication App(Google Auth,MicrosoftAuth\n"
										+ "3.Goto Add new key  and paste the key onto the field\n"
										+ "4.Select Time Based (TOTP) and save you should see a 6 digit number on your app\n"
										+ "5.After entering details on mobile click yes to continue\n"
										+ "6.Verify the generated key below is the same if not synchronise your system time to your mobiles'\n"
										+ "7.All done you may login based on any method\n"
										
    */
  final String key=getRan(); // Initialise only once 
  System.out.println(key);
  
  String otp=getTimeBasedOtp(key);  // use this to check 6 digit code entered from phone via any authenticator app to system key ;
    
     
  }

}
