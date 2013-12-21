package com.niklim.clicktrace.model.helper;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.Files;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.service.FileManager;

/**
 * Deletes {@link Session} from the disk.
 */
public class SessionDeleter {
	private static final Logger log = LoggerFactory.getLogger(SessionDeleter.class);

	@Inject
	private FileManager fileManager;

	public void delete(Session delete) {
		String dirPath = FileManager.SESSIONS_DIR + delete.getName();
		for (String filename : fileManager.loadFileNames(dirPath, new FileManager.TrashFilter())) {
			try {
				String filePath = dirPath + File.separator + filename;
				Files.delete(filePath);
			} catch (IOException e) {
				log.error("Unable to delete screenshot image", e);
			}
		}
		try {
			Files.delete(dirPath);
		} catch (IOException e) {
			log.error("Unable to delete session folder", e);
		}
	}
}
