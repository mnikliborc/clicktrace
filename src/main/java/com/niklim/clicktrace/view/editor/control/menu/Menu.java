package com.niklim.clicktrace.view.editor.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;

@Singleton
public class Menu {
	JMenuBar menubar;

	@Inject
	private FileMenu fileMenu;

	@Inject
	private SessionMenu sessionMenu;

	@Inject
	private ScreenShotMenu screenShotMenu;

	public Menu() {

	}

	@Inject
	public void init() {
		menubar = new JMenuBar();

		menubar.add(fileMenu.getMenu());
		menubar.add(sessionMenu.getMenu());
		menubar.add(screenShotMenu.getMenu());
		menubar.add(createTools());
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

	public static JMenuItem createMenuItem(String label, ActionListener listener) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(listener);
		return item;
	}

	public static JMenuItem createMenuItem(String label, String iconName, ActionListener listener) {
		JMenuItem item = new JMenuItem(label, new ImageIcon(Icons.createIconImage(iconName, label)));
		item.addActionListener(listener);
		return item;
	}

	public JMenuBar getMenuBar() {
		return menubar;
	}

	public void sessionStateChanged() {
		sessionMenu.sessionStateChanged();
		screenShotMenu.sessionStateChanged();
	}
}
