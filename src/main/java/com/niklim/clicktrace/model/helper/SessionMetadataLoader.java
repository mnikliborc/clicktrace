package com.niklim.clicktrace.model.helper;

import java.io.File;
import java.util.Date;

import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.SessionMetadata;
import com.niklim.clicktrace.service.FileManager;

/**
 * Loads {@link SessionMetada} from the disk.
 */
public class SessionMetadataLoader {
	public SessionMetadata loadMetadata(Session session) {
		int size = session.getShots().size();

		File file = new File(FileManager.SESSIONS_DIR + session.getName());
		file.lastModified();
		return new SessionMetadata(new Date(file.lastModified()), size);
	}
}
