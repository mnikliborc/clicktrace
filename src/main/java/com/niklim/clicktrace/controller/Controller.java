package com.niklim.clicktrace.controller;

import com.google.inject.Inject;
import com.niklim.clicktrace.capture.Capture;
import com.niklim.clicktrace.editor.Editor;
import com.niklim.clicktrace.tray.Tray;
import com.niklim.clicktrace.tray.TrayController;

public class Controller implements TrayController {
	@Inject
	private Capture capture;

	@Inject
	private Tray tray;

	@Inject
	private Editor editor;

	@Override
	public void startSession() {
		capture.start();
	}

	@Override
	public void stopSession() {
		capture.stop();
	}

	@Override
	public String getSessionName() {
		return capture.getSessionName();
	}

	@Override
	public void setSessionName(String name) {
		capture.setSessionName(name);
	}

	@Override
	public void openEditor() {
		editor.open(capture.getSessionName());
	}

}
