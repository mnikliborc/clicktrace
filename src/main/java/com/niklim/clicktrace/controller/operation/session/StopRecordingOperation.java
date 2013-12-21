package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class StopRecordingOperation extends AbstractOperation {
	@Inject
	private MainController controller;

	@Override
	public void perform() {
		controller.stopRecording();
	}
}
