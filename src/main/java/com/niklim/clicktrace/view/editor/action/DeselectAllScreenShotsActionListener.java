package com.niklim.clicktrace.view.editor.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;

public class DeselectAllScreenShotsActionListener implements ActionListener {
	@Inject
	private Controller controller;

	public void actionPerformed(ActionEvent event) {
		controller.setSelectedAllScreenshots(false);
	}
}
