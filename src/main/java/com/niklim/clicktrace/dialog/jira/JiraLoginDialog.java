package com.niklim.clicktrace.dialog.jira;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.ConnectException;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.RestClientException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.dialog.AbstractDialog;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.JiraConfig.JiraUserMetadata;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.export.jira.JiraMetadataService;

@Singleton
public class JiraLoginDialog extends AbstractDialog<JiraLoginView> {
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

	private KeyAdapter loginAction = new KeyAdapter() {
		public void keyTyped(KeyEvent e) {
			if (e.getKeyChar() == '\n') {
				okAction();
			}
		}
	};

	@Inject
	public void init() {
		createListeners();
		postInit();
	}

	private void createListeners() {
		view.jiraInstanceUrl.addKeyListener(loginAction);
		view.username.addKeyListener(loginAction);
		view.password.addKeyListener(loginAction);
	}

	public void open() {
		initModel();

		center();
		view.dialog().setVisible(true);
	}

	private void initModel() {
		JiraConfig jiraConfig = props.getJiraConfig();
		view.jiraInstanceUrl.setText(jiraConfig.getInstanceUrl());
		view.username.setText(jiraConfig.getUsername());
	}

	@Override
	protected void okAction() {
		showWaitingCursor();

		JiraConfig jiraConfig = new JiraConfig(view.jiraInstanceUrl.getText(), view.username.getText());
		jiraConfig.setPassword(view.password.getText());
		try {
			JiraUserMetadata userMetadata = metadataService.loadUserMetadata(jiraConfig);
			jiraConfig.setUserMetadata(userMetadata);
			close();
			exportDialog.open(jiraConfig);
		} catch (ExecutionException e) {
			handleKnownException(e);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_UNKNOWN_LOGIN_FAILURE);
		}

		hideWaitingCursor();
	}

	private void handleKnownException(ExecutionException e) {
		RestClientException restException = getCause(e, RestClientException.class);
		if (restException != null) {
			handleRestException(restException);
			return;
		}

		ConnectException connectException = getCause(e, ConnectException.class);
		if (connectException != null) {
			JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_UNAVAILABLE);
		} else {
			log.error("Unhandled ExecutionException cause", e.getCause());
			JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_UNKNOWN_LOGIN_FAILURE);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T getCause(Throwable t, Class<T> exceptionClass) {
		if (t.getClass() == exceptionClass) {
			return (T) t;
		}

		if (t.getCause() == null) {
			return null;
		} else {
			return getCause(t.getCause(), exceptionClass);
		}
	}

	private void handleRestException(RestClientException re) {
		if (re.getStatusCode().isPresent()) {
			int statusCode = re.getStatusCode().get();
			if (statusCode == HTTP_UNAUTHORIZED) {
				JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_EXPORT_AUTHENTICATION_FAILURE);
				return;
			} else if (statusCode == HTTP_FORBIDDEN) {
				JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_EXPORT_CAPTCHA_NEEDED);
				return;
			} else if (statusCode == HTTP_NOT_FOUND) {
				JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_EXPORT_WRONG_URL);
				return;
			} else {
				log.error(MessageFormat.format("Unhandled HTTP Code {0}", statusCode));
			}
		} else {
			log.error("HTTP Code not present.");
		}
	}

	@Override
	protected JiraLoginView createView() {
		return new JiraLoginView();
	}
}
