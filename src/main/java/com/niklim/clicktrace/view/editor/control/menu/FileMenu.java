package com.niklim.clicktrace.view.editor.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.view.editor.action.session.NewSessionActionListener;
import com.niklim.clicktrace.view.editor.action.session.OpenSessionActionListener;

@Singleton
public class FileMenu {
	JMenu menu;

	@Inject
	private NewSessionActionListener newSessionActionListener;

	@Inject
	private OpenSessionActionListener openSessionActionListener;

	@Inject
	public void init() {
		menu = new JMenu("File");

		JMenuItem fileNewSession = createFileNewSession();
		JMenuItem fileOpenSession = createFileOpenSession();
		JMenuItem fileExit = createFileExit();

		menu.add(fileNewSession);
		menu.add(fileOpenSession);
		menu.addSeparator();
		menu.add(fileExit);
	}

	public JMenu getMenu() {
		return menu;
	}

	private JMenuItem createFileNewSession() {
		return Menu.createMenuItem("New session", Icons.NEW_SESSION, newSessionActionListener);
	}

	private JMenuItem createFileOpenSession() {
		return Menu.createMenuItem("Open session", Icons.OPEN_SESSION, openSessionActionListener);
	}

	private JMenuItem createFileExit() {
		return Menu.createMenuItem("Exit", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
	}
}
