package com.niklim.clicktrace.dialog.jira;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.dialog.AbstractDialogView;
import com.niklim.clicktrace.msg.InfoMsgs;

public class JiraLoginView extends AbstractDialogView {
	JTextField jiraInstanceUrl;
	JTextField username;
	JPasswordField password;

	public JiraLoginView() {
		dialog.setTitle("Log into " + InfoMsgs.JIRA_ADDON_NAME);
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
	}
}
