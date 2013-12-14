package com.niklim.clicktrace.controller.action.screenshot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.Controller;

public class RefreshScreenShotActionListener implements ActionListener {

	@Inject
	private Controller controller;

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.refreshScreenShot();
	}

}
