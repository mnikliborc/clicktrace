package com.niklim.clicktrace.controller;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.ChangeCapture;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionAlreadyExistsException;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.view.editor.Editor;

@Singleton
public class Controller {
	@Inject
	private ChangeCapture changeCapture;

	@Inject
	private Editor editor;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private SessionManager sessionManager;

	public void startSession() {
		changeCapture.start();
		activeSession.setRecording(true);
		editor.sessionStateChanged();
	}

	public void stopSession() {
		changeCapture.stop();
		activeSession.setRecording(false);
		editor.sessionStateChanged();
	}

	public void newSession(String sessionName) {
		try {
			Session session = sessionManager.createSession(sessionName);

			openSession(session);
		} catch (SessionAlreadyExistsException ex) {
			JOptionPane.showMessageDialog(editor.getFrame(), "Could not create session. Already exists.");
		}
	}

	public void openSession(Session session) {
		activeSession.setSession(session);
		activeSession.setActive(true);
		activeSession.setRecording(false);
		changeCapture.stop();

		editor.showSession(session);
		editor.sessionStateChanged();
	}

	public void deleteActiveSession() {
		Session session = activeSession.getSession();
		session.delete();

		activeSession.setSession(null);
		activeSession.setActive(false);
		activeSession.setRecording(false);
		changeCapture.stop();

		editor.sessionStateChanged();
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
