package com.niklim.clicktrace.capture;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.TimeMeter;
import com.niklim.clicktrace.capture.voter.LineVoter;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.ImageSaver;
import com.niklim.clicktrace.service.ScreenShotUtils;

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
	private ImageSaver imageSaver;

	private List<Click> clicks = new LinkedList<Click>();
	private BufferedImage lastImage;

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

		detector.setVoter(new LineVoter(props.getCaptureSensitivity()));
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

		if (lastImage != null) {
			drawClicks(lastImage);
			imageSaver.save(lastImage, activeSession.getSession().getName());
			lastImage = null;
			waitForImageSaver();
		}
	}

	/**
	 * Wait 1 sec for ImageSaver to save all pending images. Brute solution, but
	 * effective.
	 */
	private void waitForImageSaver() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			log.error("", e);
		}
	}

	private class CaptureTask extends TimerTask {
		@Override
		public void run() {
			capture(Optional.<Click> absent());
		}
	}

	/**
	 * Takes a screenshot and decides whether it should be saved. On screenshot
	 * save it stores recorded mouse clicks on the last screenshot.
	 */
	public synchronized void capture(Optional<Click> clickOpt) {
		TimeMeter tm = TimeMeter.start("CaptureManager.capture", log);

		Rectangle captureRect = getCaptureRectangle();
		TimeMeter tmRobot = TimeMeter.start("CaptureManager.robot", log);
		BufferedImage image = robot.createScreenCapture(captureRect);
		tmRobot.stop();

		if (detector.detect(lastImage, image)) {
			log.debug("Screen change detected");

			if (clickOpt.isPresent()) {
				clicks.add(clickOpt.get());
			}

			if (lastImage != null) {
				drawClicks(lastImage);
				imageSaver.save(lastImage, activeSession.getSession().getName());
			}

			lastImage = image;
		} else if (clickOpt.isPresent()) {
			clicks.add(clickOpt.get());
		}

		tm.stop();
	}

	private void drawClicks(BufferedImage image) {
		if (captureMouseClicks) {
			ScreenShotUtils.markClicks(image, clicks);
		}
		clicks.clear();
	}

	private Rectangle getCaptureRectangle() {
		if (screenshotRect != null) {
			return screenshotRect;
		} else {
			Dimension screenSize = ScreenUtils.getPrimarySize();
			return new Rectangle(0, 0, screenSize.width, screenSize.height);
		}
	}

}
