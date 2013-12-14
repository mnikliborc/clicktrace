package com.niklim.clicktrace.view.editor.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.view.editor.Editor;

public class DeleteScreenShotActionListener implements ActionListener {
	@Inject
	private Editor editor;
	@Inject
	private Controller controller;
	@Inject
	private ActiveSession activeSesssion;

	@Override
	public void actionPerformed(ActionEvent e) {
		if (activeSesssion.getSession() == null || activeSesssion.getActiveShot() == null) {
			return;
		}
		String screenShot = activeSesssion.getActiveShot().toString();
		int answer = JOptionPane.showConfirmDialog(editor.getFrame(), "Are you sure to delete '"
				+ screenShot + "' screenshot?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteActiveScreenShot();
		}
	}

}
