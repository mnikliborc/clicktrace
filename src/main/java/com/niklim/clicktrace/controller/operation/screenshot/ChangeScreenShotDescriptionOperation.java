package com.niklim.clicktrace.controller.operation.screenshot;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.dialog.description.DescriptionDialog;
import com.niklim.clicktrace.dialog.description.DescriptionDialogCallback;
import com.niklim.clicktrace.model.ScreenShot;

public class ChangeScreenShotDescriptionOperation extends AbstractOperation {

	@Inject
	private DescriptionDialog descriptionEditor;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private SaveScreenShotDescriptionOperation saveScreenShotDescriptionOperation;

	@Override
	public void perform() {
		final ScreenShot activeShot = activeSession.getActiveShot();
		if (activeShot != null) {
			descriptionEditor.open(new DescriptionDialogCallback() {

				@Override
				public void setText(String text) {
					activeShot.setDescription(text);
					saveScreenShotDescriptionOperation.perform();
				}

				@Override
				public String getTitle() {
					return activeShot + " - description";
				}

				@Override
				public String getText() {
					return activeShot.getDescription();
				}
			});
		}
	}

}
