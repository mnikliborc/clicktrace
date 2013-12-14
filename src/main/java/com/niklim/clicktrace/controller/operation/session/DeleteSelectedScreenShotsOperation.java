package com.niklim.clicktrace.controller.operation.session;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.view.MainView;

public class DeleteSelectedScreenShotsOperation extends AbstractOperation {
	@Inject
	private MainController controller;

	@Inject
	private MainView mainView;

	@Inject
	private ActiveSession activeSession;

	public void actionPerformed(ActionEvent event) {
	}

	@Override
	public void perform() {
		if (activeSession.getSession() == null) {
			return;
		}

		int selectedShotsCount = activeSession.getSelectedShots().size();
		if (selectedShotsCount == 0) {
			JOptionPane.showMessageDialog(mainView.getFrame(), "No screenshots selected.");
			return;
		}

		int answer = JOptionPane.showConfirmDialog(mainView.getFrame(), "Are you sure to remove "
				+ selectedShotsCount + " screenshot(s)?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteSelectedScreenshots();
		}
	}
}
