package com.niklim.clicktrace.capture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

public class ImgManager {
	public static final String SESSIONS_DIR = "sessions/";
	public static final String DEFAULT_DIR = "sessions/default/";

	public ImgManager() {
		createIfDirNotExists(SESSIONS_DIR);
	}

	protected void saveScreenShot(BufferedImage image, String sessionName) throws IOException {
		Date date = new Date();
		String filePath = date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();

		if (sessionName == null || sessionName.trim().equals("")) {
			filePath = DEFAULT_DIR + filePath;
			createIfDirNotExists(DEFAULT_DIR);
		} else {
			filePath = SESSIONS_DIR + sessionName + "/" + filePath;
			createIfDirNotExists(SESSIONS_DIR + sessionName);
		}
		ImageIO.write(image, "png", new File(filePath + ".png"));
	}

	private void createIfDirNotExists(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}
}
