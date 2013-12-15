package com.niklim.clicktrace.controller;

import com.google.inject.AbstractModule;

public class ControllerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GlobalKeyboardListener.class).to(GlobalKeyboardListenerImpl.class);
	}
	
}