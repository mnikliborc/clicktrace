package com.niklim.clicktrace.view;

import javax.swing.JFrame;

/**
 * Provides app's main {@link JFrame}. Used to avoid passing the frame all over
 * the code.
 */
public class MainFrameHolder {
	private static JFrame frame;

	public static void set(JFrame frame) {
		MainFrameHolder.frame = frame;
	}

	public static JFrame get() {
		return frame;
	}
}
