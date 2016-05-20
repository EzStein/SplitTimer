package xyz.ezstein.backend.util;

import java.util.concurrent.TimeUnit;

/**
 * A utility class
 * @author Ezra
 *
 */
public class Util {
	
	/**
	 * Converts nanoseconds into a human readable time representation (hh:mm:ss)
	 * @param nanos
	 * @return a human readable time
	 */
	public static String nanosToReadable(long nanos){
		String out ="";
		if(nanos<0){
			nanos=-1*nanos;
			out+="-";
		}
		long hours = TimeUnit.NANOSECONDS.toHours(nanos);
		long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos)-TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(nanos));
		long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos)-TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(nanos));
		long milliseconds = TimeUnit.NANOSECONDS.toMillis(nanos)-TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(nanos));
		out+=hours+":"+minutes+":"+seconds+":"+milliseconds;
		return out;
	}
}
