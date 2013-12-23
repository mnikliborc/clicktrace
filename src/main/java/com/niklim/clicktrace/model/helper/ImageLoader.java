package com.niklim.clicktrace.model.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.service.FileManager;

/**
 * Loads {@link ScreenShot}'s image from the disk.
 */
public class ImageLoader {
	private static final Logger log = LoggerFactory.getLogger(ImageLoader.class);

	public BufferedImage load(ScreenShot shot) {
		try {
			File file = new File(FileManager.SESSIONS_DIR + shot.getSession().getName() + File.separator
					+ shot.getFilename());
			return ImageIO.read(file);
		} catch (IOException e) {
			log.error(ErrorMsgs.SCREENSHOT_LOAD_IMAGE_ERROR, e);
			ErrorNotifier.notify(ErrorMsgs.SCREENSHOT_LOAD_IMAGE_ERROR);
			return null;
		}
	}
}
