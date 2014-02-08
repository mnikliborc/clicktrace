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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.FileManager;
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
	private FileManager fileManager;
	
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
		time.schedule(new CaptureTask(), 500, CAPTURE_PERIOD_MS);
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
		
		if (lastImage != null) {
			try {
				drawClicks(lastImage);
				fileManager.saveImage(lastImage, activeSession.getSession().getName());
			} catch (IOException e) {
				log.error(ErrorMsgs.SCREENSHOT_SAVE_ERROR, e);
				ErrorNotifier.notify(ErrorMsgs.SCREENSHOT_SAVE_ERROR);
			}
			lastImage = null;
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
		System.out.println(Thread.currentThread());
		if (clickOpt.isPresent()) {
			System.out.println(clickOpt.get());
		}
		Rectangle captureRect = getCaptureRectangle();
		BufferedImage image = robot.createScreenCapture(captureRect);
		
		long currentTimeMillis = System.currentTimeMillis();
		if (detector.detect(lastImage, image)) {
			log.debug("Screen change detected");
			
			try {
				if (clickOpt.isPresent()) {
					clicks.add(clickOpt.get());
				}
				
				if (lastImage != null) {
					drawClicks(lastImage);
					Future<String> fn = fileManager.saveImage(lastImage, activeSession.getSession()
							.getName());
					try {
						System.out.println("save=" + fn.get());
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
				
				lastImage = image;
			} catch (IOException e) {
				log.error(ErrorMsgs.SCREENSHOT_SAVE_ERROR, e);
				ErrorNotifier.notify(ErrorMsgs.SCREENSHOT_SAVE_ERROR);
			}
		} else if (clickOpt.isPresent()) {
			clicks.add(clickOpt.get());
		}
		System.out.println("captTime=" + (System.currentTimeMillis() - currentTimeMillis));
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
