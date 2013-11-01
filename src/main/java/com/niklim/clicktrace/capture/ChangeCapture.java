package com.niklim.clicktrace.capture;

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
import com.google.inject.Singleton;
import com.niklim.clicktrace.ImageFileManager;
import com.niklim.clicktrace.controller.ActiveSession;

@Singleton
public class ChangeCapture {
	private static final Logger log = LoggerFactory.getLogger(ChangeCapture.class);

	@Inject
	private Robot robot;

	@Inject
	private ChangeDetector detector;

	@Inject
	private ActiveSession activeSession;

	private Timer time;

	public void start() {
		time = new Timer();
		time.schedule(new CaptureTask(), 0, 1000);
	}

	public void stop() {
		if (time != null) {
			time.cancel();
		}
		time = null;
		detector.reset();
	}

	private class CaptureTask extends TimerTask {
		@Override
		public void run() {
			capture();
		}
	}

	public synchronized void capture() {
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

		if (detector.detect(image)) {
			try {
				ImageFileManager.saveImage(image, activeSession.getSessionName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
