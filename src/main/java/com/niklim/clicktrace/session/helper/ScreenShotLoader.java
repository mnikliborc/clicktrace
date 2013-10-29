package com.niklim.clicktrace.session.helper;

import java.util.ArrayList;
import java.util.List;

import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.session.ScreenShot;
import com.niklim.clicktrace.session.Session;

public class ScreenShotLoader {
	public List<ScreenShot> load(Session session) {
		List<ScreenShot> shots = new ArrayList<ScreenShot>();
		for (String shotName : ImageFileManager.loadFileNames(ImageFileManager.SESSIONS_DIR + session.getName())) {
			ScreenShot shot = new ScreenShot();
			shot.setName(shotName);
			shot.setSession(session);
			shots.add(shot);
		}
		return shots;
	}
}
