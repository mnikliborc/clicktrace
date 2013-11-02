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
	JMenuItem sessionDeleteSelected;
	JMenuItem sessionSelectAll;
	JMenuItem sessionDeselectAll;
	JMenuItem sessionStart;
	JMenuItem sessionStop;

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

		sessionStart = createSessionStart();
		sessionStop = createSessionStop();
		sessionSelectAll = createSessionSelectAll();
		sessionDeselectAll = createSessionDeselectAll();
		sessionDeleteSelected = createSessionDeleteSelected();
		sessionDeleteActiveSession = createSessionDeleteActiveSession();

		session.add(sessionStart);
		session.add(sessionStop);
		session.add(sessionSelectAll);
		session.add(sessionDeselectAll);
		session.addSeparator();
		session.add(sessionDeleteSelected);
		session.add(sessionDeleteActiveSession);
		return session;
	}

	private JMenuItem createSessionStart() {
		JMenuItem menuItem = createMenuItem("Start recording", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionStop() {
		JMenuItem menuItem = createMenuItem("Stop recording", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionSelectAll() {
		JMenuItem menuItem = createMenuItem("Select all screenshots", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				controller.setSelectedAllScreenshots(true);
			}
		});
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeselectAll() {
		JMenuItem menuItem = createMenuItem("Deselect all screenshots", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				controller.setSelectedAllScreenshots(false);
			}
		});
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteSelected() {
		JMenuItem menuItem = createMenuItem("Delete selected screenshots", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int answer = JOptionPane.showConfirmDialog(menubar.getParent(), "Are you sure?", "",
						JOptionPane.OK_CANCEL_OPTION);
				if (answer == JOptionPane.OK_OPTION) {
					controller.deleteSelectedScreenshots();
				}
			}
		});
		menuItem.setEnabled(false);
		return menuItem;
	}

	private JMenuItem createSessionDeleteActiveSession() {
		JMenuItem menuItem = createMenuItem("Delete current session", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int answer = JOptionPane.showConfirmDialog(menubar.getParent(), "Are you sure?", "",
						JOptionPane.OK_CANCEL_OPTION);
				if (answer == JOptionPane.OK_OPTION) {
					controller.deleteActiveSession();
				}
			}
		});
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

	public void sessionActive(boolean active) {
		sessionDeleteActiveSession.setEnabled(active);
		sessionDeleteActiveSession.setEnabled(active);
		sessionDeleteSelected.setEnabled(active);
		sessionSelectAll.setEnabled(active);
		sessionDeselectAll.setEnabled(active);
		sessionStart.setEnabled(active);
		sessionStop.setEnabled(active);
	}
}
