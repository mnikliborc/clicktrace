package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;

public class ScreenShotLoader {
	public List<ScreenShot> load(Session session) {
		PropertiesConfiguration prop = new PropertiesConfiguration();
		try {
			prop = new PropertiesConfiguration(ImageFileManager.SESSIONS_DIR + session.getDirname() + File.separator
					+ ImageFileManager.PROP_FILENAME);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		List<ScreenShot> shots = new ArrayList<ScreenShot>();
		for (String shotFilename : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR + session.getDirname(),
				new ImageFileManager.ImageFilter())) {
			ScreenShot shot = new ScreenShot();
			shot.setFilename(shotFilename);
			shot.setSession(session);
			shot.setLabel(prop.getString(shotFilename + ".label"));
			shot.setDescription(prop.getString(shotFilename + ".description"));
			shots.add(shot);
		}
		return shots;
	}
}
