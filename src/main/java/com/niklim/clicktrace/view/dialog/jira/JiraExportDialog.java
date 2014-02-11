package com.niklim.clicktrace.view.dialog.jira;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.JiraExportService;
import com.niklim.clicktrace.service.SessionCompressor;
import com.niklim.clicktrace.service.exception.JiraExportException;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

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

	private JiraConfig jiraConfig;

	ExecutorService executor;
	Future<String> compressedSession;

	public JiraExportDialog() {
		executor = Executors.newFixedThreadPool(1);
	}

	JLabel issueKeyLabel;
	JTextField issueKey;

	JCheckBox createIssueCheckBox;
	JLabel issueSummaryLabel;
	JTextField issueSummary;
	JComboBox issueType;

	public void open(JiraConfig jiraConfig) {
		this.jiraConfig = jiraConfig;

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
		dialog.setTitle("Export to JIRA Clicktrace Plugin");
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]"));
		dialog.setResizable(false);

		issueKeyLabel = new JLabel("Issue key");
		issueKey = new JTextField();

		dialog.add(issueKeyLabel);
		dialog.add(issueKey, "w 400, wrap");

		createIssueCreateControls();

		dialog.add(createControlPanel("Export"), "span 2, h 50, align r");

		createListeners();
		postInit();
	}

	private void createIssueCreateControls() {
		createIssueCheckBox = new JCheckBox("create Issue");
		issueSummaryLabel = new JLabel("Issue summary");
		issueSummary = new JTextField();
		issueType = new JComboBox(new String[] {});

		dialog.add(new JPanel());
		dialog.add(createIssueCheckBox, "wrap");

	}

	private void createListeners() {
		createIssueCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (createIssueCheckBox.isSelected()) {
					issueKeyLabel.setEnabled(false);
					issueKey.setEnabled(false);

				} else {
					issueKeyLabel.setEnabled(true);
					issueKey.setEnabled(true);
				}
			}
		});
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
			boolean sessionExist = jiraExportService.checkSessionExist(jiraConfig.getUsername(), jiraConfig
					.getPassword().get(), issueKey.getText(), activeSession.getSession().getName(), jiraConfig
					.getInstanceUrl());
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

			jiraExportService.exportSession(jiraConfig.getUsername(), jiraConfig.getPassword().get(),
					issueKey.getText(), activeSession.getSession().getName(), stream, jiraConfig.getInstanceUrl());
			JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_SUCCESS);
			close();
		} catch (JiraExportException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} catch (Throwable e) {
			log.error("unpredicted", e);
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} finally {
			hideWaitingCursor();
		}

	}

}
