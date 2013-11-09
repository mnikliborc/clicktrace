package com.niklim.clicktrace.view.editor.session;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.imgscalr.Scalr;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.view.editor.Editor;

@Singleton
public class ScreenShotView {
	@Inject
	private Editor editor;

	private JPanel panel;

	public ScreenShotView() {
		panel = new JPanel(new MigLayout());
	}

	public void show(ScreenShot shot) {
		panel.removeAll();

		int thumbWidth = (int) editor.getEditorDimension().getWidth();
		int thumbHeight = (int) editor.getEditorDimension().getHeight();
		try {
			BufferedImage image = scaleImage(shot.getImage(), thumbWidth);
			double heightWidthRatio = (double) thumbHeight / thumbWidth;
			panel.add(new ThumbPanel(image, thumbWidth, (int) (thumbWidth * heightWidthRatio)));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
