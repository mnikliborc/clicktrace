package com.niklim.clicktrace.controller.action.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;

public class NewSessionActionListener implements ActionListener {
	@Inject
	private Controller controller;

	@Override
	public void actionPerformed(ActionEvent e) {
		createSession();
	}

	public boolean createSession() {
		String name = JOptionPane.showInputDialog("Set new session name");
		if (name != null) {
			return controller.newSession(name);
		} else {
			return false;
		}
	}

}
