package com.niklim.clicktrace.model.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.model.dao.SessionPropertiesReader;
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

		List<String> filenames = fileManager.loadFileNames(FileManager.SESSIONS_DIR + session.getName(),
				new FileManager.ImageFilter());

		Optional<Map<String, Integer>> ordering = reader.getOrdering();
		List<ScreenShot> shots;

		if (ordering.isPresent()) {
			shots = loadOrdered(session, reader, filenames, ordering);
		} else {
			shots = loadUnordered(session, reader, filenames);
		}

		return shots;
	}

	private List<ScreenShot> loadUnordered(Session session, SessionPropertiesReader reader, List<String> filenames) {
		List<ScreenShot> shots;
		shots = Lists.newArrayList();
		for (String shotFilename : filenames) {
			ScreenShot shot = loadShot(session, reader, shotFilename);
			shots.add(shot);
		}
		return shots;
	}

	private List<ScreenShot> loadOrdered(Session session, SessionPropertiesReader reader, List<String> filenames,
			Optional<Map<String, Integer>> ordering) {
		List<ScreenShot> shots = new ArrayList<ScreenShot>(Lists.newArrayList(new ScreenShot[ordering.get().size()]));

		// there might be new shots, which have not been ordered yet - store
		// them in 'unordered'
		List<ScreenShot> unordered = Lists.newArrayList();
		for (String shotFilename : filenames) {
			ScreenShot shot = loadShot(session, reader, shotFilename);

			Integer index = ordering.get().get(shotFilename);
			if (index != null) {
				shots.set(index, shot);
			} else {
				unordered.add(shot);
			}
		}

		// shots might have been deleted, but still in ordering
		shots = Lists.newArrayList(Collections2.filter(shots, Predicates.notNull()));
		shots.addAll(unordered);
		return shots;
	}

	private ScreenShot loadShot(Session session, SessionPropertiesReader reader, String shotFilename) {
		ScreenShot shot = new ScreenShot(imageLoader, deleter);
		shot.setFilename(shotFilename);
		shot.setSession(session);

		shot.setLabel(reader.getLabel(shotFilename));
		shot.setDescription(reader.getShotDescription(shotFilename));
		shot.setClicks(reader.getClicks(shotFilename));
		return shot;
	}
}
