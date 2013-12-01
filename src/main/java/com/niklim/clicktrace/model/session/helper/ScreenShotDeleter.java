package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.niklim.clicktrace.FileManager;
import com.niklim.clicktrace.model.session.ScreenShot;

public class ScreenShotDeleter {
	public void delete(ScreenShot shot) {
		deleteImage(shot);
		deleteProperties(shot);
	}

	private void deleteProperties(ScreenShot shot) {
		// TODO refactor session properties handling, at the moment it is spread
		// across the codebase
		String propFilePath = FileManager.SESSIONS_DIR + shot.getSession().getName() + File.separator
				+ FileManager.SESSION_PROPS_FILENAME;
		try {
			PropertiesConfiguration prop = new PropertiesConfiguration(propFilePath);
			prop.clearProperty(shot.getFilename() + ".clicks");
			prop.clearProperty(shot.getFilename() + ".description");
			prop.clearProperty(shot.getFilename() + ".label");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
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
}
