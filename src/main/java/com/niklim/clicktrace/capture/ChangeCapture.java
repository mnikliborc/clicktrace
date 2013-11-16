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
import com.niklim.clicktrace.AppProperties;
import com.niklim.clicktrace.FileManager;
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

	@Inject
	private AppProperties props;

	private Timer time;

	public void start() {
		time = new Timer();
		time.schedule(new CaptureTask(), 0, (int) ((double) 1000 / props.getCaptureFrequency()));
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
				FileManager.saveImage(image, activeSession.getSession().getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
