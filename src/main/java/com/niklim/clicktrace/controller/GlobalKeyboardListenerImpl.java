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
import com.niklim.clicktrace.controller.operation.session.DeselectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.controller.operation.session.OpenOpenSessionDialogOperation;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.controller.operation.session.SelectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.StartSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StopSessionOperation;
import com.niklim.clicktrace.view.MainView;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

/**
 * Listens to keystrokes and triggers operations on registered shortcuts. Uses
 * JNativeHook.
 */
@Singleton
public class GlobalKeyboardListenerImpl implements GlobalKeyboardListener {
	private static Logger log = LoggerFactory.getLogger(GlobalKeyboardListener.class);

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
	private DeselectAllScreenShotsOperation deselectAllScreenShotsOperation;

	@Inject
	private ChangeScreenShotLabelOperation changeScreenShotLabelOperation;

	public GlobalKeyboardListenerImpl() {
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
		JFrame mainFrame = mainView.getFrame();
		registerAction(mainFrame, OperationsShortcutEnum.SCROLL_UP, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.scrollUp();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SCROLL_DOWN, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainView.scrollDown();
			}
		});

		registerAction(mainFrame, OperationsShortcutEnum.SHOT_LAST, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showLastScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_FIRST, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showFirstScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_NEXT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showNextScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_PREV, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.showPrevScreenShot();
			}
		});

		registerAction(mainFrame, OperationsShortcutEnum.SHOT_DELETE, deleteScreenShotOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_EDIT, editScreenShotOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_DESCRIPTION, openScreenShotDescriptionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_SELECT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.toggleSelectScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_REFRESH, refreshScreenShotOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_LABEL, changeScreenShotLabelOperation.action());

		registerAction(mainFrame, OperationsShortcutEnum.FIND, openSearchDialogOperation.action());

		registerAction(mainFrame, OperationsShortcutEnum.SESSION_REFRESH, refreshSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_OPEN, openSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_NEW, newSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_DELETE, deleteCurrentSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_SELECT_ALL_SHOTS,
				selectAllScreenShotsOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_DESELECT_ALL_SHOTS,
				deselectAllScreenShotsOperation.action());

		registerMenuAction(mainFrame);
	}

	/**
	 * Makes pressing Alt key to open File Menu in the GUI.
	 * 
	 * @param mainFrame
	 */
	@SuppressWarnings("serial")
	private void registerMenuAction(final JFrame mainFrame) {
		Action menuAction = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JRootPane rootPane = mainFrame.getRootPane();
				JMenuBar jMenuBar = rootPane.getJMenuBar();
				JMenu menu = jMenuBar.getMenu(0);
				menu.doClick();
			}
		};

		JRootPane rootPane = mainFrame.getRootPane();
		ActionMap actionMap = rootPane.getActionMap();

		final String MENU_ACTION_KEY = "expand_that_first_menu_please";
		actionMap.put(MENU_ACTION_KEY, menuAction);
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ALT, 0, true), MENU_ACTION_KEY);
	}

	private void registerAction(JFrame frame, OperationsShortcutEnum shortcut, ActionListener listener) {
		frame.getRootPane().registerKeyboardAction(listener, KeyStroke.getKeyStroke(shortcut.code, shortcut.modifier),
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

