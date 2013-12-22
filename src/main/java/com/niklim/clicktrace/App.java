package com.niklim.clicktrace;

import javax.swing.ToolTipManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.niklim.clicktrace.capture.CaptureModule;
import com.niklim.clicktrace.capture.mouse.MouseCapture;
import com.niklim.clicktrace.controller.ControllerModule;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.hook.GlobalKeyboardListener;

/**
 * Application bootstrap.
 */
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		log.info("app started");

		Injector injector = createInjector();
		injector.getInstance(MouseCapture.class);
		injector.getInstance(GlobalKeyboardListener.class);

		MainController controller = injector.getInstance(MainController.class);

		ToolTipManager.sharedInstance().setInitialDelay(1000);

		log.info("app ready to work");
		controller.init();
	}

	public static Injector createInjector() {
		return Guice.createInjector(new ControllerModule(), new CaptureModule());
	}
}
