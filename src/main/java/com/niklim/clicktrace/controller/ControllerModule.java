package com.niklim.clicktrace.controller;

import com.google.inject.AbstractModule;
import com.niklim.clicktrace.view.tray.TrayController;

public class ControllerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TrayController.class).to(Controller.class);
	}

}