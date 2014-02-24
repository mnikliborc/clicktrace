package com.niklim.clicktrace.model.helper;

import java.io.File;
import java.util.Date;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.SessionMetadata;
import com.niklim.clicktrace.props.UserProperties;

/**
 * Loads {@link SessionMetada} from the disk.
 */
public class SessionMetadataLoader {
	@Inject
	private UserProperties props;

	public SessionMetadata loadMetadata(Session session) {
		int size = session.getShots().size();

		File file = new File(props.getSessionsDirPath() + session.getName());
		file.lastModified();
		return new SessionMetadata(new Date(file.lastModified()), size);
	}
}
