package com.niklim.clicktrace.view.editor.action.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;
import com.niklim.clicktrace.view.editor.control.OpenSessionDialog;

public class OpenSessionActionListener implements ActionListener {
	@Inject
	private OpenSessionDialog openSessionDialog;

	@Override
	public void actionPerformed(ActionEvent e) {
		openSessionDialog.open();
	}
}
