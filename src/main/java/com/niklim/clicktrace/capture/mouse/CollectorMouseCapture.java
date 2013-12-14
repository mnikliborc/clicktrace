package com.niklim.clicktrace.capture.mouse;

import org.jnativehook.mouse.NativeMouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.CaptureManager;
import com.niklim.clicktrace.model.Click;

/**
 * On mouse click notifies {@link CaptureManager}.
 */
@Singleton
public class CollectorMouseCapture extends MouseCapture {
	private static Logger log = LoggerFactory.getLogger(ImmediateMouseCapture.class);

	@Inject
	private CaptureManager capture;

	public CollectorMouseCapture() {
		log.info("service instantiated");
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if (activeSession.isRecording()) {
			capture.mouseClicked(new Click(e.getX(), e.getY(), e.getButton()));
		}
	}

}
