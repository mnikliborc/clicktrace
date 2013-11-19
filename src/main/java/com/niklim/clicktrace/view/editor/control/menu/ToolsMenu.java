package com.niklim.clicktrace.view.editor.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenSearchDialogActionListener;
import com.niklim.clicktrace.view.editor.dialog.SettingsDialog;

@Singleton
public class ToolsMenu {
	@Inject
	private SettingsDialog settingsDialog;

	@Inject
	private OpenSearchDialogActionListener openSearchDialogActionListener;

	public JMenu getMenu() {
		JMenu tools = new JMenu("Tools");

		JMenuItem toolsSettings = createToolsSettings();
		JMenuItem toolsSearch = createToolsSearch();

		tools.add(toolsSettings);
		tools.add(toolsSearch);
		return tools;
	}

	private JMenuItem createToolsSettings() {
		return Menu.createMenuItem("Settings", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				settingsDialog.open();
			}
		});
	}

	private JMenuItem createToolsSearch() {
		return Menu.createMenuItem("Search", Icons.SEARCH, openSearchDialogActionListener);
	}

}
