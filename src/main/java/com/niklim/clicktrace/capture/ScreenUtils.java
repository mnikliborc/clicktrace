package com.niklim.clicktrace.capture;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

public class ScreenUtils {
	public static Dimension getPrimarySize() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int width = ge.getDefaultScreenDevice().getDisplayMode().getWidth();
		int height = ge.getDefaultScreenDevice().getDisplayMode().getHeight();
		return new Dimension(width, height);
	}
}
