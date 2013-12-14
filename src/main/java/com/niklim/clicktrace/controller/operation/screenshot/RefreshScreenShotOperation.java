package com.niklim.clicktrace.controller.operation.screenshot;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class RefreshScreenShotOperation extends AbstractOperation {

	@Inject
	private MainController controller;

	@Override
	public void perform() {
		controller.refreshScreenShot();
	}

}
