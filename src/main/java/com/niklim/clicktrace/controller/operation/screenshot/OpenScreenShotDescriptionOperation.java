package com.niklim.clicktrace.controller.operation.screenshot;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.view.dialog.DescriptionDialog;

public class OpenScreenShotDescriptionOperation extends AbstractOperation {

	@Inject
	private DescriptionDialog descriptionEditor;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void perform() {
		ScreenShot activeShot = activeSession.getActiveShot();
		if (activeShot != null) {
			descriptionEditor.open(activeShot);
		}
	}

}
