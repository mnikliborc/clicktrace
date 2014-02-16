package com.niklim.clicktrace.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.dao.SessionPropertiesReader;
import com.niklim.clicktrace.model.dao.SessionPropertiesWriter;
import com.niklim.clicktrace.model.helper.ScreenShotLoader;
import com.niklim.clicktrace.model.helper.SessionDeleter;
import com.niklim.clicktrace.model.helper.SessionMetadataLoader;
import com.niklim.clicktrace.service.exception.SessionAlreadyExistsException;

@Singleton
public class SessionManager {
	public static final String PROP_SUFFIX_LABEL = ".label";
	public static final String PROP_SUFFIX_DESCRIPTION = ".description";
	public static final String PROP_SUFFIX_CLICKS = ".clicks";
	public static final String PROP_SESSION_DESCRIPTION = "session.description";
	public static final String PROP_SESSION_ORDERING = "session.ordering";

	@Inject
	private FileManager fileManager;

	@Inject
	private SessionDeleter deleter;
	@Inject
	private ScreenShotLoader screenShotsLoader;
	@Inject
	private SessionMetadataLoader sessionMetadataHelper;

	/**
	 * Loads all the {@link Session}s found in the default session folder.
	 * 
	 * @return all sessions
	 */
	public List<Session> loadAll() {
		List<Session> sessions = new LinkedList<Session>();

		List<String> filenames = fileManager.loadFileNames(FileManager.SESSIONS_DIR, new FileManager.NoTrashFilter());
		for (String sessionName : filterSessionNames(filenames)) {
			Session session = createSessionInstance();
			session.setName(sessionName);

			SessionPropertiesReader reader = createSessionPropertiesReader(session);
			session.setDescription(reader.getSessionDescription());

			sessions.add(session);
		}

		return sessions;
	}

	private Collection<String> filterSessionNames(List<String> filenames) {
		return Collections2.filter(filenames, new Predicate<String>() {
			public boolean apply(String filename) {
				File sessionDir = new File(FileManager.SESSIONS_DIR + filename);
				return sessionDir.isDirectory();
			}
		});
	}

	private Session createSessionInstance() {
		return new Session(deleter, screenShotsLoader, sessionMetadataHelper);
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

	public Session findSessionByName(String sessionName) {
		for (Session session : loadAll()) {
			if (session.getName().equals(sessionName)) {
				return session;
			}
		}
		return null;
	}
}