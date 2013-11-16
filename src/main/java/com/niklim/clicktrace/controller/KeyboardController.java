package com.niklim.clicktrace.controller;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.view.editor.action.screenshot.DeleteScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.EditScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenScreenShotDescriptionActionListener;
import com.niklim.clicktrace.view.editor.action.session.StartSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.StopSessionActionListener;

/**
 * Warning: this class does not intercept keyboard events. Don't know why.
 * Should work:)
 */
@Singleton
public class KeyboardController implements NativeKeyListener {
	@Inject
	private Controller controller;

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
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
		if (event.getKeyCode() == NativeKeyEvent.VK_R && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			startSessionActionListener.actionPerformed(null);
		} else if (event.getKeyCode() == NativeKeyEvent.VK_S && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			stopSessionActionListener.actionPerformed(null);
		} else if (event.getKeyCode() == NativeKeyEvent.VK_RIGHT && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showLastScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_LEFT && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showFirstScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_RIGHT && !NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showNextScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_LEFT && !NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			controller.showPrevScreenShot();
		} else if (event.getKeyCode() == NativeKeyEvent.VK_E && !NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			editScreenShotActionListener.actionPerformed(null);
		} else if (event.getKeyCode() == NativeKeyEvent.VK_D && !NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl")) {
			openScreenShotDescriptionActionListener.actionPerformed(null);
		} else if (event.getKeyCode() == NativeKeyEvent.VK_DELETE) {
			deleteScreenShotActionListener.actionPerformed(null);
		}
	}
}
