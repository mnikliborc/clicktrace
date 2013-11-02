package com.niklim.clicktrace.controller;

import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.Session;

@Singleton
public class ActiveSession {
	private Session session;
	private boolean active = false;

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
}
