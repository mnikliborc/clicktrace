package com.niklim;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new CaptureModule());
		Capture instance = injector.getInstance(Capture.class);
        instance.start();
	}
}
