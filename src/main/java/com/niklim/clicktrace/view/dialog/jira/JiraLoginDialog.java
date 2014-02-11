package com.niklim.clicktrace.view.dialog.jira;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

public class JiraLoginDialog extends AbstractDialog {

	@Inject
	private JiraExportDialog exportDialog;

	@Inject
	private UserProperties props;

	JTextField jiraInstanceUrl;
	JTextField username;
	JPasswordField password;

	@Inject
	public void init() {
		dialog.setTitle("Log into JIRA Clicktrace Plugin");
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]"));
		dialog.setResizable(false);

		jiraInstanceUrl = new JTextField();
		username = new JTextField();
		password = new JPasswordField();

		dialog.add(new JLabel("JIRA URL"));
		dialog.add(jiraInstanceUrl, "w 400, wrap");
		dialog.add(new JLabel("Username"));
		dialog.add(username, "w 400, wrap");
		dialog.add(new JLabel("Password"));
		dialog.add(password, "w 400, wrap");

		dialog.add(createControlPanel("Log in"), "span 2, h 50, align r");
		postInit();
	}

	public void open() {
		JiraConfig jiraConfig = props.getJiraConfig();
		jiraInstanceUrl.setText(jiraConfig.getInstanceUrl());
		username.setText(jiraConfig.getUsername());

		center();
		dialog.setVisible(true);
	}

	@Override
	protected void okAction() {
		close();

		JiraConfig jiraConfig = new JiraConfig(jiraInstanceUrl.getText(), username.getText());
		jiraConfig.setPassword(password.getText());
		exportDialog.open(jiraConfig);
	}

}
