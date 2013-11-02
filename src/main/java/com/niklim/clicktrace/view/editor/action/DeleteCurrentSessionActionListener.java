package com.niklim.clicktrace.view.editor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;
import com.niklim.clicktrace.view.editor.Editor;

public class DeleteCurrentSessionActionListener implements ActionListener {
	@Inject
	private Controller controller;

	@Inject
	private Editor editor;

	@Override
	public void actionPerformed(ActionEvent e) {
		int answer = JOptionPane
				.showConfirmDialog(editor.getFrame(), "Are you sure?", "", JOptionPane.OK_CANCEL_OPTION);
		if (answer == JOptionPane.OK_OPTION) {
			controller.deleteActiveSession();
		}
	}

}
