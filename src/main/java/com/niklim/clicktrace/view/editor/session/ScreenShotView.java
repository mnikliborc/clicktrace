package com.niklim.clicktrace.view.editor.session;

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
import com.niklim.clicktrace.model.session.Click;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.view.editor.Editor;

@Singleton
public class ScreenShotView {
	@Inject
	private Editor editor;

	private JPanel panel;
	private BufferedImage mouseMark;

	public ScreenShotView() {
		panel = new JPanel(new MigLayout());

		try {
			URL file = Thread.currentThread().getContextClassLoader().getResource(Icons.MOUSE_MARK_RED);
			mouseMark = ImageIO.read(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void show(ScreenShot shot) {
		panel.removeAll();

		int thumbWidth = (int) editor.getEditorDimension().getWidth();
		int thumbHeight = (int) editor.getEditorDimension().getHeight();
		try {
			BufferedImage image = scaleImage(markClicks(shot.getImage(), shot.getClicks()), thumbWidth);
			double heightWidthRatio = (double) thumbHeight / thumbWidth;
			panel.add(new ThumbPanel(image, thumbWidth, (int) (thumbWidth * heightWidthRatio)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage markClicks(BufferedImage image, List<Click> clicks) {
		Graphics2D g = image.createGraphics();
		for (Click click : clicks) {
			g.setColor(Color.RED);
			g.drawOval(click.getX() - 15, click.getY() - 15, 30, 30);
			g.drawImage(mouseMark, click.getX() + 10, click.getY() - 25, null);
			g.drawChars(String.valueOf(click.getButton()).toCharArray(), 0, 1, click.getX() - 15, click.getY() - 15);
			g.dispose();
		}
		return image;
	}

	private BufferedImage scaleImage(BufferedImage image, int thumbWidth) throws IOException {
		BufferedImage scaledImage = Scalr.resize(image, thumbWidth);
		return scaledImage;
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
