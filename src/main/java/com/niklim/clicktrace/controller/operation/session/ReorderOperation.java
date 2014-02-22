package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.dialog.ReorderingDialog;

public class ReorderOperation extends AbstractOperation {
	@Inject
	private ReorderingDialog reorderingDialog;

	@Override
	public void perform() {
		reorderingDialog.open();
	}

}
