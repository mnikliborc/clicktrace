package com.niklim.clicktrace.controller;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Singleton;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;

/**
 * Manages active Clicktrace session.
 */
@Singleton
public class ActiveSession {
	private Session session;
	private ScreenShot activeShot;
	private boolean recording = false;
	private Set<ScreenShot> selectedShots = new HashSet<ScreenShot>();


	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean isSessionLoaded() {
		return session != null;
	}

	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public ScreenShot getActiveShot() {
		return activeShot;
	}

	public void setActiveShot(ScreenShot shot) {
		activeShot = shot;
	}

	public boolean isActiveShotLoaded() {
		return activeShot != null;
	}

	public ScreenShot getShot(int i) {
		return session.getShots().get(i);
	}

	public boolean isShotSelected(ScreenShot shot) {
		return selectedShots.contains(shot);
	}

	public void selectShot(ScreenShot shot) {
		selectedShots.add(shot);
	}

	public void deselectShot(ScreenShot shot) {
		selectedShots.remove(shot);
	}

	public void setAllShotsSelected(boolean selected) {
		selectedShots.clear();
		if (selected) {
			selectedShots.addAll(session.getShots());
		}
	}

	public Set<ScreenShot> getSelectedShots() {
		return selectedShots;
	}

	/**
	 * Removes the shot from the active session. Does not delete it from disk.
	 * 
	 * @param remShot
	 */
	public void removeShot(ScreenShot remShot) {
		getSession().getShots().remove(remShot);
		deselectShot(remShot);
		if (activeShot == remShot) {
			activeShot = null;
		}
	}

	public void setFirstShotActive() {
		if (session == null) {
			return;
		}

		if (!session.getShots().isEmpty()) {
			setActiveShot(session.getShots().get(0));
		} else {
			setActiveShot(null);
		}

	}

	public int getActiveShotIndex() {
		return session.getShots().indexOf(activeShot);
	}

}
