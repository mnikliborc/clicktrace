package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.dialog.JiraExportDialog;

public class JiraExportOperation extends AbstractOperation {

	@Inject
	private JiraExportDialog jiraExportDialog;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void perform() {
		if (activeSession.isSessionLoaded()) {
			jiraExportDialog.open();
		}
	}

}
