package com.niklim.clicktrace.controller;

import com.google.inject.Inject;
import com.niklim.clicktrace.capture.ActiveSession;
import com.niklim.clicktrace.capture.ChangeCapture;
import com.niklim.clicktrace.view.editor.Editor;
import com.niklim.clicktrace.view.tray.Tray;
import com.niklim.clicktrace.view.tray.TrayController;

public class Controller implements TrayController {
	@Inject
	private ChangeCapture changeCapture;

	@Inject
	private Tray tray;

	@Inject
	private Editor editor;

	@Inject
	private ActiveSession activeSession;

	@Override
	public void startSession() {
		changeCapture.start();
		activeSession.setActive(true);
	}

	@Override
	public void stopSession() {
		changeCapture.stop();
		activeSession.setActive(false);
	}

	@Override
	public String getActiveSessionName() {
		return activeSession.getSessionName();
	}

	@Override
	public void setSessionName(String sessionName) {
		activeSession.setSessionName(sessionName);
	}

	@Override
	public void openEditor() {
		editor.open(activeSession.getSessionName());
	}

}
