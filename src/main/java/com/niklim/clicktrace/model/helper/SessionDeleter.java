package com.niklim.clicktrace.model.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.service.FileManager;

/**
 * Deletes {@link Session} from the disk.
 */
public class SessionDeleter {
	@Inject
	private FileManager fileManager;

	public void delete(Session delete) {
		for (String filename : fileManager.loadFileNames(FileManager.SESSIONS_DIR + delete.getName(),
				new FileManager.TrashFilter())) {
			try {
				Path filePath = Paths.get(FileManager.SESSIONS_DIR + delete.getName() + File.separator + filename);
				Files.delete(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Path dirPath = Paths.get(FileManager.SESSIONS_DIR + delete.getName());
			Files.delete(dirPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
