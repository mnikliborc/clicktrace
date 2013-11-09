package com.niklim.clicktrace.controller;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;

@Singleton
public class ActiveSession {
	private Session session;
	private ScreenShot shot;
	private boolean active = false;
	private boolean recording = false;
	private Set<ScreenShot> selectedShots = new HashSet<ScreenShot>();

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public ScreenShot getShot() {
		return shot;
	}

	public void setShot(ScreenShot shot) {
		this.shot = shot;
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

	public void setSelectedAllShots(boolean selected) {
		selectedShots.clear();
		if (selected) {
			selectedShots.addAll(session.getShots());
		}
	}

	public Set<ScreenShot> getSelectedShots() {
		return selectedShots;
	}

	public void removeShot(ScreenShot remShot) {
		getSession().getShots().remove(remShot);
		deselectShot(remShot);
		if (shot == remShot) {
			shot = null;
		}
	}

	public void setFirstShotActive() {
		if (!session.getShots().isEmpty()) {
			shot = session.getShots().get(0);
		} else {
			shot = null;
		}

	}
}
