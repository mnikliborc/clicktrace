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
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.model.helper.SessionPropertiesWriter;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.FileManager;
import com.niklim.clicktrace.service.SessionManager;

/**
 * Screenshots capturing manager. {@link UserProperties} provides configuration.
 */
@Singleton
public class CaptureManager {
	private static final Logger log = LoggerFactory.getLogger(CaptureManager.class);
	// TODO fix memory leaks
	@Inject
	private Robot robot;

	@Inject
	private ChangeDetector detector;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private UserProperties props;

	@Inject
	private FileManager fileManager;

	@Inject
	private SessionManager sessionManager;

	private List<Click> clicks = new LinkedList<Click>();
	private String lastImageFilename;
	private boolean recordMouseClicks;

	private Timer time;

	/**
	 * Starts periodic screenshot capturing.
	 */
	public void start() {
		log.debug("Capturing started");

		recordMouseClicks = props.getRecordMouseClicks();
		time = new Timer();
		int period = (int) ((double) 1000 / props.getCaptureFrequency());
		time.schedule(new CaptureTask(), period, period);
	}

	/**
	 * Stops screenshots capturing.
	 */
	public void stop() {
		if (time != null) {
			log.debug("Capturing stopped");
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

	/**
	 * Takes a screenshot and decides whether it should be saved. On screenshot
	 * save it stores recorded mouse clicks on the previous screenshot.
	 */
	public synchronized void capture() {
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

		if (detector.detect(image)) {
			log.debug("Screen change detected");

			if (lastImageFilename != null) {
				saveClicks();
			}
			try {
				lastImageFilename = fileManager.saveImage(image, activeSession.getSession().getName());
			} catch (IOException e) {
				log.error(ErrorMsgs.SCREENSHOT_SAVE_ERROR, e);
				ErrorNotifier.notify(ErrorMsgs.SCREENSHOT_SAVE_ERROR);
			}
		}
	}

	private void saveClicks() {
		if (!recordMouseClicks) {
			return;
		}

		SessionPropertiesWriter writer = sessionManager.createSessionPropertiesWriter(activeSession.getSession());
		writer.saveShotClicks(lastImageFilename, clicks);
		clicks.clear();
	}

	public void mouseClicked(Click click) {
		log.debug("click added ({},{},{})", click.getX(), click.getY(), click.getButton());
		clicks.add(click);
	}

}
