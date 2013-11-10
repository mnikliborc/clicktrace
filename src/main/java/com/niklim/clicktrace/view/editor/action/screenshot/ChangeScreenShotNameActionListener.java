package com.niklim.clicktrace.view.editor.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;

public class ChangeScreenShotNameActionListener implements ActionListener {

	@Inject
	private Controller controller;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String label = JOptionPane.showInputDialog("Set screenshot label", activeSession.getActiveShot().getLabel());
		controller.changeActiveScreenShotName(label);
	}

}
