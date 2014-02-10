package com.niklim.clicktrace.capture;

import java.awt.AWTException;
import java.awt.Robot;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.niklim.clicktrace.capture.mouse.ImmediateMouseCapture;
import com.niklim.clicktrace.capture.mouse.MouseCapture;

public class CaptureModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MouseCapture.class).to(ImmediateMouseCapture.class);
		requireBinding(MouseCapture.class);
	}

	@Provides
	Robot provideRobot() {
		try {
			return new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

}