package com.niklim.clicktrace.capture;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.AppProperties;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.session.Click;
import com.niklim.clicktrace.model.session.helper.SessionPropertiesWriter;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionManager;

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

	@Inject
	private FileManager fileManager;

	@Inject
	private SessionManager sessionManager;

	private List<Click> clicks = new LinkedList<Click>();
	private String lastImageFilename;
	private boolean recordMouseClicks;

	private Timer time;

	public void start() {
		recordMouseClicks = props.getRecordMouseClicks();
		time = new Timer();
		int period = (int) ((double) 1000 / props.getCaptureFrequency());
		time.schedule(new CaptureTask(), period, period);
	}

	public void stop() {
		if (time != null) {
			time.cancel();
		}
		time = null;
		detector.reset();

		if (lastImageFilename != null) {
			saveClicks();
			lastImageFilename = null;
		}
	}

	private class CaptureTask extends TimerTask {
		@Override
		public void run() {
			capture();
		}
	}

	public synchronized void capture() {
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit()
				.getScreenSize()));

		if (detector.detect(image)) {
			if (lastImageFilename != null) {
				saveClicks();
			}
			try {
				lastImageFilename = fileManager.saveImage(image, activeSession.getSession()
						.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveClicks() {
		if (!recordMouseClicks) {
			return;
		}

		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(activeSession
				.getSession());
		writer.saveShotClicks(lastImageFilename, clicks);
		clicks.clear();
	}

	public void mouseClicked(Click click) {
		log.debug("click added ({},{},{})", click.getX(), click.getY(), click.getButton());
		clicks.add(click);
	}

}
