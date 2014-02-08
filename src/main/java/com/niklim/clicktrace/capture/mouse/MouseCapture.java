package com.niklim.clicktrace.capture.mouse;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.ErrorMsgs;

/**
 * Abstract class for mouse clicks capturing. Initializes JNativeHook.
 */
public abstract class MouseCapture implements NativeMouseInputListener {
	private static Logger log = LoggerFactory.getLogger(MouseCapture.class);

	@Inject
	protected ActiveSession activeSession;

	public MouseCapture() {
		try {
			if (!GlobalScreen.isNativeHookRegistered()) {
				GlobalScreen.registerNativeHook();
				log.info("Native hook registered");
			}
		} catch (NativeHookException e) {
			log.error("Unable to register JNativeHook", e);
			ErrorNotifier.interrupt(ErrorMsgs.UNABLE_TO_REGISTER_NATIVE_HOOK_ERROR);
		}
		GlobalScreen.getInstance().addNativeMouseListener(this);
	}

	public abstract void nativeMouseReleased(NativeMouseEvent e);

	public void nativeMouseClicked(NativeMouseEvent e) {
	}

	public void nativeMousePressed(NativeMouseEvent e) {
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
	}
}
