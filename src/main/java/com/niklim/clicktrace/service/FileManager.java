package com.niklim.clicktrace.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.Futures;
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.Files;
import com.niklim.clicktrace.TimeMeter;
import com.niklim.clicktrace.msg.ErrorMsgs;

/**
 * Manages creating folders, properties files and saving/deleting screenshot
 * images.
 */
public class FileManager {
	private static final Logger log = LoggerFactory.getLogger(FileManager.class);

	public static String SESSIONS_DIR = "sessions/";
	public static final String DEFAULT_DIR = "sessions/default/";
	public static final String SESSION_PROPS_FILENAME = "session.properties";

	private static Format format = new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");

	private static FileCompressor compressor = new FileCompressor("jpg", "png");

	public static class TrashFilter implements FilenameFilter {
		@Override
		public boolean accept(File file, String name) {
			return !".".equals(name) && !"..".equals(name) && !".DS_Store".equals(name) && !name.endsWith("~");
		}
	}

	public static class ImageFilter extends TrashFilter {
		@Override
		public boolean accept(File file, String name) {
			return super.accept(file, name) && !SESSION_PROPS_FILENAME.equals(name);
		}
	}

	static {
		createIfDirNotExists(SESSIONS_DIR);
	}

	public Future<String> saveImage(BufferedImage image, String sessionName) throws IOException {
		TimeMeter tm = TimeMeter.start("FileManager.saveImage", log);
		// TODO create FIFO queue with Shots to save and save them
		// asynchronously
		FileCompressor.CompressionResult compressionResult = compressor.getBestCompressed(image);

		File file = findFileToSave(sessionName, compressionResult);
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(compressionResult.stream.toByteArray());
		fop.flush();
		fop.close();

		tm.stop();
		return Futures.immediateFuture(file.getName());
	}

	/**
	 * Creates filepath based on timestamp (sec precision). If file already
	 * exists, then appends 'x' to the timestamp and checks again if exists, and
	 * so on.
	 * 
	 * @param sessionName
	 * @param compressionResult
	 * @return
	 */
	private File findFileToSave(String sessionName, FileCompressor.CompressionResult compressionResult) {
		Date date = new Date();
		String timeStamp = format.format(date);

		File file;
		String postfix = "";
		do {
			String filename = timeStamp + postfix + "." + compressionResult.format;
			String filePath = createFilePath(sessionName, filename);
			file = new File(filePath);
			postfix += "x";
		} while (file.exists());

		return file;
	}

	private String createFilePath(String sessionName, String filename) {
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

	public void deleteImage(String sessionName, String imageName) {
		String filePath = createFilePath(sessionName, imageName);
		File file = new File(filePath);
		file.delete();
	}

	public List<String> loadFileNames(String dirName, FilenameFilter filter) {
		List<String> fileNames = new ArrayList<String>();

		File[] files = new File(dirName).listFiles(filter);

		for (File sessionDir : files) {
			fileNames.add(sessionDir.getName());
		}

		Collections.sort(fileNames);
		return fileNames;
	}

	public boolean createSessionDir(String sessionName) {
		return createIfDirNotExists(SESSIONS_DIR + sessionName);
	}

	public void createSessionPropsFile(String sessionName) {
		File f = new File(SESSIONS_DIR + sessionName + File.separator + SESSION_PROPS_FILENAME);
		try {
			f.createNewFile();
		} catch (IOException e) {
			log.error("Unable to create session properties file", e);
			ErrorNotifier.notify(ErrorMsgs.SESSION_SAVE_PROPS_ERROR);
		}
	}

	public boolean sessionExists(String sessionName) {
		return Files.exists(FileManager.SESSIONS_DIR + sessionName);
	}

	public void renameSession(String oldName, String newName) throws IOException {
		Files.move(FileManager.SESSIONS_DIR + oldName, FileManager.SESSIONS_DIR + newName);
	}

	public void createSessionFolder(String sessionName) throws IOException {
		File newDir = new File(FileManager.SESSIONS_DIR + sessionName);
		if (!newDir.mkdir()) {
			throw new IOException(FileManager.SESSIONS_DIR + sessionName);
		}
	}

	public boolean canCreateSession(String sessionName) {
		File newDir = new File(FileManager.SESSIONS_DIR + sessionName);
		boolean canCreate = newDir.mkdir();
		if (canCreate) {
			newDir.delete();
		}
		return canCreate;
	}
}
