package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class DeselectAllScreenShotsOperation extends AbstractOperation {
	@Inject
	private Controller controller;

	@Override
	public void perform() {
		controller.setSelectedAllScreenshots(false);
	}
}
