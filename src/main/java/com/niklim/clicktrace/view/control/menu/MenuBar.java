package com.niklim.clicktrace.view.control.menu;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.view.OperationsShortcutEnum;

@Singleton
public class MenuBar {
	JMenuBar menubar;

	@Inject
	private FileMenu fileMenu;

	@Inject
	private SessionMenu sessionMenu;

	@Inject
	private ScreenShotMenu screenShotMenu;

	@Inject
	private ToolsMenu toolsMenu;

	@Inject
	public void init() {
		menubar = new JMenuBar();

		menubar.add(fileMenu.getMenu());
		menubar.add(sessionMenu.getMenu());
		menubar.add(screenShotMenu.getMenu());
		menubar.add(toolsMenu.getMenu());
	}

	public static JMenuItem createMenuItem(String label, OperationsShortcutEnum shortcut, ActionListener actionListener) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(actionListener);

		if (shortcut != null) {
			item.setToolTipText(shortcut.text);
		}

		return item;
	}

	public static JMenuItem createMenuItem(String label, String iconName, OperationsShortcutEnum shortcut,
			ActionListener listener) {
		JMenuItem item = new JMenuItem(label, new ImageIcon(Icons.createIconImage(iconName, label)));
		item.addActionListener(listener);

		if (shortcut != null) {
			item.setToolTipText(shortcut.text);
		}

		return item;
	}

	public JMenuBar getMenuBar() {
		return menubar;
	}

	public void sessionStateChanged() {
		sessionMenu.sessionStateChanged();
		screenShotMenu.sessionStateChanged();
		toolsMenu.sessionStateChanged();
	}
}
