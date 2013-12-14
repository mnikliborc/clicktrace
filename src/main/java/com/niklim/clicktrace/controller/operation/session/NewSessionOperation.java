package com.niklim.clicktrace.controller.operation.session;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;

public class NewSessionOperation extends AbstractOperation {
	@Inject
	private MainController controller;

	public boolean createSession() {
		String name = JOptionPane.showInputDialog("Set new session name");
		if (name != null) {
			return controller.newSession(name);
		} else {
			return false;
		}
	}

	@Override
	public void perform() {
		createSession();
	}

}
