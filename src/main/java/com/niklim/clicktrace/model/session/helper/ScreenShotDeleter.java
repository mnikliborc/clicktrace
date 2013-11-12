package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.model.session.ScreenShot;

public class ScreenShotDeleter {
	public void delete(ScreenShot shot) {
		// TODO delete screenshot properties
		try {
			Path filePath = Paths.get(ImageFileManager.SESSIONS_DIR + shot.getSession().getDirname() + File.separator + shot.getFilename());
			Files.delete(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
