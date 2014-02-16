package com.niklim.clicktrace.view.dialog.jira;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.RestClientException;
import com.google.inject.Inject;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.JiraConfig.JiraUserMetadata;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.export.jira.JiraMetadataService;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

public class JiraLoginDialog extends AbstractDialog {
	private static final Logger log = LoggerFactory.getLogger(JiraLoginDialog.class);

	private static final int HTTP_UNAUTHORIZED = 401;
	private static final int HTTP_FORBIDDEN = 403;
	private static final int HTTP_NOT_FOUND = 404;

	@Inject
	private JiraExportDialog exportDialog;

	@Inject
	private UserProperties props;

	@Inject
	private JiraMetadataService metadataService;

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
		JiraConfig jiraConfig = new JiraConfig(jiraInstanceUrl.getText(), username.getText());
		jiraConfig.setPassword(password.getText());
		try {
			JiraUserMetadata userMetadata = metadataService.loadUserMetadata(jiraConfig);
			jiraConfig.setUserMetadata(userMetadata);
			close();
			exportDialog.open(jiraConfig);
		} catch (ExecutionException e) {
			handleKnownException(e);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_UNKNOWN_LOGIN_FAILURE);
		}

	}

	private void handleKnownException(ExecutionException e) {
		if (e.getCause() instanceof RestClientException) {
			handleRestException((RestClientException) e.getCause());
		} else {
			log.error("Unhandled ExecutionException cause", e.getCause());
			JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_UNKNOWN_LOGIN_FAILURE);
		}
	}

	private void handleRestException(RestClientException re) {
		if (re.getStatusCode().isPresent()) {
			int statusCode = re.getStatusCode().get();
			if (statusCode == HTTP_UNAUTHORIZED) {
				JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_AUTHENTICATION_FAILURE);
				return;
			} else if (statusCode == HTTP_FORBIDDEN) {
				JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_CAPTCHA_NEEDED);
				return;
			} else if (statusCode == HTTP_NOT_FOUND) {
				JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_WRONG_URL);
				return;
			} else {
				log.error(MessageFormat.format("Unhandled HTTP Code {0}", statusCode));
			}
		} else {
			log.error("HTTP Code not present.");
		}
	}
}
