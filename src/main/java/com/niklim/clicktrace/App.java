package com.niklim.clicktrace;

import javax.swing.ToolTipManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.niklim.clicktrace.capture.CaptureModule;
import com.niklim.clicktrace.capture.mouse.MouseCapture;
import com.niklim.clicktrace.controller.ControllerModule;
import com.niklim.clicktrace.controller.KeyboardController;
import com.niklim.clicktrace.controller.MainController;

/**
 * Application bootstrap.
 */
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new ControllerModule(), new CaptureModule());
		injector.getInstance(MouseCapture.class);
		injector.getInstance(KeyboardController.class);
		injector.getInstance(MainController.class);

		ToolTipManager.sharedInstance().setInitialDelay(1000);

		log.info("app started");
	}
}
