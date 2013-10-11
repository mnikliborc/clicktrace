package com.niklim.clicktrace.capture;

import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class Capture extends TimerTask {
	private static final Logger log = LoggerFactory.getLogger(Capture.class);

	@Inject
	Robot robot;

	@Inject
	ChangeDetector detector;

	private Timer time;

	private String sessionName;

	public void start() {
		time = new Timer();
		time.schedule(this, 0, 1000);
	}

	public void stop() {
		time.cancel();
		time = null;
		detector.reset();
	}

	@Override
	public void run() {
		try {
			BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

			if (detector.detect(image)) {
				saveScreenShot(image);
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void saveScreenShot(BufferedImage image) throws IOException {
		Date date = new Date();
		String filePath = date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();

		if (sessionName == null || sessionName.trim().equals("")) {
			filePath = "default/" + filePath;
			createIfDirNotExist("default");
		} else {
			filePath = sessionName + "/" + filePath;
			createIfDirNotExist(sessionName);
		}
		ImageIO.write(image, "png", new File(filePath + ".png"));
	}

	private void createIfDirNotExist(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
