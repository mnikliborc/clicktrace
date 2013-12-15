package com.niklim.clicktrace.view.control.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.session.ChangeSessionDescriptionOperation;
import com.niklim.clicktrace.controller.operation.session.ChangeSessionNameOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteCurrentSessionOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteSelectedScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.DeselectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.controller.operation.session.SelectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.StartSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StopSessionOperation;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

public class SessionMenu {
	JMenu menu;

	JMenuItem sessionDeleteActiveSession;
	JMenuItem sessionDeleteSelected;
	JMenuItem sessionSelectAll;
	JMenuItem sessionDeselectAll;
	JMenuItem sessionStart;
	JMenuItem sessionStop;
	JMenuItem sessionChangeLabel;
	JMenuItem sessionRefresh;
	JMenuItem sessionDescription;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private DeleteCurrentSessionOperation deleteCurrentSessionOperation;

	@Inject
	private StartSessionOperation startSessionOperation;

	@Inject
	private StopSessionOperation stopSessionOperation;

	@Inject
	private RefreshSessionOperation refreshSessionOperation;

	@Inject
	private DeleteSelectedScreenShotsOperation deleteSelectedScreenShotsOperation;

	@Inject
	private SelectAllScreenShotsOperation selectAllScreenShotsOperation;

	@Inject
	private DeselectAllScreenShotsOperation deselectAllScreenShotsOperation;

	@Inject
	private ChangeSessionNameOperation changeSessionNameOperation;

	@Inject
	private ChangeSessionDescriptionOperation changeSessionDescriptionOperation;

	public void sessionStateChanged() {
		sessionDeleteActiveSession.setEnabled(activeSession.isSessionLoaded());
		sessionDeleteActiveSession.setEnabled(activeSession.isSessionLoaded());
		sessionDeleteSelected.setEnabled(activeSession.isSessionLoaded());
		sessionSelectAll.setEnabled(activeSession.isSessionLoaded());
		sessionDeselectAll.setEnabled(activeSession.isSessionLoaded());
		sessionStart.setEnabled(!activeSession.isRecording());
		sessionStop.setEnabled(activeSession.isSessionLoaded() && activeSession.isRecording());
		sessionRefresh.setEnabled(activeSession.isSessionLoaded());
		sessionChangeLabel.setEnabled(activeSession.isSessionLoaded());
		sessionDescription.setEnabled(activeSession.isSessionLoaded());
	}

	@Inject
	public void init() {
		menu = new JMenu("Session");

		sessionStart = createSessionStart();
		sessionStop = createSessionStop();
		sessionRefresh = createSessionRefresh();
		sessionSelectAll = createSessionSelectAll();
		sessionDeselectAll = createSessionDeselectAll();
		sessionDeleteSelected = createSessionDeleteSelected();
		sessionDeleteActiveSession = createSessionDeleteActiveSession();
		sessionChangeLabel = createSessionChangeLabel();
		sessionDescription = createSessionDescription();

		menu.add(sessionStart);
		menu.add(sessionStop);
		menu.add(sessionRefresh);
		menu.addSeparator();
		menu.add(sessionSelectAll);
		menu.add(sessionDeselectAll);
		menu.add(sessionDeleteSelected);
		menu.addSeparator();
		menu.add(sessionChangeLabel);
		menu.add(sessionDescription);
		menu.addSeparator();
		menu.add(sessionDeleteActiveSession);
	}

	private JMenuItem createSessionDescription() {
		JMenuItem menuItem = MenuBar.createMenuItem("Change description", Icons.DESCRIPTION_SESSION,
				OperationsShortcutEnum.SESSION_DESCRIPTION,
				changeSessionDescriptionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	public JMenu getMenu() {
		return menu;
	}

	private JMenuItem createSessionChangeLabel() {
		JMenuItem menuItem = MenuBar.createMenuItem("Change name", null, changeSessionNameOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStart() {
		JMenuItem menuItem = MenuBar.createMenuItem("Start recording", Icons.START_SESSION,
				OperationsShortcutEnum.RECORD_START,
				startSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStop() {
		JMenuItem menuItem = MenuBar.createMenuItem("Stop recording",
				Icons.STOP_SESSION, OperationsShortcutEnum.RECORD_STOP,
 stopSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionRefresh() {
		JMenuItem menuItem = MenuBar.createMenuItem("Refresh session", Icons.REFRESH_SESSION,
				OperationsShortcutEnum.SESSION_REFRESH,
				refreshSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionSelectAll() {
		JMenuItem menuItem = MenuBar.createMenuItem("Select all screenshots",
				OperationsShortcutEnum.SESSION_SELECT_ALL_SHOTS,
 selectAllScreenShotsOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeselectAll() {
		JMenuItem menuItem = MenuBar.createMenuItem("Deselect all screenshots",
				OperationsShortcutEnum.SESSION_DESELECT_ALL_SHOTS,
				deselectAllScreenShotsOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteSelected() {
		JMenuItem menuItem = MenuBar.createMenuItem("Delete selected screenshots", null,
				deleteSelectedScreenShotsOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteActiveSession() {
		JMenuItem menuItem = MenuBar.createMenuItem("Delete current session", Icons.DELETE_SESSION,
				OperationsShortcutEnum.SESSION_DELETE,
				deleteCurrentSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}
}
