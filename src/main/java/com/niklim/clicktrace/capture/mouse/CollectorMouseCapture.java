package com.niklim.clicktrace.capture.mouse;

import org.jnativehook.mouse.NativeMouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.ChangeCapture;
import com.niklim.clicktrace.model.session.Click;

@Singleton
public class CollectorMouseCapture extends MouseCapture {
	private static Logger log = LoggerFactory.getLogger(ImmediateMouseCapture.class);

	@Inject
	private ChangeCapture changeCapture;

	public CollectorMouseCapture() {
		log.info("service instantiated");
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent e) {
		if (activeSession.getRecording()) {
			changeCapture.mouseClicked(new Click(e.getX(), e.getY(), e.getButton()));
		}
	}

}
