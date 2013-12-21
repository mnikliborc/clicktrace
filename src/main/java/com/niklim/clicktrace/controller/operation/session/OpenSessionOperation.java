package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.dialog.OpenSessionDialog;

public class OpenSessionOperation extends AbstractOperation {
	@Inject
	private OpenSessionDialog openSessionDialog;

	@Override
	public void perform() {
		openSessionDialog.open();
	}
}
