package com.niklim.clicktrace.controller;

import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.Session;

@Singleton
public class ActiveSession {
	private Session session;
	private boolean active = false;
	private boolean recording = false;

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
}
