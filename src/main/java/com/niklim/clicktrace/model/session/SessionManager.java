package com.niklim.clicktrace.model.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.google.inject.Singleton;
import com.niklim.clicktrace.ImageFileManager;

@Singleton
public class SessionManager {

	private static final String PROP_SUFIX_LABEL = ".label";
	private static final String PROP_SESSION_LABEL = "session.label";
	private static final String PROP_SUFIX_DESCRIPTION = ".description";

	public List<Session> loadAll() {
		List<Session> sessions = new LinkedList<Session>();

		for (String sessionName : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR, new ImageFileManager.TrashFilter())) {
			Session session = new Session();
			session.setDirname(sessionName);

			session.setLabel(loadSessionProperty(session).getProperty(PROP_SESSION_LABEL));

			sessions.add(session);
		}

		return sessions;
	}

	public Session createSession(String sessionName) throws SessionAlreadyExistsException {
		Session session = new Session();
		session.setDirname(sessionName);
		if (!ImageFileManager.createSessionDir(sessionName)) {
			throw new SessionAlreadyExistsException();
		}

		ImageFileManager.createSessionPropsFile(sessionName);
		return session;
	}

	public void saveSessionLabel(Session session) {
		saveSessionProperty(session, PROP_SESSION_LABEL, session.getLabel());
	}

	public void saveShotLabel(Session session, ScreenShot shot) {
		saveSessionProperty(session, shot.getFilename() + PROP_SUFIX_LABEL, shot.getLabel());
	}

	public void saveShotDescription(Session session, ScreenShot shot) {
		saveSessionProperty(session, shot.getFilename() + PROP_SUFIX_DESCRIPTION, shot.getDescription());
	}

	private Properties loadSessionProperty(Session session) {
		String propFilePath = ImageFileManager.SESSIONS_DIR + session.getDirname() + File.separator + ImageFileManager.PROP_FILENAME;
		Properties prop = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(propFilePath);
			prop.load(fileInputStream);
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	private void saveSessionProperty(Session session, String key, String value) {
		String propFilePath = ImageFileManager.SESSIONS_DIR + session.getDirname() + File.separator + ImageFileManager.PROP_FILENAME;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(propFilePath));

			prop.setProperty(key, value);

			FileOutputStream fileOutputStream = new FileOutputStream(propFilePath);
			prop.store(fileOutputStream, null);
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}