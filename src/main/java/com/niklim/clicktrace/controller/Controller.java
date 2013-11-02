package com.niklim.clicktrace.controller;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.ChangeCapture;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionAlreadyExistsException;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.view.editor.Editor;
import com.niklim.clicktrace.view.editor.control.Menu;

@Singleton
public class Controller {
	@Inject
	private ChangeCapture changeCapture;

	@Inject
	private Editor editor;

	@Inject
	private Menu menu;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private SessionManager sessionManager;

	public void startSession() {
		changeCapture.start();
		activeSession.setActive(true);
	}

	public void stopSession() {
		changeCapture.stop();
		activeSession.setActive(false);
	}

	public void openEditor() {
		editor.open(activeSession.getSession());
	}

	public void newSession(String sessionName) {
		menu.sessionActive(true);

		try {
			Session session = sessionManager.createSession(sessionName);

			activeSession.setSession(session);
			activeSession.setActive(true);
			editor.showSession(session);
		} catch (SessionAlreadyExistsException ex) {
			JOptionPane.showMessageDialog(editor.getFrame(), "Could not create session. Already exists.");
		}
	}

	public void openSession(Session session) {
		menu.sessionActive(true);
		activeSession.setSession(session);
		activeSession.setActive(true);
		editor.showSession(session);
	}

	public void deleteActiveSession() {
		Session session = activeSession.getSession();
		session.delete();

		activeSession.setSession(null);
		menu.sessionActive(false);
		editor.hideSession();
	}

	public void setSelectedAllScreenshots(boolean selected) {
		editor.setSelectedAllScreenShots(selected);
	}

	public void deleteSelectedScreenshots() {
		editor.deleteSelectedScreenShots();

	}

	public void refreshSession() {
		Session session = activeSession.getSession();
		session.loadScreenShots();
		editor.showSession(session);
	}

}
