package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionManager;

public class ScreenShotDeleter {
	@Inject
	private SessionManager sessionManager;

	public void delete(ScreenShot shot) {
		deleteImage(shot);
		deleteProperties(shot);
	}

	private void deleteImage(ScreenShot shot) {
		try {
			Path filePath = Paths.get(FileManager.SESSIONS_DIR + shot.getSession().getName() + File.separator
					+ shot.getFilename());
			Files.delete(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteProperties(ScreenShot shot) {
		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(shot.getSession());
		writer.clearShotProps(shot);
	}
}
