package com.niklim.clicktrace.controller;

import com.google.inject.AbstractModule;
import com.niklim.clicktrace.controller.hook.GlobalKeyboardListener;
import com.niklim.clicktrace.controller.hook.GlobalKeyboardListenerImpl;

public class ControllerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GlobalKeyboardListener.class).to(GlobalKeyboardListenerImpl.class);
	}
	
}