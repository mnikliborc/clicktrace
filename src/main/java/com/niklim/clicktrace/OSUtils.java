package com.niklim.clicktrace;

public class OSUtils {
	public static boolean isOnMac() {
		return System.getProperty("os.name").startsWith("Mac");
	}

	public static boolean isJava6() {
		return System.getProperty("java.version").startsWith("1.6");
	}
}
