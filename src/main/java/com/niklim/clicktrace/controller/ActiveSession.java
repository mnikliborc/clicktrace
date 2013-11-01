package com.niklim.clicktrace.controller;

import com.google.inject.Singleton;

@Singleton
public class ActiveSession {
	private String sessionName;
	private boolean active = false;

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
