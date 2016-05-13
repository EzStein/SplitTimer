package xyz.ezstein.backend.util;

import java.util.concurrent.TimeUnit;

public class Util {
	public static String nanosToReadable(long nanos){
		long hours = TimeUnit.NANOSECONDS.toHours(nanos);
		long minutes = TimeUnit.NANOSECONDS.toMinutes(nanos)-TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(nanos));
		long seconds = TimeUnit.NANOSECONDS.toSeconds(nanos)-TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(nanos));
		long milliseconds = TimeUnit.NANOSECONDS.toMillis(nanos)-TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(nanos));
		return hours+":"+minutes+":"+seconds+":"+milliseconds;
	}
}
