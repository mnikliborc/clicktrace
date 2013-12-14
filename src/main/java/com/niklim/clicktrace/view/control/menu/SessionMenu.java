package com.niklim.clicktrace.view.control.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.session.ChangeSessionNameOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteCurrentSessionOperation;
import com.niklim.clicktrace.controller.operation.session.DeleteSelectedScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.DeselectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.RefreshSessionOperation;
import com.niklim.clicktrace.controller.operation.session.SelectAllScreenShotsOperation;
import com.niklim.clicktrace.controller.operation.session.StartSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StopSessionOperation;

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

		menu.add(sessionStart);
		menu.add(sessionStop);
		menu.add(sessionRefresh);
		menu.addSeparator();
		menu.add(sessionSelectAll);
		menu.add(sessionDeselectAll);
		menu.add(sessionDeleteSelected);
		menu.addSeparator();
		menu.add(sessionChangeLabel);
		menu.add(sessionDeleteActiveSession);
	}

	public JMenu getMenu() {
		return menu;
	}

	private JMenuItem createSessionChangeLabel() {
		JMenuItem menuItem = MenuBar.createMenuItem("Change name", changeSessionNameOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStart() {
		JMenuItem menuItem = MenuBar.createMenuItem("Start recording", Icons.START_SESSION,
				startSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStop() {
		JMenuItem menuItem = MenuBar.createMenuItem("Stop recording", Icons.STOP_SESSION,
 stopSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionRefresh() {
		JMenuItem menuItem = MenuBar.createMenuItem("Refresh session", Icons.REFRESH_SESSION,
				refreshSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionSelectAll() {
		JMenuItem menuItem = MenuBar.createMenuItem("Select all screenshots",
 selectAllScreenShotsOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeselectAll() {
		JMenuItem menuItem = MenuBar.createMenuItem("Deselect all screenshots",
				deselectAllScreenShotsOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteSelected() {
		JMenuItem menuItem = MenuBar.createMenuItem("Delete selected screenshots",
				deleteSelectedScreenShotsOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteActiveSession() {
		JMenuItem menuItem = MenuBar.createMenuItem("Delete current session", Icons.DELETE_SESSION,
				deleteCurrentSessionOperation.action());
		menuItem.setEnabled(false);
		return menuItem;
	}
}
