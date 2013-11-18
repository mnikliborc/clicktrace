package com.niklim.clicktrace.capture;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.niklim.clicktrace.capture.mouse.CollectorMouseCapture;
import com.niklim.clicktrace.capture.mouse.MouseCapture;

public class CaptureModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(MouseCapture.class).to(CollectorMouseCapture.class);
	}

	@Provides
	List<ChangeVoter> provideChangeVoters() {
		return Lists.<ChangeVoter> newArrayList(new NoVoter());
	}

	@Provides
	Robot provideRobot() {
		try {
			return new Robot();
		} catch (AWTException e) {
			new RuntimeException(e);
		}
		return null;
	}

}