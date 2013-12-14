package com.niklim.clicktrace.controller.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;
import com.niklim.clicktrace.view.dialog.SearchDialog;

public class OpenSearchDialogActionListener implements ActionListener {

	@Inject
	private SearchDialog searchDialog;

	@Override
	public void actionPerformed(ActionEvent e) {
		searchDialog.open();
	}

}
