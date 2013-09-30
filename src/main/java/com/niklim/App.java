package com.niklim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new CaptureModule());
		Capture instance = injector.getInstance(Capture.class);

		log.info("app start");
		instance.start();
	}
}
