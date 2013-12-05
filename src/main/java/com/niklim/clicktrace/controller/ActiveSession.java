package com.niklim.clicktrace.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.dialog.ActiveShotListener;

@Singleton
public class ActiveSession {
	private Session session;
	private ScreenShot activeShot;
	private boolean recording = false;
	private Set<ScreenShot> selectedShots = new HashSet<ScreenShot>();

	private List<ActiveShotListener> activeShotListeners = new ArrayList<ActiveShotListener>();

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean isSessionOpen() {
		return session != null;
	}

	public boolean getRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public ScreenShot getActiveShot() {
		return activeShot;
	}

	public void setActiveShot(ScreenShot shot) {
		if (activeShot != shot) {
			for (ActiveShotListener l : activeShotListeners) {
				l.shotChanged(activeShot);
			}
		}
		activeShot = shot;
	}

	public boolean isActiveShotOpen() {
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

	public void registerActiveShotListener(ActiveShotListener l) {
		activeShotListeners.add(l);
	}

	public void unregisterActiveShotListener(ActiveShotListener l) {
		activeShotListeners.remove(l);
	}

}
