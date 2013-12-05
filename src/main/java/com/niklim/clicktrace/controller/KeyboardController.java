package com.niklim.clicktrace.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
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
import com.niklim.clicktrace.view.editor.ControlShortcutEnum;
import com.niklim.clicktrace.view.editor.Editor;
import com.niklim.clicktrace.view.editor.action.screenshot.ChangeScreenShotLabelActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.DeleteScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.EditScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenScreenShotDescriptionActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenSearchDialogActionListener;
import com.niklim.clicktrace.view.editor.action.screenshot.RefreshScreenShotActionListener;
import com.niklim.clicktrace.view.editor.action.session.DeleteCurrentSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.NewSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.OpenOpenSessionDialogActionListener;
import com.niklim.clicktrace.view.editor.action.session.RefreshSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.SelectAllScreenShotsActionListener;
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

	@Inject
	private OpenSearchDialogActionListener openSearchDialogActionListener;

	@Inject
	private OpenOpenSessionDialogActionListener openSessionActionListener;

	@Inject
	private NewSessionActionListener newSessionActionListener;

	@Inject
	private DeleteCurrentSessionActionListener deleteCurrentSessionActionListener;

	@Inject
	private RefreshSessionActionListener refreshSessionActionListener;

	@Inject
	private RefreshScreenShotActionListener refreshScreenShotActionListener;

	@Inject
	private SelectAllScreenShotsActionListener selectAllScreenShotsActionListener;

	@Inject
	private ChangeScreenShotLabelActionListener changeScreenShotLabelActionListener;

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

	public void registerKeyboardHooks(final JFrame editorFrame) {
		registerAction(editorFrame, ControlShortcutEnum.SCROLL_UP, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.scrollUp();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SCROLL_DOWN, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.scrollDown();
			}
		});

		registerAction(editorFrame, ControlShortcutEnum.SHOT_LAST, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showLastScreenShot();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_FIRST, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showFirstScreenShot();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_NEXT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showNextScreenShot();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_PREV, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showPrevScreenShot();
			}
		});

		registerAction(editorFrame, ControlShortcutEnum.SHOT_DELETE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteScreenShotActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_EDIT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editScreenShotActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_DESCRIPTION, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openScreenShotDescriptionActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_SELECT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.toggleSelectScreenShot();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_REFRESH, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshScreenShotActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_LABEL, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeScreenShotLabelActionListener.actionPerformed(null);
			}
		});

		registerAction(editorFrame, ControlShortcutEnum.FIND, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSearchDialogActionListener.actionPerformed(null);
			}
		});

		registerAction(editorFrame, ControlShortcutEnum.SESSION_REFRESH, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshSessionActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SESSION_OPEN, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openSessionActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SESSION_NEW, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newSessionActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SESSION_DELETE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteCurrentSessionActionListener.actionPerformed(null);
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SESSION_SELECT_ALL_SHOTS,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectAllScreenShotsActionListener.actionPerformed(null);
					}
				});

		registerMenuAction(editorFrame);
	}

	@SuppressWarnings("serial")
	private void registerMenuAction(final JFrame editorFrame) {
		Action menuAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JRootPane rootPane = editorFrame.getRootPane();
				JMenuBar jMenuBar = rootPane.getJMenuBar();
				JMenu menu = jMenuBar.getMenu(0);
				menu.doClick();
			}
		};

		JRootPane rootPane = editorFrame.getRootPane();
		ActionMap actionMap = rootPane.getActionMap();

		final String MENU_ACTION_KEY = "expand_that_first_menu_please";
		actionMap.put(MENU_ACTION_KEY, menuAction);
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true), MENU_ACTION_KEY);
	}

	private void registerAction(JFrame frame, ControlShortcutEnum shortcut, ActionListener listener) {
		frame.getRootPane().registerKeyboardAction(listener,
				KeyStroke.getKeyStroke(shortcut.code, shortcut.modifier),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent event) {
		if (Character.toUpperCase(event.getKeyChar()) == 'R'
				&& keyModifiersMatchText(event, "Shift+Ctrl", "Ctrl+Shift")) {
			startSessionActionListener.actionPerformed(null);
		} else if (Character.toUpperCase(event.getKeyChar()) == 'S'
				&& keyModifiersMatchText(event, "Shift+Ctrl", "Ctrl+Shift")) {
			stopSessionActionListener.actionPerformed(null);
		}
	}

	private boolean keyModifiersMatchText(NativeKeyEvent event, String... texts) {
		for (String text : texts) {
			if (NativeInputEvent.getModifiersText(event.getModifiers()).equals(text)) {
				return true;
			}
		}
		return false;
	}
}
