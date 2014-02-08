package com.niklim.clicktrace.capture.mouse;

import org.jnativehook.mouse.NativeMouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.CaptureManager;
import com.niklim.clicktrace.model.Click;

/**
 * On mouse click takes a screenshot, mark on in the click and saves on disk.
 * Not used.
 */
@Singleton
public class ImmediateMouseCapture extends MouseCapture {
	private static Logger log = LoggerFactory.getLogger(ImmediateMouseCapture.class);
	
	@Inject
	private CaptureManager capture;
	
	public ImmediateMouseCapture() {
		log.info("service instantiated");
	}
	
	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if (activeSession.isRecording()) {
			capture.capture(Optional.<Click> of(new Click(e.getX(), e.getY(), e.getButton())));
		}
	}
}
