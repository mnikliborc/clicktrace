package com.niklim.clicktrace.view.editor.action.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;

public class ChangeSessionLabelActionListener implements ActionListener {

	@Inject
	private Controller controller;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String label = JOptionPane.showInputDialog("Set session name", activeSession.getSession());
		if (label != null) {
			controller.changeActiveSessionLabel(label);
		}
	}

}
