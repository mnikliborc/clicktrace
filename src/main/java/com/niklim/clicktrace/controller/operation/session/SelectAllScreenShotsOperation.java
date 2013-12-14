package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class SelectAllScreenShotsOperation extends AbstractOperation {
	@Inject
	private MainController controller;

	@Override
	public void perform() {
		controller.setSelectedAllScreenshots(true);
	}
}
