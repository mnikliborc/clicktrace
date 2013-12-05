package com.niklim.clicktrace.view.editor.action.session;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.view.editor.Editor;

public class DeleteSelectedScreenShotsActionListener implements ActionListener {
	@Inject
	private Controller controller;

	@Inject
	private Editor editor;

	@Inject
	private ActiveSession activeSession;

	public void actionPerformed(ActionEvent event) {
		if (activeSession.getSession() == null) {
			return;
		}

		int selectedShotsCount = activeSession.getSelectedShots().size();
		if (selectedShotsCount == 0) {
			JOptionPane.showMessageDialog(editor.getFrame(), "No screenshots selected.");
			return;
		}

		int answer = JOptionPane.showConfirmDialog(editor.getFrame(), "Are you sure to remove "
				+ selectedShotsCount + " screenshot(s)?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteSelectedScreenshots();
		}
	}
}
