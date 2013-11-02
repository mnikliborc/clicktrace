package com.niklim.clicktrace.view.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;

@Singleton
public class Menu {
	JMenuBar menubar;

	JMenuItem sessionDeleteActiveSession;

	@Inject
	private MenuController controller;

	@Inject
	private OpenSessionDialog openSessionDialog;

	public Menu() {
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

		JMenuItem sessionStart = createSessionStart();
		JMenuItem sessionStop = createSessionStop();
		JMenuItem sessionSelectAll = createSessionSelectAll();
		JMenuItem sessionDeleteSelected = createSessionDeleteSelected();
		sessionDeleteActiveSession = createSessionDeleteActiveSession();
		sessionDeleteActiveSession.setEnabled(false);

		session.add(sessionStart);
		session.add(sessionStop);
		session.add(sessionSelectAll);
		session.add(sessionDeleteSelected);
		session.addSeparator();
		session.add(sessionDeleteActiveSession);
		return session;
	}

	private JMenuItem createSessionStart() {
		return createMenuItem("Start recording", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createSessionStop() {
		return createMenuItem("Stop recording", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createSessionSelectAll() {
		return createMenuItem("Select all screenshots", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createSessionDeleteSelected() {
		return createMenuItem("Delete selected screenshots", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createSessionDeleteActiveSession() {
		return createMenuItem("Delete current session", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
			}
		});
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
		return createMenuItem("New session", Icons.NEW_SESSION, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String name = JOptionPane.showInputDialog("Set session name");
				if (name != null) {
					controller.newSession(name);
				}
			}
		});
	}

	private JMenuItem createFileOpenSession() {
		return createMenuItem("Open session", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				openSessionDialog.open();
			}
		});
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

	public void sessionActivated() {
		sessionDeleteActiveSession.setEnabled(true);
	}
}
