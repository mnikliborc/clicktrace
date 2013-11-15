package com.niklim.clicktrace.view.editor.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.view.editor.SettingsDialog;

@Singleton
public class ToolsMenu {
	@Inject
	private SettingsDialog settingsDialog;

	public JMenu getMenu() {
		JMenu tools = new JMenu("Tools");

		JMenuItem toolsSettings = createToolsSettings();
		tools.add(toolsSettings);
		return tools;
	}

	private JMenuItem createToolsSettings() {
		return Menu.createMenuItem("Settings", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				settingsDialog.open();
			}
		});
	}
}
