package com.niklim;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

class CaptureModule extends AbstractModule {

	@Override
	protected void configure() {
	}

	@Provides
	List<ChangeVoter> provideChangeVoters() {
		return Lists.<ChangeVoter> newArrayList(new PixelVoter());
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