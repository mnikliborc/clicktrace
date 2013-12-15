package com.niklim.clicktrace.controller.operation.session;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.MainView;

public class DeleteCurrentSessionOperation extends AbstractOperation {
	@Inject
	private MainController controller;

	@Inject
	private MainView mainView;

	@Inject
	private ActiveSession activeSession;


	@Override
	public void perform() {
		if (!activeSession.isSessionLoaded()) {
			return;
		}

		int answer = JOptionPane.showConfirmDialog(mainView.getFrame(),
				"Are you sure to delete the session?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteActiveSession();
		}
	}

}
