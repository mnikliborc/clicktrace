package com.niklim.clicktrace.controller.hook;

import java.awt.Toolkit;
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
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.OSUtils;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.NavigationController;
import com.niklim.clicktrace.controller.operation.screenshot.ChangeScreenShotDescriptionOperation;
import com.niklim.clicktrace.controller.operation.screenshot.ChangeScreenShotLabelOperation;
import com.niklim.clicktrace.controller.operation.screenshot.DeleteScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.EditScreenShotOperation;
import com.niklim.clicktrace.controller.operation.screenshot.RefreshScreenShotOperation;
import com.niklim.clicktrace.controller.operation.session.ChangeSessionDescriptionOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteActiveSessionOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteSelectedScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.DeselectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.HtmlExportOperation;
import com.niklim.clicktrace.controller.operation.session.JiraExportOperation;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.controller.operation.session.OpenSearchDialogOperation;
import com.niklim.clicktrace.controller.operation.session.OpenSessionOperation;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.controller.operation.session.ReorderOperation;
import com.niklim.clicktrace.controller.operation.session.SelectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.StartRecordingOperation;
import com.niklim.clicktrace.controller.operation.session.StopRecordingOperation;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.view.MainView;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

/**
 * Listens to keystrokes and triggers operations on registered shortcuts. Uses
 * JNativeHook.
 */
@Singleton
public class GlobalKeyboardListenerImpl implements GlobalKeyboardListener {
	private static final int MAC_SHIFT_CTRL_MODIFIER = 3;

	private static Logger log = LoggerFactory.getLogger(GlobalKeyboardListener.class);

	@Inject
	private MainController mainController;

	@Inject
	private NavigationController navigationController;

	@Inject
	private MainView mainView;

	@Inject
	private ChangeScreenShotDescriptionOperation changeScreenShotDescriptionOperation;

	@Inject
	private DeleteScreenShotOperation deleteScreenShotOperation;

	@Inject
	private EditScreenShotOperation editScreenShotOperation;

	@Inject
	private StartRecordingOperation startRecordingOperation;

	@Inject
	private StopRecordingOperation stopRecordingOperation;

	@Inject
	private OpenSearchDialogOperation openSearchDialogOperation;

	@Inject
	private OpenSessionOperation openSessionOperation;

	@Inject
	private NewSessionOperation newSessionOperation;

	@Inject
	private DeleteActiveSessionOperation deleteActiveSessionOperation;

	@Inject
	private RefreshSessionOperation refreshSessionOperation;

	@Inject
	private RefreshScreenShotOperation refreshScreenShotOperation;

	@Inject
	private SelectAllScreenShotsOperation selectAllScreenShotsOperation;

	@Inject
	private DeselectAllScreenShotsOperation deselectAllScreenShotsOperation;

	@Inject
	private DeleteSelectedScreenShotsOperation deleteSelectedShotsOperation;

	@Inject
	private ChangeScreenShotLabelOperation changeScreenShotLabelOperation;

	@Inject
	private ChangeSessionDescriptionOperation changeSessionDescriptionOperation;

	@Inject
	private ReorderOperation reorderOperation;

	@Inject
	private JiraExportOperation jiraExportOperation;

	@Inject
	private HtmlExportOperation htmlExportOperation;

	public GlobalKeyboardListenerImpl() {
		try {
			if (!GlobalScreen.isNativeHookRegistered()) {
				GlobalScreen.registerNativeHook();
			}
		} catch (NativeHookException e) {
			log.error(ErrorMsgs.UNABLE_TO_REGISTER_NATIVE_HOOK_ERROR, e);
			ErrorNotifier.interrupt(ErrorMsgs.UNABLE_TO_REGISTER_NATIVE_HOOK_ERROR);
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

		if (!OSUtils.isOnMac()) {
			registerNavigationOnWindowsLinux(mainFrame);
		} else {
			registerNavigationOnMac(mainFrame);
		}

		registerAction(mainFrame, OperationsShortcutEnum.SHOT_DELETE, deleteScreenShotOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_EDIT, editScreenShotOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_DESCRIPTION,
				changeScreenShotDescriptionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_SELECT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainController.toggleSelectScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_REFRESH, refreshScreenShotOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_LABEL, changeScreenShotLabelOperation.action());

		registerAction(mainFrame, OperationsShortcutEnum.FIND, openSearchDialogOperation.action());

		registerAction(mainFrame, OperationsShortcutEnum.SESSION_REFRESH, refreshSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_OPEN, openSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_NEW, newSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_DELETE, deleteActiveSessionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_DELETE_SELECTED, deleteSelectedShotsOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_DESCRIPTION,
				changeSessionDescriptionOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_REORDER, reorderOperation.action());

		registerAction(mainFrame, OperationsShortcutEnum.SESSION_SELECT_ALL_SHOTS,
				selectAllScreenShotsOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.SESSION_DESELECT_ALL_SHOTS,
				deselectAllScreenShotsOperation.action());

		registerAction(mainFrame, OperationsShortcutEnum.JIRA_EXPORT, jiraExportOperation.action());
		registerAction(mainFrame, OperationsShortcutEnum.HTML_EXPORT, htmlExportOperation.action());

		registerMenuAction(mainFrame);
	}

	private void registerNavigationOnWindowsLinux(JFrame mainFrame) {
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_LAST, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navigationController.showLastScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_FIRST, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navigationController.showFirstScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_NEXT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navigationController.showNextScreenShot();
			}
		});
		registerAction(mainFrame, OperationsShortcutEnum.SHOT_PREV, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				navigationController.showPrevScreenShot();
			}
		});
	}

	private void registerNavigationOnMac(JFrame frame) {
		// TODO correct shortcut tooltips for prev/next screenshot navigation
		frame.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navigationController.showLastScreenShot();
			}
		}, KeyStroke.getKeyStroke("shift RIGHT"), JComponent.WHEN_IN_FOCUSED_WINDOW);
		frame.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navigationController.showFirstScreenShot();
			}
		}, KeyStroke.getKeyStroke("shift LEFT"), JComponent.WHEN_IN_FOCUSED_WINDOW);
		frame.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navigationController.showPrevScreenShot();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		frame.getRootPane().registerKeyboardAction(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				navigationController.showNextScreenShot();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
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
				&& (keyModifiersMatchText(event, "Shift+Ctrl", "Ctrl+Shift") || isCrtlShiftOnMac(event))) {
			startRecordingOperation.perform();
		} else if (Character.toUpperCase(event.getKeyChar()) == 'S'
				&& (keyModifiersMatchText(event, "Shift+Ctrl", "Ctrl+Shift") || isCrtlShiftOnMac(event))) {
			stopRecordingOperation.perform();
		}
	}

	/**
	 * OSX has different Modifiers value for Shift+Ctrl than Windows/Linux.
	 * 
	 * @param event
	 * @return true if Shift+Ctrl was pressed
	 */
	private boolean isCrtlShiftOnMac(NativeKeyEvent event) {
		return OSUtils.isOnMac() && event.getModifiers() == MAC_SHIFT_CTRL_MODIFIER;
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
