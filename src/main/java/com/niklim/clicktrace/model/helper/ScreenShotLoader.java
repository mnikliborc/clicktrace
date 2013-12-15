package com.niklim.clicktrace.model.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionManager;

/**
 * Loads {@link ScreenShot} image from the disk.
 */
public class ScreenShotLoader {
	@Inject
	private FileManager fileManager;
	@Inject
	private SessionManager sessionManager;
	@Inject
	private ImageLoader imageLoader;
	@Inject
	private ScreenShotDeleter deleter;

	public List<ScreenShot> load(Session session) {
		SessionPropertiesReader reader = sessionManager.createSessionPropertiesReader(session);

		List<ScreenShot> shots = new ArrayList<ScreenShot>();
		for (String shotFilename : fileManager.loadFileNames(FileManager.SESSIONS_DIR + session.getName(),
				new FileManager.ImageFilter())) {
			ScreenShot shot = new ScreenShot(imageLoader, deleter);
			shot.setFilename(shotFilename);
			shot.setSession(session);

			shot.setLabel(reader.getLabel(shotFilename));
			shot.setDescription(reader.getShotDescription(shotFilename));
			shot.setClicks(reader.getClicks(shotFilename));

			shots.add(shot);
		}
		return shots;
	}
}
