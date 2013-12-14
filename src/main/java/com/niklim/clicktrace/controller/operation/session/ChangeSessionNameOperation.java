package com.niklim.clicktrace.controller.operation.session;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class ChangeSessionNameOperation extends AbstractOperation {

	@Inject
	private Controller controller;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void perform() {
		String label = JOptionPane.showInputDialog("Set session name", activeSession.getSession());
		if (label != null) {
			controller.changeActiveSessionName(label);
		}
	}

}
