package com.niklim.clicktrace.view.editor.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.action.screenshot.OpenSearchDialogActionListener;
import com.niklim.clicktrace.view.editor.dialog.JiraExportDialog;
import com.niklim.clicktrace.view.editor.dialog.SettingsDialog;

@Singleton
public class ToolsMenu {
	@Inject
	private SettingsDialog settingsDialog;

	@Inject
	private OpenSearchDialogActionListener openSearchDialogActionListener;

	@Inject
	private JiraExportDialog jiraExportDialog;

	@Inject
	private ActiveSession activeSession;

	private JMenuItem toolsExportToJira;

	public JMenu getMenu() {
		JMenu tools = new JMenu("Tools");

		JMenuItem toolsSettings = createToolsSettings();
		JMenuItem toolsSearch = createToolsSearch();
		toolsExportToJira = createToolsExportToJira();

		tools.add(toolsSettings);
		tools.add(toolsSearch);
		tools.add(toolsExportToJira);
		return tools;
	}

	private JMenuItem createToolsExportToJira() {
		return Menu.createMenuItem("Export to Clicktrace on JIRA Plugin", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				jiraExportDialog.open();
			}
		});
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

	public void sessionStateChanged() {
		Session session = activeSession.getSession();

		boolean atLeastOneShot = false;
		if (session != null) {
			atLeastOneShot = session.getShots().size() > 0;
		}
		toolsExportToJira.setEnabled(activeSession.isSessionOpen() && atLeastOneShot);
	}

}
