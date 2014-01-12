package com.niklim.clicktrace.capture;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
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
	private static final int CAPTURE_PERIOD_MS = 1000;

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

	private Timer time;

	private boolean captureMouseClicks;
	private Rectangle screenshotRect;

	/**
	 * Starts periodic screenshot capturing.
	 */
	public void start() {
		log.info("Capturing started");

		configure();

		time = new Timer();
		time.schedule(new CaptureTask(), CAPTURE_PERIOD_MS, CAPTURE_PERIOD_MS);
	}

	private void configure() {
		captureMouseClicks = props.getCaptureMouseClicks();
		if (props.getCaptureFullScreen()) {
			screenshotRect = null;
		} else {
			screenshotRect = props.getCaptureRectangle();
		}
	}

	/**
	 * Stops screenshots capturing.
	 */
	public void stop() {
		if (time != null) {
			log.info("Capturing stopped");
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
		Rectangle captureRect = getCaptureRectangle();
		BufferedImage image = robot.createScreenCapture(captureRect);

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

	private Rectangle getCaptureRectangle() {
		if (screenshotRect != null) {
			return screenshotRect;
		} else {
			Dimension screenSize = ScreenUtils.getPrimarySize();
			return new Rectangle(0, 0, screenSize.width, screenSize.height);
		}
	}

	private void saveClicks() {
		if (!captureMouseClicks) {
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
