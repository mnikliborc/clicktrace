package com.niklim.clicktrace.capture.mouse;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jnativehook.mouse.NativeMouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.service.FileManager;

/**
 * On mouse click takes a screenshot, mark on in the click and saves on disk.
 * Not used.
 */
@Singleton
public class ImmediateMouseCapture extends MouseCapture {
	private static Logger log = LoggerFactory.getLogger(ImmediateMouseCapture.class);

	@Inject
	private Robot robot;

	@Inject
	private FileManager fileManager;

	public ImmediateMouseCapture() {
		log.info("service instantiated");
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if (activeSession.getRecording()) {
			capture(e);
		}
	}

	private void capture(NativeMouseEvent e) {
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

		drawClick(image, e.getX(), e.getY(), e.getButton());
		try {
			fileManager.saveImage(image, activeSession.getSession().getName());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void drawClick(BufferedImage image, int mouseX, int mouseY, int button) {
		Graphics2D g = image.createGraphics();
		g.setColor(Color.RED);
		g.drawOval(mouseX - 15, mouseY - 15, 30, 30);
		g.drawChars(String.valueOf(button).toCharArray(), 0, 1, mouseX - 15, mouseY - 15);
		g.dispose();
	}
}
