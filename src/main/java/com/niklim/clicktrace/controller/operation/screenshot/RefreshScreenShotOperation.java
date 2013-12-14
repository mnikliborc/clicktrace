package com.niklim.clicktrace.controller.operation.screenshot;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class RefreshScreenShotOperation extends AbstractOperation {

	@Inject
	private Controller controller;

	@Override
	public void perform() {
		controller.refreshScreenShot();
	}

}
