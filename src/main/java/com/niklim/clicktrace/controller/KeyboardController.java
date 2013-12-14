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
import com.niklim.clicktrace.controller.operation.screenshot.ChangeScreenShotLabelOperation;
import com.niklim.clicktrace.controller.operation.screenshot.DeleteScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.EditScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.OpenScreenShotDescriptionOperation;
import com.niklim.clicktrace.controller.operation.screenshot.OpenSearchDialogOperation;
import com.niklim.clicktrace.controller.operation.screenshot.RefreshScreenShotOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteCurrentSessionOperation;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.controller.operation.session.OpenOpenSessionDialogOperation;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.controller.operation.session.SelectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.StartSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StopSessionOperation;
import com.niklim.clicktrace.view.ControlShortcutEnum;
import com.niklim.clicktrace.view.MainView;

@Singleton
public class KeyboardController implements NativeKeyListener {
	private static Logger log = LoggerFactory.getLogger(KeyboardController.class);

	@Inject
	private MainController controller;

	@Inject
	private MainView mainView;

	@Inject
	private OpenScreenShotDescriptionOperation openScreenShotDescriptionOperation;

	@Inject
	private DeleteScreenShotOperation deleteScreenShotOperation;

	@Inject
	private EditScreenShotOperation editScreenShotOperation;

	@Inject
	private StartSessionOperation startSessionOperation;

	@Inject
	private StopSessionOperation stopSessionOperation;

	@Inject
	private OpenSearchDialogOperation openSearchDialogOperation;

	@Inject
	private OpenOpenSessionDialogOperation openSessionOperation;

	@Inject
	private NewSessionOperation newSessionOperation;

	@Inject
	private DeleteCurrentSessionOperation deleteCurrentSessionOperation;

	@Inject
	private RefreshSessionOperation refreshSessionOperation;

	@Inject
	private RefreshScreenShotOperation refreshScreenShotOperation;

	@Inject
	private SelectAllScreenShotsOperation selectAllScreenShotsOperation;

	@Inject
	private ChangeScreenShotLabelOperation changeScreenShotLabelOperation;

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

	@Inject
	public void registerKeyboardHooks() {
		JFrame editorFrame = mainView.getFrame();
		registerAction(editorFrame, ControlShortcutEnum.SCROLL_UP, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.scrollUp();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SCROLL_DOWN, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.scrollDown();
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

		registerAction(editorFrame, ControlShortcutEnum.SHOT_DELETE, deleteScreenShotOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SHOT_EDIT, editScreenShotOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SHOT_DESCRIPTION,
				openScreenShotDescriptionOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SHOT_SELECT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.toggleSelectScreenShot();
			}
		});
		registerAction(editorFrame, ControlShortcutEnum.SHOT_REFRESH, refreshScreenShotOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SHOT_LABEL, changeScreenShotLabelOperation.action());

		registerAction(editorFrame, ControlShortcutEnum.FIND, openSearchDialogOperation.action());

		registerAction(editorFrame, ControlShortcutEnum.SESSION_REFRESH, refreshSessionOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SESSION_OPEN, openSessionOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SESSION_NEW, newSessionOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SESSION_DELETE, deleteCurrentSessionOperation.action());
		registerAction(editorFrame, ControlShortcutEnum.SESSION_SELECT_ALL_SHOTS,
				selectAllScreenShotsOperation.action());

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
			startSessionOperation.perform();
		} else if (Character.toUpperCase(event.getKeyChar()) == 'S'
				&& keyModifiersMatchText(event, "Shift+Ctrl", "Ctrl+Shift")) {
			stopSessionOperation.perform();
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
