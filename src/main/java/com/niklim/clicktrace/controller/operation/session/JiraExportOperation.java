package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.dialog.jira.JiraLoginDialog;

public class JiraExportOperation extends AbstractOperation {

	@Inject
	private JiraLoginDialog jiraLoginDialog;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void perform() {
		if (activeSession.isSessionLoaded()) {
			jiraLoginDialog.open();
		}
	}

}
