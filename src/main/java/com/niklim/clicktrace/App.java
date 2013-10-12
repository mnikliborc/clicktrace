package com.niklim.clicktrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.niklim.clicktrace.capture.CaptureModule;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.controller.ControllerModule;

//A. SCREEN CAPTURE
//1. take screenshot
//2. compare with previous one to determine whether screen changed (use voters which decide, 
//e.g. for cursor move, text input. Detecting mouse/keyboard activity should be helpful.
//3. if change detected - save screenshot
//4. periodically use OCR to index screenshots

//B. CONFIGURE CAPTURE
//1. capture mouse clicks, key pressed?
//2. screen capture frequency
//3. on/off voters
//4. set capture session tag (to automatically annotate shots)

//C. SEARCH ENGINE
//1. search by: text, time, tags
//2. tag, edit (paint like), manipulate (putting in directory structure), compress, publish

public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ControllerModule(), new CaptureModule());
		injector.getInstance(Controller.class);

		log.info("app start");
	}
}
