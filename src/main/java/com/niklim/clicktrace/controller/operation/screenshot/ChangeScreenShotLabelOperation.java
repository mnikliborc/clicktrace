package com.niklim.clicktrace.controller.operation.screenshot;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class ChangeScreenShotLabelOperation extends AbstractOperation {

	@Inject
	private MainController controller;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void perform() {
		if (!activeSession.isActiveShotLoaded()) {
			return;
		}

		String label = JOptionPane.showInputDialog("Set screenshot label", activeSession.getActiveShot());
		if (label != null) {
			controller.changeActiveScreenShotLabel(label);
		}
	}

}
