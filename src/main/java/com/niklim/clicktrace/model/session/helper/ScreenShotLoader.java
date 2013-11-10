package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;

public class ScreenShotLoader {
	public List<ScreenShot> load(Session session) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(ImageFileManager.SESSIONS_DIR + session.getName() + File.separator
					+ ImageFileManager.PROP_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<ScreenShot> shots = new ArrayList<ScreenShot>();
		for (String shotFilename : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR + session.getName(),
				new ImageFileManager.ImageFilter())) {
			ScreenShot shot = new ScreenShot();
			shot.setFilename(shotFilename);
			shot.setSession(session);
			shot.setLabel(prop.getProperty(shotFilename + ".label"));
			shot.setDescription(prop.getProperty(shotFilename + ".description"));
			shots.add(shot);
		}
		return shots;
	}
}
