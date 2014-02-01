package com.niklim.clicktrace.view.control.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.controller.operation.session.HtmlExportOperation;
import com.niklim.clicktrace.controller.operation.session.JiraExportOperation;
import com.niklim.clicktrace.controller.operation.session.OpenSearchDialogOperation;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.view.OperationsShortcutEnum;
import com.niklim.clicktrace.view.dialog.settings.SettingsDialog;

@Singleton
public class ToolsMenu {
	@Inject
	private SettingsDialog settingsDialog;
	
	@Inject
	private OpenSearchDialogOperation openSearchDialogOperation;
	
	@Inject
	private JiraExportOperation jiraExportOperation;
	
	@Inject
	private HtmlExportOperation htmlExportOperation;
	
	@Inject
	private ActiveSession activeSession;
	
	private JMenuItem toolsExportToJira;
	
	private JMenuItem toolsExportToHtml;
	
	public JMenu getMenu() {
		JMenu tools = new JMenu("Tools");
		
		JMenuItem toolsSettings = createToolsSettings();
		JMenuItem toolsSearch = createToolsSearch();
		toolsExportToJira = createToolsExportToJira();
		toolsExportToJira.setEnabled(false);
		
		toolsExportToHtml = createToolsExportToHtml();
		toolsExportToHtml.setEnabled(false);
		
		tools.add(toolsSettings);
		tools.add(toolsSearch);
		tools.add(toolsExportToJira);
		tools.add(toolsExportToHtml);
		return tools;
	}
	
	private JMenuItem createToolsExportToHtml() {
		return MenuBar.createMenuItem("Export to HTML", OperationsShortcutEnum.HTML_EXPORT,
				htmlExportOperation.action());
	}
	
	private JMenuItem createToolsExportToJira() {
		return MenuBar.createMenuItem("Export to JIRA Clicktrace Plugin",
				OperationsShortcutEnum.JIRA_EXPORT, jiraExportOperation.action());
	}
	
	private JMenuItem createToolsSettings() {
		return MenuBar.createMenuItem("Settings", null, new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				settingsDialog.open();
			}
		});
	}
	
	private JMenuItem createToolsSearch() {
		return MenuBar.createMenuItem("Find", Icons.SEARCH, OperationsShortcutEnum.FIND,
				openSearchDialogOperation.action());
	}
	
	public void sessionStateChanged() {
		Session session = activeSession.getSession();
		
		boolean atLeastOneShot = false;
		if (session != null) {
			atLeastOneShot = session.getShots().size() > 0;
		}
		toolsExportToJira.setEnabled(activeSession.isSessionLoaded() && atLeastOneShot);
		toolsExportToHtml.setEnabled(activeSession.isSessionLoaded() && atLeastOneShot);
	}
	
}
