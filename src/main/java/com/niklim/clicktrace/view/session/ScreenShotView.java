package com.niklim.clicktrace.view.session;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.ViewScaling;
import com.niklim.clicktrace.view.MainFrameHolder;

/**
 * Displays screenshot image with mouse clicks.
 */
@Singleton
public class ScreenShotView {
	private static Logger log = LoggerFactory.getLogger(ScreenShotView.class);

	@Inject
	private UserProperties props;

	private JPanel panel;
	private BufferedImage mouseMarkLeft;
	private BufferedImage mouseMarkRight;

	public ScreenShotView() {
		panel = new JPanel(new MigLayout());

		try {
			mouseMarkLeft = loadMouseMark(Icons.MOUSE_MARK_RED_LEFT);
			mouseMarkRight = loadMouseMark(Icons.MOUSE_MARK_RED_RIGHT);
		} catch (Exception e) {
			log.error("Unable to load mouse mark icons", e);
		}
	}

	private BufferedImage loadMouseMark(String icon) throws IOException {
		URL file = Thread.currentThread().getContextClassLoader().getResource(icon);
		return ImageIO.read(file);
	}

	public void show(ScreenShot shot) {
		int frameWidth = (int) (MainFrameHolder.get().getSize().getWidth());
		int frameHeight = (int) (MainFrameHolder.get().getSize().getHeight());
		try {
			BufferedImage imageWithClicks = markClicks(shot.getImage(), shot.getClicks());
			BufferedImage imageFinal = scaleImage(imageWithClicks, frameWidth, frameHeight);
			panel.removeAll();
			panel.add(new ThumbPanel(imageFinal, imageFinal.getWidth(), imageFinal.getHeight()));
		} catch (IOException e) {
			log.error("Unable to scale screenshot image", e);
		}
	}

	private BufferedImage markClicks(BufferedImage image, List<Click> clicks) {
		Graphics2D g = image.createGraphics();
		int clickIndex = 1;
		for (Click click : clicks) {
			drawMark(g, clickIndex, click);
			clickIndex++;
		}
		g.dispose();
		return image;
	}

	private void drawMark(Graphics2D g, int clickIndex, Click click) {
		g.setColor(Color.RED);
		g.drawOval(click.getX() - 15, click.getY() - 15, 30, 30);

		if (click.getButton() == Click.Button.RIGHT) {
			g.drawImage(mouseMarkRight, click.getX() + 10, click.getY() - 25, null);
		} else {
			g.drawImage(mouseMarkLeft, click.getX() + 10, click.getY() - 25, null);
		}

		g.drawChars(String.valueOf(clickIndex).toCharArray(), 0, 1, click.getX() - 15, click.getY() - 15);
	}

	private BufferedImage scaleImage(BufferedImage image, int frameWidth, int frameHeight) throws IOException {
		int targetSize = 0;
		Mode mode;
		if (props.getScreenshotViewScaling() == ViewScaling.HORIZONTAL) {
			targetSize = (int) (frameWidth * 0.97);
			mode = Mode.FIT_TO_WIDTH;
		} else {
			System.out.println(frameHeight);
			targetSize = (int) (frameHeight - 160);
			System.out.println(targetSize);
			mode = Mode.FIT_TO_HEIGHT;
		}

		if (mode == Mode.FIT_TO_WIDTH && image.getWidth() <= targetSize) {
			return image;
		} else if (mode == Mode.FIT_TO_HEIGHT && image.getHeight() <= targetSize) {
			return image;
		} else {
			// BufferedImage scaledImage = Scalr.resize(image, targetWidth);
			BufferedImage scaledImage = Scalr.resize(image, mode, targetSize);
			// BufferedImage scaledImage = Scalr.resize(image, targetWidth,
			// targetHeight);
			return scaledImage;
		}
	}

	public JPanel getPanel() {
		return panel;
	}

	private class ThumbPanel extends JPanel {
		BufferedImage image;

		ThumbPanel(BufferedImage image, int width, int height) {
			setPreferredSize(new Dimension(width, height));
			setBorder(BorderFactory.createLineBorder(new Color(95, 158, 160), 2));
			this.image = image;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);
		}
	}

	public void clear() {
		panel.removeAll();
	}

}
