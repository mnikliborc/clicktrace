package com.niklim.clicktrace.controller;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.view.editor.Editor;
import com.niklim.clicktrace.view.editor.action.screenshot.DeleteScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.EditScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenScreenShotDescriptionActionListener;
import com.niklim.clicktrace.view.editor.action.session.StartSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.StopSessionActionListener;

@Singleton
public class KeyboardController implements NativeKeyListener {
	private static Logger log = LoggerFactory.getLogger(KeyboardController.class);

	@Inject
	private Controller controller;

	@Inject
	private Editor editor;

	@Inject
	private OpenScreenShotDescriptionActionListener openScreenShotDescriptionActionListener;

	@Inject
	private DeleteScreenShotActionListener deleteScreenShotActionListener;

	@Inject
	private EditScreenShotActionListener editScreenShotActionListener;

	@Inject
	private StartSessionActionListener startSessionActionListener;

	@Inject
	private StopSessionActionListener stopSessionActionListener;

	public KeyboardController() {
		try {
			if (!GlobalScreen.isNativeHookRegistered()) {
				GlobalScreen.registerNativeHook();
			}
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
		GlobalScreen.getInstance().addNativeKeyListener(this);
		log.debug("service instantiated");
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent event) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent event) {
		if (!isEditorActive()) {
			return;
		}
		if (event.getKeyCode() == NativeKeyEvent.VK_RIGHT
				&& NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showLastScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_LEFT
				&& NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showFirstScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_RIGHT
				&& !NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showNextScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_LEFT
				&& !NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showPrevScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_DELETE) {
			deleteScreenShotActionListener.actionPerformed(null);
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
		if (event.getKeyChar() == 'r' && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl+Alt")) {
			startSessionActionListener.actionPerformed(null);
		} else if (event.getKeyChar() == 's'
				&& NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl+Alt")) {
			stopSessionActionListener.actionPerformed(null);
		}

		if (!isEditorActive()) {
			return;
		}
		if (event.getKeyChar() == 'e' && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			editScreenShotActionListener.actionPerformed(null);
		} else if (event.getKeyChar() == 'd' && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			openScreenShotDescriptionActionListener.actionPerformed(null);
		}

	}

	private boolean isEditorActive() {
		return editor.getFrame().isVisible() && editor.getFrame().isActive();
	}
}
