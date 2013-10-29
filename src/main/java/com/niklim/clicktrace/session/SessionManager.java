package com.niklim.clicktrace.session;

import java.util.LinkedList;
import java.util.List;

import com.niklim.clicktrace.ImageFileManager;

public class SessionManager {

	public List<Session> load() {
		List<Session> sessions = new LinkedList<Session>();

		for (String sessionName : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR)) {
			Session session = new Session();
			session.setName(sessionName);

			sessions.add(session);
		}

		return sessions;
	}

}
