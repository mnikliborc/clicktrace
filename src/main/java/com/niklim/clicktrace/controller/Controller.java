package com.niklim.clicktrace.controller;

import com.google.inject.Inject;
import com.niklim.clicktrace.capture.ChangeCapture;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.Editor;
import com.niklim.clicktrace.view.editor.menu.Menu;
import com.niklim.clicktrace.view.editor.menu.MenuController;
import com.niklim.clicktrace.view.tray.Tray;
import com.niklim.clicktrace.view.tray.TrayController;

public class Controller implements TrayController, MenuController {
	@Inject
	private ChangeCapture changeCapture;

	@Inject
	private Tray tray;

	@Inject
	private Editor editor;

	@Inject
	private Menu menu;

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
	public void openEditor() {
		editor.open(activeSession.getSession());
	}

	@Override
	public void newSession(String sessionName) {
		menu.sessionActive(true);

		Session session = new Session();
		session.setName(sessionName);

		activeSession.setSession(session);
		editor.open(session);
	}

	@Override
	public void openSession(Session session) {
		menu.sessionActive(true);
		activeSession.setSession(session);
		editor.showSession(session);
	}

	@Override
	public void deleteActiveSession() {
		Session session = activeSession.getSession();
		session.delete();

		activeSession.setSession(null);
		menu.sessionActive(false);
		editor.hideSession();
	}

}
