package com.niklim.clicktrace;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.niklim.clicktrace.capture.CaptureModule;
import com.niklim.clicktrace.capture.mouse.MouseCapture;
import com.niklim.clicktrace.controller.ControllerModule;
import com.niklim.clicktrace.controller.GlobalKeyboardListener;

public class DefaultTestGuiceContext {
	public static Injector load() {
		Injector injector = Guice.createInjector(new ControllerModule(), new CaptureModule());
		injector.getInstance(MouseCapture.class);
		injector.getInstance(GlobalKeyboardListener.class);
		return injector;
	}
}
