package com.niklim;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

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

public class Capture {
	public static void main(String[] args) {
		 ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		 scheduler.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				ChangeDetector detector = new ChangeDetector();
				BufferedImage image;
				try {
					Robot robot = new Robot();
					image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					if (detector.detect(image)){
						Date date = new Date();
						String id = date.getHours() + "-" + date.getMinutes() + "-" + date.getSeconds();
						ImageIO.write(image, "png", new File("screenshot"+id+".png"));
					}
				} catch (HeadlessException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			 
		 }, 0, 1, TimeUnit.SECONDS);
	}
}

interface ChangeVoter {
	Vote vote(BufferedImage prev, BufferedImage current);
}

enum Vote {
	SAVE, DISCARD, INDIFFERENT; 
}

class BlindVoter implements ChangeVoter {
	@Override
	public Vote vote(BufferedImage prev, BufferedImage current) {
		return Vote.SAVE;
	}
}

class ChangeDetector {
	List<ChangeVoter> voters = new ArrayList<ChangeVoter>();
	private BufferedImage prevImage;
	
	public boolean detect(BufferedImage currentImage) {
		BufferedImage tempPrevImage = prevImage;
		prevImage = currentImage;
		
		if (tempPrevImage == null) {
			return true;
		}
		
		for (ChangeVoter voter : voters) {
			switch (voter.vote(tempPrevImage, currentImage)) {
				case SAVE: return true;
				case DISCARD: return false;
				default: continue;
			}
		}
		
		return false;
	}
}
