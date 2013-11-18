package com.niklim.clicktrace.model.session.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.common.base.Strings;
import com.niklim.clicktrace.FileManager;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.ScreenShot.Click;
import com.niklim.clicktrace.model.session.Session;

public class ScreenShotLoader {
	public List<ScreenShot> load(Session session) {
		PropertiesConfiguration prop = new PropertiesConfiguration();
		try {
			prop = new PropertiesConfiguration(FileManager.SESSIONS_DIR + session.getName() + File.separator
					+ FileManager.SESSION_PROPS_FILENAME);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}

		List<ScreenShot> shots = new ArrayList<ScreenShot>();
		for (String shotFilename : FileManager.loadFileNames(FileManager.SESSIONS_DIR + session.getName(), new FileManager.ImageFilter())) {
			ScreenShot shot = new ScreenShot();
			shot.setFilename(shotFilename);
			shot.setSession(session);
			shot.setLabel(prop.getString(shotFilename + ".label"));
			shot.setDescription(prop.getString(shotFilename + ".description"));

			String clicks = Strings.nullToEmpty(prop.getString(shotFilename + ".clicks"));
			shot.setClicks(Click.getList(clicks));

			shots.add(shot);
		}
		return shots;
	}
}
