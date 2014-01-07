package com.niklim.clicktrace.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.model.Click;

public class ScreenShotUtils {
	private static Logger log = LoggerFactory.getLogger(ScreenShotUtils.class);

	private static BufferedImage mouseMarkLeft;
	private static BufferedImage mouseMarkRight;

	static {
		try {
			mouseMarkLeft = loadMouseMark(Icons.MOUSE_MARK_RED_LEFT);
			mouseMarkRight = loadMouseMark(Icons.MOUSE_MARK_RED_RIGHT);
		} catch (Exception e) {
			log.error("Unable to load mouse mark icons", e);
		}
	}

	private static BufferedImage loadMouseMark(String icon) throws IOException {
		URL file = Thread.currentThread().getContextClassLoader().getResource(icon);
		return ImageIO.read(file);
	}

	public static BufferedImage markClicks(BufferedImage image, List<Click> clicks) {
		Graphics2D g = image.createGraphics();
		int clickIndex = 1;
		for (Click click : clicks) {
			drawMark(g, clickIndex, click);
			clickIndex++;
		}
		g.dispose();
		return image;
	}

	private static void drawMark(Graphics2D g, int clickIndex, Click click) {
		g.setColor(Color.RED);
		g.drawOval(click.getX() - 15, click.getY() - 15, 30, 30);

		if (click.getButton() == Click.Button.RIGHT) {
			g.drawImage(mouseMarkRight, click.getX() + 10, click.getY() - 25, null);
		} else {
			g.drawImage(mouseMarkLeft, click.getX() + 10, click.getY() - 25, null);
		}

		g.drawChars(String.valueOf(clickIndex).toCharArray(), 0, 1, click.getX() - 15, click.getY() - 15);
	}
}
