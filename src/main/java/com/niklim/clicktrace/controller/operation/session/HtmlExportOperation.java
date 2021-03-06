package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.dialog.HtmlExportDialog;

public class HtmlExportOperation extends AbstractOperation {
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
