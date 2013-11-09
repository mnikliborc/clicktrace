package com.niklim.clicktrace.model.session;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Singleton;
import com.niklim.clicktrace.ImageFileManager;

@Singleton
public class SessionManager {

	public List<Session> loadAll() {
		List<Session> sessions = new LinkedList<Session>();

		for (String sessionName : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR)) {
			Session session = new Session();
			session.setName(sessionName);

			sessions.add(session);
		}

		return sessions;
	}

	public Session createSession(String sessionName) throws SessionAlreadyExistsException {
		Session session = new Session();
		session.setName(sessionName);
		if (ImageFileManager.createSessionDir(sessionName)) {
			return session;
		} else {
			throw new SessionAlreadyExistsException();
		}
	}

}