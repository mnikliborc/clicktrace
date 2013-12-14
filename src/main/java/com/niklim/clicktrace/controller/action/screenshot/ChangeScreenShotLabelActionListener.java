package com.niklim.clicktrace.controller.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;

public class ChangeScreenShotLabelActionListener implements ActionListener {

	@Inject
	private Controller controller;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String label = JOptionPane.showInputDialog("Set screenshot label", activeSession.getActiveShot());
		if (label != null) {
			controller.changeActiveScreenShotLabel(label);
		}
	}

}
