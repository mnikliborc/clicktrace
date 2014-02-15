package com.niklim.clicktrace;

import java.text.MessageFormat;

import org.slf4j.Logger;

public class TimeMeter {
	private final long startMilis;
	private final String label;
	private final Logger log;

	private static boolean active = false;

	public static void init() {
		active = true;
	}

	private TimeMeter(String label, Logger log) {
		startMilis = System.currentTimeMillis();
		this.label = label;
		this.log = log;
	}

	public void stop() {
		if (!active)
			return;

		long measurement = System.currentTimeMillis() - startMilis;
		String msg = MessageFormat.format("{0} - {1}ms", label, measurement);
		if (log != null) {
			log.debug(msg);
		} else {
			System.out.println(msg);
		}
	}

	public static TimeMeter start(String label, Logger log) {
		return new TimeMeter(label, log);
	}

	public static TimeMeter start(String label) {
		return new TimeMeter(label, null);
	}
}
