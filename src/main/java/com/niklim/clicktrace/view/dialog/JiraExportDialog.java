package com.niklim.clicktrace.view.dialog;

import java.awt.Cursor;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.JiraConfig;
import com.niklim.clicktrace.service.JiraExportService;
import com.niklim.clicktrace.service.SessionCompressor;
import com.niklim.clicktrace.service.exception.JiraExportException;

public class JiraExportDialog extends AbstractDialog {

	private static final Logger log = LoggerFactory.getLogger(JiraExportDialog.class);

	@Inject
	private JiraExportService jiraExportService;

	@Inject
	private UserProperties props;

	@Inject
	private ActiveSession activeSession;

	@Inject
	private SessionCompressor sessionCompressor;

	ExecutorService executor;
	Future<String> compressedSession;

	public JiraExportDialog() {
		executor = Executors.newFixedThreadPool(1);
	}

	JTextField jiraInstanceUrl;
	JTextField username;
	JPasswordField password;
	JTextField issueKey;

	public void open() {
		JiraConfig jiraConfig = props.getJiraConfig();
		jiraInstanceUrl.setText(jiraConfig.getInstanceUrl());
		username.setText(jiraConfig.getUsername());

		compressedSession = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return sessionCompressor.compress(activeSession.getSession());
			}
		});

		center();
		dialog.setVisible(true);
	}

	@Override
	public void close() {
		compressedSession = null;
		dialog.setVisible(false);
	}

	@Inject
	public void init() {
		dialog.setTitle("Export to Clicktrace on JIRA Plugin");
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]"));
		dialog.setResizable(false);

		jiraInstanceUrl = new JTextField();
		username = new JTextField();
		password = new JPasswordField();
		issueKey = new JTextField();

		dialog.add(new JLabel("JIRA URL"));
		dialog.add(jiraInstanceUrl, "w 400, wrap");
		dialog.add(new JLabel("Username"));
		dialog.add(username, "w 400, wrap");
		dialog.add(new JLabel("Password"));
		dialog.add(password, "w 400, wrap");
		dialog.add(new JLabel("Issue key"));
		dialog.add(issueKey, "w 400, wrap");

		dialog.add(createControlPanel("Export"), "span 2, h 50, align r");

		pack();
	}

	@Override
	public void okAction() {
		if (StringUtils.isEmpty(issueKey.getText())) {
			JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_ISSUE_KEY_EMPTY);
			issueKey.requestFocus();
			return;
		}

		if (confirmSessionExport()) {
			exportSession();
		}
	}

	private boolean confirmSessionExport() {
		try {
			boolean sessionExist = jiraExportService.checkSessionExist(username.getText(), password.getText(),
					issueKey.getText(), activeSession.getSession().getName(), jiraInstanceUrl.getText());
			if (sessionExist) {
				return askUserForExportConfirmation();
			} else {
				return true;
			}
		} catch (JiraExportException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage());
			return false;
		}
	}

	private boolean askUserForExportConfirmation() {
		int res = JOptionPane.showConfirmDialog(dialog, "Session exists. Overwrite?", "Overwrite?",
				JOptionPane.OK_CANCEL_OPTION);
		return res == JOptionPane.OK_OPTION;
	}

	private void exportSession() {
		try {
			showWaitingCursor();
			String stream = compressedSession.get();

			jiraExportService.exportSession(username.getText(), password.getText(), issueKey.getText(), activeSession
					.getSession().getName(), stream, jiraInstanceUrl.getText());
			hideWaitingCursor();
			JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_SUCCESS);
			close();
		} catch (JiraExportException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} catch (Throwable e) {
			log.error("unpredicted", e);
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		}

	}

	private void showWaitingCursor() {
		dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	private void hideWaitingCursor() {
		dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
