package com.niklim.clicktrace.model.session;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.inject.Singleton;
import com.niklim.clicktrace.FileManager;

@Singleton
public class SessionManager {

	private static final String PROP_SUFIX_LABEL = ".label";
	private static final String PROP_SUFIX_DESCRIPTION = ".description";

	public List<Session> loadAll() {
		List<Session> sessions = new LinkedList<Session>();

		for (String sessionName : FileManager.loadFileNames(FileManager.SESSIONS_DIR, new FileManager.TrashFilter())) {
			Session session = new Session();
			session.setName(sessionName);

			sessions.add(session);
		}

		return sessions;
	}

	public Session createSession(String sessionName) throws SessionAlreadyExistsException, IOException {
		if (FileManager.sessionExists(sessionName)) {
			throw new SessionAlreadyExistsException();
		}

		FileManager.createSession(sessionName);

		Session session = new Session();
		session.setName(sessionName);

		FileManager.createSessionPropsFile(sessionName);
		return session;

	}

	public void saveShotLabel(Session session, ScreenShot shot) {
		saveSessionProperty(session, shot.getFilename() + PROP_SUFIX_LABEL, shot.getLabel());
	}

	public void saveShotDescription(Session session, ScreenShot shot) {
		saveSessionProperty(session, shot.getFilename() + PROP_SUFIX_DESCRIPTION, shot.getDescription());
	}

	private void saveSessionProperty(Session session, String key, String value) {
		try {
			String propFilePath = FileManager.SESSIONS_DIR + session.getName() + File.separator + FileManager.PROP_FILENAME;
			PropertiesConfiguration prop = new PropertiesConfiguration(propFilePath);
			prop.setProperty(key, value);
			prop.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void changeSessionName(Session session, String newName) throws SessionAlreadyExistsException, IOException {
		if (!FileManager.canCreateSession(newName)) {
			throw new IOException();
		}
		if (FileManager.sessionExists(newName)) {
			throw new SessionAlreadyExistsException();
		}

		FileManager.renameSession(session.getName(), newName);
		session.setName(newName);
	}
}