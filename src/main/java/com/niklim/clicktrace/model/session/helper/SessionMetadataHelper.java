package com.niklim.clicktrace.model.session.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionMetadata;
import com.niklim.clicktrace.service.FileManager;

public class SessionMetadataHelper {

	public SessionMetadata loadMetadata(Session session) {
		int size = session.getShots().size();
		try {
			Path sessionDir = Paths.get(FileManager.SESSIONS_DIR + session.getName());
			BasicFileAttributes attr = Files.readAttributes(sessionDir, BasicFileAttributes.class);
			return new SessionMetadata(new Date(attr.creationTime().toMillis()), new Date(attr.lastModifiedTime()
					.toMillis()), size);
		} catch (IOException e) {
			e.printStackTrace();
			return new SessionMetadata(new Date(), new Date(), size);
		}
	}
}
