package com.niklim.clicktrace;

import javax.swing.ToolTipManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.niklim.clicktrace.capture.CaptureModule;
import com.niklim.clicktrace.capture.mouse.MouseCapture;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.controller.ControllerModule;
import com.niklim.clicktrace.controller.KeyboardController;

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

/**
 * TODO ensure session name is valid directory name
 * 
 */
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ControllerModule(), new CaptureModule());
		injector.getInstance(MouseCapture.class);
		injector.getInstance(KeyboardController.class);
		injector.getInstance(Controller.class);

		ToolTipManager.sharedInstance().setInitialDelay(1000);

		log.info("app start");
	}
}
