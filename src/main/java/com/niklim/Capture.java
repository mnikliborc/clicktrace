package com.niklim;

import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

//A. SCREEN CAPTURE
//1. take screenshot
//2. compare with previous one to determine whether screen changed (use voters which decide, 
//e.g. for cursor move, text input. Detecting mouse/keyboard activity should be helpful.
//3. if change detected - save screenshot
//4. periodically use OCR to index screenshots

//B. CONFIGURE CAPTURE
//1. capture mouse clicks, key pressed?
//2. screen capture frequency
//3. on/off voters
//4. set capture session tag (to automatically annotate shots)

//C. SEARCH ENGINE
//1. search by: text, time, tags
//2. tag, edit (paint like), manipulate (putting in directory structure), compress, publish

public class Capture implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(Capture.class);

	@Inject
	Robot robot;

	@Inject
	ChangeDetector detector;

	public void start() {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
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

	public void saveScreenShot(BufferedImage image) throws IOException {
		log.info("Saving screenshot");
		Date date = new Date();
		String id = date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();
		ImageIO.write(image, "png", new File("screenshot" + id + ".png"));
	}
}
