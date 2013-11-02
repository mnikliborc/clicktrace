package com.niklim.clicktrace.view.editor.action;

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
		String name = JOptionPane.showInputDialog("Set session name");
		if (name != null) {
			controller.newSession(name);
		}
	}

}
