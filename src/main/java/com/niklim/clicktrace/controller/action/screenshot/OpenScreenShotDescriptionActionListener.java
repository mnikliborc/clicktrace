package com.niklim.clicktrace.controller.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.view.dialog.DescriptionDialog;

public class OpenScreenShotDescriptionActionListener implements ActionListener {

	@Inject
	private DescriptionDialog descriptionEditor;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void actionPerformed(ActionEvent e) {
		ScreenShot activeShot = activeSession.getActiveShot();
		if (activeShot != null) {
			descriptionEditor.open(activeShot);
		}
	}

}
