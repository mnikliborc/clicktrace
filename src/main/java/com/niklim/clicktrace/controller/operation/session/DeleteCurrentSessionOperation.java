package com.niklim.clicktrace.controller.operation.session;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.MainView;

public class DeleteCurrentSessionOperation extends AbstractOperation {
	@Inject
	private Controller controller;

	@Inject
	private MainView editor;


	@Override
	public void perform() {
		int answer = JOptionPane.showConfirmDialog(editor.getFrame(),
				"Are you sure to delete the session?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteActiveSession();
		}
	}

}
