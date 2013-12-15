package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.dialog.HtmlExportDialog;

public class OpenHtmlExportDialogOperation extends AbstractOperation {
	@Inject
	private HtmlExportDialog htmlExportDialog;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void perform() {
		if (activeSession.isSessionLoaded()) {
			htmlExportDialog.open();
		}
	}
}
