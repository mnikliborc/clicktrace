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
import com.niklim.clicktrace.props.AppProperties;
import com.niklim.clicktrace.service.FileManager;

/**
 * Application bootstrap.
 */
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		redirectUnhandledExceptions();

		log.info("app started");

		// order of instantiation is important!
		Injector injector = createInjector();
		injector.getInstance(AppProperties.class);
		injector.getInstance(MouseCapture.class);
		injector.getInstance(GlobalKeyboardListener.class);

		FileManager fileManager = injector.getInstance(FileManager.class);
		fileManager.init();

		MainController controller = injector.getInstance(MainController.class);

		ToolTipManager.sharedInstance().setInitialDelay(1000);

		// TimeMeter.init();
		log.info("app ready to work");
		controller.init();
	}

	private static void redirectUnhandledExceptions() {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				log.error("Uncaught exception", e);
			}
		});
	}

	public static Injector createInjector() {
		return Guice.createInjector(new ControllerModule(), new CaptureModule());
	}
}
