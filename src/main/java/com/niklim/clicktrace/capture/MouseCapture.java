package com.niklim.clicktrace.capture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.FileManager;
import com.niklim.clicktrace.controller.ActiveSession;

@Singleton
public class MouseCapture implements NativeMouseInputListener {
	private static final Logger log = LoggerFactory.getLogger(MouseCapture.class);

	@Inject
	private Robot robot;

	@Inject
	private ActiveSession activeSession;

	public MouseCapture() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		// Add the appropriate listeners for the example object.
		GlobalScreen.getInstance().addNativeMouseListener(this);
	}

	private void capture(NativeMouseEvent e) {
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

		drawClick(image, e.getX(), e.getY(), e.getButton());
		try {
			FileManager.saveImage(image, activeSession.getSession().getName());
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

	public void nativeMouseReleased(NativeMouseEvent e) {
		if (activeSession.getRecording()) {
			log.debug("Mouse released (x = {}, y = {}, b = {}", e.getX(), e.getY(), e.getButton());
			capture(e);
		}
	}

	public void nativeMouseClicked(NativeMouseEvent e) {
	}

	public void nativeMousePressed(NativeMouseEvent e) {
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
	}
}
