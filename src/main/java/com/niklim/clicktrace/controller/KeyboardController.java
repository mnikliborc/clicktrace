package com.niklim.clicktrace.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

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
	}

	public void registerKeyboardHooks(JFrame editorFrame) {
		registerAction(editorFrame, KeyEvent.VK_UP, 0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.scrollUp();
			}
		});
		registerAction(editorFrame, KeyEvent.VK_DOWN, 0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.scrollDown();
			}
		});

		registerAction(editorFrame, KeyEvent.VK_RIGHT, KeyEvent.ALT_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showLastScreenShot();
			}
		});
		registerAction(editorFrame, KeyEvent.VK_LEFT, KeyEvent.ALT_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showFirstScreenShot();
			}
		});
		registerAction(editorFrame, KeyEvent.VK_RIGHT, KeyEvent.CTRL_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showNextScreenShot();
			}
		});
		registerAction(editorFrame, KeyEvent.VK_LEFT, KeyEvent.CTRL_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showPrevScreenShot();
			}
		});

		registerAction(editorFrame, KeyEvent.VK_DELETE, 0, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteScreenShotActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editScreenShotActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openScreenShotDescriptionActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.toggleSelectScreenShot();
			}
		});
	}

	private void registerAction(JFrame frame, int key, int modifier, ActionListener listener) {
		frame.getRootPane().registerKeyboardAction(listener, KeyStroke.getKeyStroke(key, modifier),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
		if (event.getKeyChar() == 'r' && NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl+Alt")) {
			startSessionActionListener.actionPerformed(null);
		} else if (event.getKeyChar() == 's'
				&& NativeInputEvent.getModifiersText(event.getModifiers()).equals("Ctrl+Alt")) {
			stopSessionActionListener.actionPerformed(null);
		}
	}
}
