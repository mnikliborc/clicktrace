package com.niklim.clicktrace.view.editor.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.view.editor.action.DeleteCurrentSessionActionListener;
import com.niklim.clicktrace.view.editor.action.DeleteSelectedScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.DeselectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.NewSessionActionListener;
import com.niklim.clicktrace.view.editor.action.OpenSessionActionListener;
import com.niklim.clicktrace.view.editor.action.RefreshSessionActionListener;
import com.niklim.clicktrace.view.editor.action.SelectAllScreenShotsActionListener;
import com.niklim.clicktrace.view.editor.action.StartSessionActionListener;
import com.niklim.clicktrace.view.editor.action.StopSessionActionListener;

@Singleton
public class Menu {
	JMenuBar menubar;

	JMenuItem sessionDeleteActiveSession;
	JMenuItem sessionDeleteSelected;
	JMenuItem sessionSelectAll;
	JMenuItem sessionDeselectAll;
	JMenuItem sessionStart;
	JMenuItem sessionStop;
	JMenuItem sessionRefresh;

	@Inject
	private NewSessionActionListener newSessionActionListener;

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

	@Inject
	private OpenSessionActionListener openSessionActionListener;

	public Menu() {

	}

	@Inject
	public void init() {
		menubar = new JMenuBar();

		JMenu file = createFile();
		JMenu session = createSession();
		JMenu tools = createTools();

		menubar.add(file);
		menubar.add(session);
		menubar.add(tools);
	}

	private JMenu createSession() {
		JMenu session = new JMenu("Session");

		sessionStart = createSessionStart();
		sessionStop = createSessionStop();
		sessionRefresh = createSessionRefresh();
		sessionSelectAll = createSessionSelectAll();
		sessionDeselectAll = createSessionDeselectAll();
		sessionDeleteSelected = createSessionDeleteSelected();
		sessionDeleteActiveSession = createSessionDeleteActiveSession();

		session.add(sessionStart);
		session.add(sessionStop);
		session.add(sessionRefresh);
		session.addSeparator();
		session.add(sessionSelectAll);
		session.add(sessionDeselectAll);
		session.add(sessionDeleteSelected);
		session.addSeparator();
		session.add(sessionDeleteActiveSession);
		return session;
	}

	private JMenuItem createSessionStart() {
		JMenuItem menuItem = createMenuItem("Start recording", startSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStop() {
		JMenuItem menuItem = createMenuItem("Stop recording", stopSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionRefresh() {
		JMenuItem menuItem = createMenuItem("Refresh session", Icons.REFRESH, refreshSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionSelectAll() {
		JMenuItem menuItem = createMenuItem("Select all screenshots", selectAllScreenShotsActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeselectAll() {
		JMenuItem menuItem = createMenuItem("Deselect all screenshots", deselectAllScreenShotsActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteSelected() {
		JMenuItem menuItem = createMenuItem("Delete selected screenshots", deleteSelectedScreenShotsActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteActiveSession() {
		JMenuItem menuItem = createMenuItem("Delete current session", Icons.DELETE_SESSION,
				deleteCurrentSessionActionListener);
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenu createTools() {
		JMenu tools = new JMenu("Tools");

		JMenuItem toolsSettings = createToolsSettings();
		tools.add(toolsSettings);
		return tools;
	}

	private JMenuItem createToolsSettings() {
		return createMenuItem("Settings", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	public JMenu createFile() {
		JMenu file = new JMenu("File");

		JMenuItem fileNewSession = createFileNewSession();
		JMenuItem fileOpenSession = createFileOpenSession();
		JMenuItem fileExit = createFileExit();

		file.add(fileNewSession);
		file.add(fileOpenSession);
		file.addSeparator();
		file.add(fileExit);
		return file;
	}

	private JMenuItem createFileNewSession() {
		return createMenuItem("New session", Icons.NEW_SESSION, newSessionActionListener);
	}

	private JMenuItem createFileOpenSession() {
		return createMenuItem("Open session", Icons.OPEN_SESSION, openSessionActionListener);
	}

	private JMenuItem createFileExit() {
		return createMenuItem("Exit", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
	}

	private JMenuItem createMenuItem(String label, ActionListener listener) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(listener);
		return item;
	}

	private JMenuItem createMenuItem(String label, String iconName, ActionListener listener) {
		JMenuItem item = new JMenuItem(label, new ImageIcon(Icons.createIconImage(iconName, label)));
		item.addActionListener(listener);
		return item;
	}

	public JMenuBar getMenu() {
		return menubar;
	}

	public void sessionActive(boolean active) {
		sessionDeleteActiveSession.setEnabled(active);
		sessionDeleteActiveSession.setEnabled(active);
		sessionDeleteSelected.setEnabled(active);
		sessionSelectAll.setEnabled(active);
		sessionDeselectAll.setEnabled(active);
		sessionStart.setEnabled(active);
		sessionStop.setEnabled(active);
		sessionRefresh.setEnabled(active);
	}
}
