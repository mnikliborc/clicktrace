package com.niklim.clicktrace;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class FileManager {
	public static String SESSIONS_DIR = "sessions/";
	public static final String DEFAULT_DIR = "sessions/default/";
	public static final String SESSION_PROPS_FILENAME = ".prop.txt";

	private static Format format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

	public static class TrashFilter implements FilenameFilter {
		@Override
		public boolean accept(File file, String name) {
			return !".".equals(name) && !"..".equals(name);
		}
	}

	public static class ImageFilter implements FilenameFilter {
		@Override
		public boolean accept(File file, String name) {
			return !".".equals(name) && !"..".equals(name) && !SESSION_PROPS_FILENAME.equals(name)
					&& !name.endsWith("~");
		}
	}

	static {
		createIfDirNotExists(SESSIONS_DIR);
	}

	public static String saveImage(BufferedImage image, String sessionName) throws IOException {
		String filename = format.format(new Date()) + ".png";
		String filePath = createFilePath(sessionName, filename);

		ImageIO.write(image, "png", new File(filePath));
		return filename;
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

	private static boolean createIfDirNotExists(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdir();
			return true;
		} else {
			return false;
		}
	}

	public static void deleteImage(String sessionName, String imageName) {
		String filePath = createFilePath(sessionName, imageName);
		File file = new File(filePath);
		file.delete();
	}

	public static List<String> loadFileNames(String dirName, FilenameFilter filter) {
		List<String> fileNames = new ArrayList<String>();

		File[] files = new File(dirName).listFiles(filter);

		for (File sessionDir : files) {
			fileNames.add(sessionDir.getName());
		}

		Collections.sort(fileNames);
		return fileNames;
	}

	public static boolean createSessionDir(String sessionName) {
		return createIfDirNotExists(SESSIONS_DIR + sessionName);
	}

	public static void createSessionPropsFile(String sessionName) {
		File f = new File(SESSIONS_DIR + sessionName + File.separator + SESSION_PROPS_FILENAME);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean sessionExists(String sessionName) {
		return Files.exists(Paths.get(FileManager.SESSIONS_DIR + sessionName));
	}

	public static void renameSession(String oldName, String newName) throws IOException {
		Files.move(Paths.get(FileManager.SESSIONS_DIR + oldName), Paths.get(FileManager.SESSIONS_DIR + newName));
	}

	public static void createSession(String sessionName) throws IOException {
		File newDir = new File(FileManager.SESSIONS_DIR + sessionName);
		if (!newDir.mkdir()) {
			throw new IOException(FileManager.SESSIONS_DIR + sessionName);
		}
	}

	public static boolean canCreateSession(String sessionName) {
		File newDir = new File(FileManager.SESSIONS_DIR + sessionName);
		boolean canCreate = newDir.mkdir();
		if (canCreate) {
			newDir.delete();
		}
		return canCreate;
	}

	public static PropertiesConfiguration loadSessionProperties(String sessionName) {
		PropertiesConfiguration sessionProps;
		try {
			sessionProps = new PropertiesConfiguration(FileManager.SESSIONS_DIR + sessionName + File.separator
					+ SESSION_PROPS_FILENAME);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			sessionProps = new PropertiesConfiguration();
		}
		return sessionProps;
	}
}
