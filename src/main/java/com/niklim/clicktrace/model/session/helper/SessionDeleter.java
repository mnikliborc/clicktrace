package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.model.session.Session;

public class SessionDeleter {
	public void delete(Session delete) {
		for (String filename : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR + delete.getName())) {
			try {
				Path filePath = Paths.get(ImageFileManager.SESSIONS_DIR + delete.getName() + File.separator + filename);
				Files.delete(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Path dirPath = Paths.get(ImageFileManager.SESSIONS_DIR + delete.getName());
			Files.delete(dirPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
