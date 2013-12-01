package com.niklim.clicktrace.view.editor.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;
import com.niklim.clicktrace.view.editor.dialog.DescriptionDialog;

public class OpenScreenShotDescriptionActionListener implements ActionListener {

	@Inject
	private DescriptionDialog descriptionEditor;

	@Override
	public void actionPerformed(ActionEvent e) {
		descriptionEditor.open();
	}

}
