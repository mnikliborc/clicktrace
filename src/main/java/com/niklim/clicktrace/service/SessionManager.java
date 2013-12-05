package com.niklim.clicktrace.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.helper.ScreenShotLoader;
import com.niklim.clicktrace.model.session.helper.SessionDeleter;
import com.niklim.clicktrace.model.session.helper.SessionMetadataHelper;
import com.niklim.clicktrace.model.session.helper.SessionPropertiesReader;
import com.niklim.clicktrace.model.session.helper.SessionPropertiesWriter;
import com.niklim.clicktrace.model.session.helper.SessionSaver;

@Singleton
public class SessionManager {
	public static final String PROP_SUFFIX_LABEL = ".label";
	public static final String PROP_SUFFIX_DESCRIPTION = ".description";
	public static final String PROP_SUFFIX_CLICKS = ".clicks";

	@Inject
	private FileManager fileManager;

	@Inject
	private SessionSaver saver;
	@Inject
	private SessionDeleter deleter;
	@Inject
	private ScreenShotLoader screenShotsLoader;
	@Inject
	private SessionMetadataHelper sessionMetadataHelper;

	public List<Session> loadAll() {
		List<Session> sessions = new LinkedList<Session>();

		for (String sessionName : fileManager.loadFileNames(FileManager.SESSIONS_DIR, new FileManager.TrashFilter())) {
			Session session = createSessionInstance();
			session.setName(sessionName);

			sessions.add(session);
		}

		return sessions;
	}

	private Session createSessionInstance() {
		return new Session(saver, deleter, screenShotsLoader, sessionMetadataHelper);
	}

	public Session createSession(String sessionName) throws SessionAlreadyExistsException, IOException {
		if (fileManager.sessionExists(sessionName)) {
			throw new SessionAlreadyExistsException();
		}

		fileManager.createSessionFolder(sessionName);

		Session session = createSessionInstance();
		session.setName(sessionName);

		fileManager.createSessionPropsFile(sessionName);
		return session;

	}

	public SessionPropertiesReader createSessionPropertiesReader(Session session) {
		return new SessionPropertiesReader(session);
	}

	public SessionPropertiesWriter createSessionPropertiesWriter(Session session) {
		return new SessionPropertiesWriter(session);
	}

	public void changeSessionName(Session session, String newName) throws SessionAlreadyExistsException, IOException {
		if (!fileManager.canCreateSession(newName)) {
			throw new IOException();
		}
		if (fileManager.sessionExists(newName)) {
			throw new SessionAlreadyExistsException();
		}

		fileManager.renameSession(session.getName(), newName);
		session.setName(newName);
	}
}