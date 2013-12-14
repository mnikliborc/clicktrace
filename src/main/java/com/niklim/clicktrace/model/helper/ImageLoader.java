package com.niklim.clicktrace.model.helper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.service.FileManager;

public class ImageLoader {
	public BufferedImage load(ScreenShot shot) {
		try {
			File file = new File(FileManager.SESSIONS_DIR + shot.getSession().getName() + File.separator
					+ shot.getFilename());
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
