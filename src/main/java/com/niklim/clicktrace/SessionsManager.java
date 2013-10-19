package com.niklim.clicktrace;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.niklim.clicktrace.editor.Editor.TrashFilter;

public class SessionsManager {
	public static final String SESSIONS_DIR = "sessions/";
	public static final String DEFAULT_DIR = "sessions/default/";

	private static Format format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

	static {
		createIfDirNotExists(SESSIONS_DIR);
	}

	public static void saveImage(BufferedImage image, String sessionName) throws IOException {
		String filename = format.format(new Date()) + ".png";
		String filePath = createFilePath(sessionName, filename);

		ImageIO.write(image, "png", new File(filePath));
	}

	private static String createFilePath(String sessionName, String filename) {
		if (sessionName == null || sessionName.trim().equals("")) {
			createIfDirNotExists(DEFAULT_DIR);
			return DEFAULT_DIR + filename;
		} else {
			createIfDirNotExists(SESSIONS_DIR + sessionName);
			return SESSIONS_DIR + sessionName + "/" + filename;
		}
	}

	private static void createIfDirNotExists(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public static void deleteImage(String sessionName, String imageName) {
		String filePath = createFilePath(sessionName, imageName);
		File file = new File(filePath);
		file.delete();
	}

	public static List<String> loadSessions() {
		return loadFiles(SESSIONS_DIR);
	}

	public static List<String> loadSession(String sessionName) {
		return loadFiles(SESSIONS_DIR + sessionName);
	}

	private static List<String> loadFiles(String dirName) {
		List<String> fileNames = new ArrayList<String>();

		File[] files = new File(dirName).listFiles(new TrashFilter());

		for (File sessionDir : files) {
			fileNames.add(sessionDir.getName());
		}

		return fileNames;
	}

}
