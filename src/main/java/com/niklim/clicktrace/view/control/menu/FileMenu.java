package com.niklim.clicktrace.view.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.controller.operation.session.OpenOpenSessionDialogOperation;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

@Singleton
public class FileMenu {
	JMenu menu;

	@Inject
	private NewSessionOperation newSessionOperation;

	@Inject
	private OpenOpenSessionDialogOperation openSessionOperation;

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
		return MenuBar.createMenuItem("New session", Icons.NEW_SESSION, OperationsShortcutEnum.SESSION_NEW,
				newSessionOperation.action());
	}

	private JMenuItem createFileOpenSession() {
		return MenuBar.createMenuItem("Open session", Icons.OPEN_SESSION, OperationsShortcutEnum.SESSION_OPEN,
				openSessionOperation.action());
	}

	private JMenuItem createFileExit() {
		return MenuBar.createMenuItem("Exit", null, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
	}
}
