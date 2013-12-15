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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.view.MainView;

/**
 * Displays screenshot image with mouse clicks.
 */
@Singleton
public class ScreenShotView {
	@Inject
	private MainView mainView;

	private JPanel panel;
	private BufferedImage mouseMarkLeft;
	private BufferedImage mouseMarkRight;

	public ScreenShotView() {
		panel = new JPanel(new MigLayout());

		try {
			mouseMarkLeft = loadMouseMark(Icons.MOUSE_MARK_RED_LEFT);
			mouseMarkRight = loadMouseMark(Icons.MOUSE_MARK_RED_RIGHT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BufferedImage loadMouseMark(String icon) throws IOException {
		URL file = Thread.currentThread().getContextClassLoader().getResource(icon);
		return ImageIO.read(file);
	}

	public void show(ScreenShot shot) {
		int thumbWidth = (int) (mainView.getEditorDimension().getWidth() * 0.97);
		try {
			BufferedImage imageWithClicks = markClicks(shot.getImage(), shot.getClicks());
			BufferedImage imageFinal = scaleImage(imageWithClicks, thumbWidth);
			panel.removeAll();
			panel.add(new ThumbPanel(imageFinal, imageFinal.getWidth(), imageFinal.getHeight()));
		} catch (IOException e) {
			e.printStackTrace();
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

		g.drawChars(String.valueOf(clickIndex).toCharArray(), 0, 1, click.getX() - 15,
				click.getY() - 15);
	}

	private BufferedImage scaleImage(BufferedImage image, int thumbWidth) throws IOException {
		if (image.getWidth() <= thumbWidth) {
			return image;
		} else {
			BufferedImage scaledImage = Scalr.resize(image, thumbWidth);
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
