package com.niklim.clicktrace.model.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionManager;

/**
 * Delete {@link ScreenShot}'s image and properties from the disk.
 */
public class ScreenShotDeleter {
	private static final Logger log = LoggerFactory.getLogger(ScreenShotDeleter.class);

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
			log.error("Unable to delete screenshot image", e);
		}
	}

	private void deleteProperties(ScreenShot shot) {
		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(shot.getSession());
		writer.clearShotProps(shot);
	}
}
