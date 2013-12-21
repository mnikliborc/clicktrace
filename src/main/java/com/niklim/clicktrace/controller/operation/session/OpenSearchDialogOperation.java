package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.dialog.SearchDialog;

public class OpenSearchDialogOperation extends AbstractOperation {

	@Inject
	private SearchDialog searchDialog;

	@Override
	public void perform() {
		searchDialog.open();
	}

}
