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

	public List<Session> loadAll() {
		List<Session> sessions = new LinkedList<Session>();

		for (String sessionName : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR, new ImageFileManager.TrashFilter())) {
			Session session = new Session();
			session.setName(sessionName);

			sessions.add(session);
		}

		return sessions;
	}

	public Session createSession(String sessionName) throws SessionAlreadyExistsException {
		Session session = new Session();
		session.setName(sessionName);
		if (!ImageFileManager.createSessionDir(sessionName)) {
			throw new SessionAlreadyExistsException();
		}

		ImageFileManager.createSessionPropsFile(sessionName);
		return session;
	}

	public void saveShotLabel(Session session, ScreenShot shot) {
		saveSessionProperty(session, shot.getFilename() + ".label", shot.getLabel());
	}

	public void saveShotDescription(Session session, ScreenShot shot) {
		saveSessionProperty(session, shot.getFilename() + ".description", shot.getDescription());
	}

	private void saveSessionProperty(Session session, String key, String value) {
		String propFilePath = ImageFileManager.SESSIONS_DIR + session.getName() + File.separator + ImageFileManager.PROP_FILENAME;
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(propFilePath));

			prop.setProperty(key, value);

			prop.store(new FileOutputStream(propFilePath), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}