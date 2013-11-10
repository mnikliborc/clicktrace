package com.niklim.clicktrace.view.editor.control.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.view.editor.action.session.DeleteCurrentSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.DeleteSelectedScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.session.DeselectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.session.RefreshSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.SelectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.session.StartSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.StopSessionActionListener;

public class SessionMenu {
	JMenu menu;

	JMenuItem sessionDeleteActiveSession;
	JMenuItem sessionDeleteSelected;
	JMenuItem sessionSelectAll;
	JMenuItem sessionDeselectAll;
	JMenuItem sessionStart;
	JMenuItem sessionStop;
	JMenuItem sessionRefresh;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private DeleteCurrentSessionActionListener deleteCurrentSessionActionListener;

	@Inject
	private StartSessionActionListener startSessionActionListener;

	@Inject
	private StopSessionActionListener stopSessionActionListener;

	@Inject
	private RefreshSessionActionListener refreshSessionActionListener;

	@Inject
	private DeleteSelectedScreenShotsActionListener deleteSelectedScreenShotsActionListener;

	@Inject
	private SelectAllScreenShotsActionListener selectAllScreenShotsActionListener;

	@Inject
	private DeselectAllScreenShotsActionListener deselectAllScreenShotsActionListener;

	public void sessionStateChanged() {
		sessionDeleteActiveSession.setEnabled(activeSession.isSessionOpen());
		sessionDeleteActiveSession.setEnabled(activeSession.isSessionOpen());
		sessionDeleteSelected.setEnabled(activeSession.isSessionOpen());
		sessionSelectAll.setEnabled(activeSession.isSessionOpen());
		sessionDeselectAll.setEnabled(activeSession.isSessionOpen());
		sessionStart.setEnabled(activeSession.isSessionOpen() && !activeSession.getRecording());
		sessionStop.setEnabled(activeSession.isSessionOpen() && activeSession.getRecording());
		sessionRefresh.setEnabled(activeSession.isSessionOpen());
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

		menu.add(sessionStart);
		menu.add(sessionStop);
		menu.add(sessionRefresh);
		menu.addSeparator();
		menu.add(sessionSelectAll);
		menu.add(sessionDeselectAll);
		menu.add(sessionDeleteSelected);
		menu.addSeparator();
		menu.add(sessionDeleteActiveSession);
	}

	public JMenu getMenu() {
		return menu;
	}

	private JMenuItem createSessionStart() {
		JMenuItem menuItem = Menu.createMenuItem("Start recording", Icons.START_SESSION, startSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStop() {
		JMenuItem menuItem = Menu.createMenuItem("Stop recording", Icons.STOP_SESSION, stopSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionRefresh() {
		JMenuItem menuItem = Menu.createMenuItem("Refresh session", Icons.REFRESH_SESSION, refreshSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionSelectAll() {
		JMenuItem menuItem = Menu.createMenuItem("Select all screenshots", selectAllScreenShotsActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeselectAll() {
		JMenuItem menuItem = Menu.createMenuItem("Deselect all screenshots", deselectAllScreenShotsActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteSelected() {
		JMenuItem menuItem = Menu.createMenuItem("Delete selected screenshots", deleteSelectedScreenShotsActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteActiveSession() {
		JMenuItem menuItem = Menu.createMenuItem("Delete current session", Icons.DELETE_SESSION, deleteCurrentSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}
}
