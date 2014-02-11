package com.niklim.clicktrace;

public class OSUtils {
	public static boolean isOnMac() {
		return System.getProperty("os.name").startsWith("Mac");
	}
}
