package com.niklim.clicktrace.capture;

import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.SessionsManager;

public class Capture {
	private static final Logger log = LoggerFactory.getLogger(Capture.class);

	@Inject
	private Robot robot;

	@Inject
	private ChangeDetector detector;

	private Timer time;

	private String sessionName;

	public void start() {
		time = new Timer();
		time.schedule(new CaptureTask(), 0, 1000);
	}

	public void stop() {
		time.cancel();
		time = null;
		detector.reset();
	}

	private class CaptureTask extends TimerTask {
		@Override
		public void run() {
			try {
				BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit()
						.getScreenSize()));

				if (detector.detect(image)) {
					SessionsManager.saveImage(image, sessionName);
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
