package com.niklim.clicktrace.view.session;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.ViewScaling;
import com.niklim.clicktrace.service.ScreenShotUtils;
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
	private ThumbPanel thumbPanel;
	
	public ScreenShotView() {
		panel = new JPanel(new MigLayout());
	}
	
	public void show(ScreenShot shot) {
		int frameWidth = (int) (MainFrameHolder.get().getSize().getWidth());
		int frameHeight = (int) (MainFrameHolder.get().getSize().getHeight());
		try {
			// backward compatibility - we mark clicks on screen capture
			BufferedImage imageWithClicks = ScreenShotUtils.markClicks(shot.getImage(),
					shot.getClicks());
			
			BufferedImage imageFinal = scaleImage(imageWithClicks, frameWidth, frameHeight);
			
			// should not leak, nevertheless
			if (thumbPanel != null) {
				thumbPanel.image = null;
			}
			panel.removeAll();
			
			thumbPanel = new ThumbPanel(imageFinal, imageFinal.getWidth(), imageFinal.getHeight());
			panel.add(thumbPanel);
		} catch (IOException e) {
			log.error("Unable to scale screenshot image", e);
		}
	}
	
	private BufferedImage scaleImage(BufferedImage image, int frameWidth, int frameHeight)
			throws IOException {
		int targetSize = 0;
		Mode mode;
		if (props.getScreenshotViewScaling() == ViewScaling.HORIZONTAL) {
			targetSize = (int) (frameWidth * 0.97);
			mode = Mode.FIT_TO_WIDTH;
		} else {
			targetSize = frameHeight - 160;
			mode = Mode.FIT_TO_HEIGHT;
		}
		
		if (mode == Mode.FIT_TO_WIDTH && image.getWidth() <= targetSize) {
			return image;
		} else if (mode == Mode.FIT_TO_HEIGHT && image.getHeight() <= targetSize) {
			return image;
		} else {
			BufferedImage scaledImage = Scalr.resize(image, mode, targetSize, Scalr.OP_ANTIALIAS);
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
