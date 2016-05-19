package xyz.ezstein.backend.app;

import java.io.File;

/**
 * An enum holding information about different operating systems.
 * @author Ezra Stein
 * @version 1.0
 * @since 2015
 */
public enum OS
{
	/**
	 * A Mac OS
	 */
	MAC(System.getProperty("user.home")+"/Library/Application Support/"), 
	/**
	 * A Windows OS
	 */
	WINDOWS(System.getenv("APPDATA") + File.separator),
	/**
	 * A Linux OS
	 */
	LINUX(System.getProperty("user.home") + "/"),
	/**
	 * A Solaris OS
	 */
	SOLARIS(System.getProperty("user.home") + "/"),
	/**
	 * OS is unknown
	 */
	UNKNOWN("");
	
	private String basePath;
	private OS(String basePath){
		this.basePath=basePath;
	}
	
	/**
	 * Returns the basePath to the application data folder
	 * @return the base path to the application data folder
	 */
	public String getBasePath(){
		return basePath;
	}
	
	/**
	 * Static method which returns the operating system of this computer
	 * @return the operating system of this computer
	 */
	public static OS getOS(){
		String osName = System.getProperty("os.name").toLowerCase();
		OS os;
		if(osName.indexOf("mac")>=0)
		{
			os =  OS.MAC;
		}
		else if(osName.indexOf("win")>=0)
		{
			os =  OS.WINDOWS;
		}
		else if(osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0 )
		{
			os = OS.LINUX;
		}
		else if(osName.indexOf("sunos") >= 0)
		{
			os = OS.SOLARIS;
		}
		else
		{
			os = OS.UNKNOWN;
		}
		return os;
	}
}
