package com.niklim.clicktrace.view.editor.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.google.inject.Singleton;

@Singleton
public class EditorMenu {
	JMenuBar menubar;

	public EditorMenu() {
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

		session.add(sessionStart);
		session.add(sessionStop);
		session.add(sessionSelectAll);
		session.addSeparator();
		session.add(sessionDeleteSelected);
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
		return createMenuItem("Select all", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createSessionDeleteSelected() {
		return createMenuItem("Delete selected", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
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
		JMenuItem fileDeleteActiveSession = createFileDeleteActiveSession();
		JMenuItem fileExit = createFileExit();

		file.add(fileNewSession);
		file.add(fileOpenSession);
		file.add(fileDeleteActiveSession);
		file.addSeparator();
		file.add(fileExit);
		return file;
	}

	private JMenuItem createFileNewSession() {
		return createMenuItem("New session", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createFileOpenSession() {
		return createMenuItem("Open session", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
			}
		});
	}

	private JMenuItem createFileDeleteActiveSession() {
		return createMenuItem("Delete current session", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// TODO implement
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

	public JMenuBar getMenu() {
		return menubar;
	}
}
