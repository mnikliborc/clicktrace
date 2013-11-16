package com.niklim.clicktrace.view.editor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.niklim.clicktrace.capture.CaptureModule;
import com.niklim.clicktrace.capture.MouseCapture;
import com.niklim.clicktrace.controller.ControllerModule;
import com.niklim.clicktrace.controller.KeyboardController;

public class TestGuiceContext {
	public static Injector load() {
		Injector injector = Guice.createInjector(new ControllerModule(), new CaptureModule());
		injector.getInstance(MouseCapture.class);
		injector.getInstance(KeyboardController.class);
		return injector;
	}
}
