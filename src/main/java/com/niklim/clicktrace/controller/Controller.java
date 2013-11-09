package com.niklim.clicktrace.controller;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.ChangeCapture;
import com.niklim.clicktrace.model.session.ScreenShot;
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
		activeSession.setRecording(true);
		editor.sessionStateChanged();
		changeCapture.start();
		editor.getFrame().setState(JFrame.ICONIFIED);
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
		activeSession.setShot(session.getShots().iterator().hasNext() ? session.getShots().iterator().next() : null);
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
		editor.setSelectedActiveScreenShot(selected);
		activeSession.setSelectedAllShots(selected);
	}

	public void refreshSession() {
		Session session = activeSession.getSession();
		session.loadScreenShots();
		editor.showSession(session);
	}

	public void showScreenShot(int i) {
		ScreenShot shot = activeSession.getSession().getShots().get(i);
		activeSession.setShot(shot);
		editor.showScreenShot(shot, activeSession.isShotSelected(shot));
		editor.refresh();
	}

	public void refreshScreenShot() {
		ScreenShot shot = activeSession.getShot();
		shot.loadImage();
		editor.showScreenShot(shot, activeSession.isShotSelected(shot));
		editor.refresh();
	}

	public void editScreenShot() {
		try {
			ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\mspaint.exe", "sessions\\"
					+ activeSession.getShot().getSession().getName() + "\\" + activeSession.getShot().getName());
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteScreenShot() {
		ScreenShot shot = activeSession.getShot();
		int indexOfNewActive = Math.max(0, activeSession.getSession().getShots().indexOf(shot) - 1);
		activeSession.removeShot(shot);
		shot.delete();

		ScreenShot newShot = activeSession.getSession().getShots().get(indexOfNewActive);
		editor.resetControl(activeSession.getSession());
		editor.showScreenShot(newShot, activeSession.isShotSelected(newShot));
		editor.refresh();
	}

	public void deleteSelectedScreenshots() {
		for (ScreenShot shot : activeSession.getSelectedShots()) {
			activeSession.removeShot(shot);
			shot.delete();
		}

		activeSession.setFirstShotActive();

		editor.showSession(activeSession.getSession());
		if (!activeSession.isShotSelected(activeSession.getShot())) {
			editor.showScreenShot(activeSession.getShot(), false);
		}
		activeSession.setSelectedAllShots(false);
		editor.sessionStateChanged();
	}

	public void selectScreenShot(boolean selected) {
		if (selected) {
			activeSession.selectShot(activeSession.getShot());
		} else {
			activeSession.deselectShot(activeSession.getShot());
		}
	}

}
