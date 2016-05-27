package xyz.ezstein.backend.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import xyz.ezstein.backend.app.Locator;

/**
 * A utility class
 * @author Ezra
 *
 */
public class Util {
	
	public static final String CONFIG_FILE = "config.properties";
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
	
	public static String getProperty(String key){
		Properties properties = new Properties();
		try {
			if(!Locator.exists(CONFIG_FILE)){
				properties.store(Files.newOutputStream(Locator.locateFile(CONFIG_FILE)),"");
			} else {
				properties.load(Files.newInputStream(Locator.locateFile(CONFIG_FILE)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties.getProperty(key);
	}
	
	public static Properties getProperties(){
		Properties properties = new Properties();
		try {
			if(!Locator.exists(CONFIG_FILE)){
				properties.store(Files.newOutputStream(Locator.locateFile(CONFIG_FILE)),"");
			} else {
				properties.load(Files.newInputStream(Locator.locateFile(CONFIG_FILE)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	
	public static void setProperty(String key, String value){
		Properties properties = new Properties();
		try {
			if(!Locator.exists(CONFIG_FILE)){
				properties.store(Files.newOutputStream(Locator.locateFile(CONFIG_FILE)),"");
			} else {
				properties.load(Files.newInputStream(Locator.locateFile(CONFIG_FILE)));
			}
			properties.setProperty(key,value);
			properties.store(Files.newOutputStream(Locator.locateFile(CONFIG_FILE)),"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setProperties(Map<String, String> props){
		Properties properties = new Properties();
		try {
			if(!Locator.exists(CONFIG_FILE)){
				properties.store(Files.newOutputStream(Locator.locateFile(CONFIG_FILE)),"");
			} else {
				properties.load(Files.newInputStream(Locator.locateFile(CONFIG_FILE)));
			}
			for(String key : props.keySet()){
				properties.setProperty(key, props.get(key));
			}
			properties.store(Files.newOutputStream(Locator.locateFile(CONFIG_FILE)),"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
