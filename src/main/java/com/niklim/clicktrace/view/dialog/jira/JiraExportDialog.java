package com.niklim.clicktrace.view.dialog.jira;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.JiraConfig.JiraUserMetadata;
import com.niklim.clicktrace.service.SessionCompressor;
import com.niklim.clicktrace.service.exception.JiraExportException;
import com.niklim.clicktrace.service.export.jira.JiraExportService;
import com.niklim.clicktrace.service.export.jira.JiraFieldDto;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

public class JiraExportDialog extends AbstractDialog {
	private static final Logger log = LoggerFactory.getLogger(JiraExportDialog.class);

	@Inject
	private JiraExportService jiraExportService;

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
	JTextField issueKeyTextField;

	JLabel issueSummaryLabel;
	JTextField issueSummary;
	JCheckBox createIssueCheckBox;

	JLabel issueTypeLabel;
	JComboBox issueType;
	JLabel priorityLabel;
	JComboBox priority;
	JLabel projectLabel;
	JComboBox project;
	JCheckBox useDescription;

	public void open(JiraConfig jiraConfig) {
		this.jiraConfig = jiraConfig;

		initModel();

		compressedSession = executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return sessionCompressor.compress(activeSession.getSession());
			}
		});

		center();
		dialog.setVisible(true);
	}

	private void initModel() {
		JiraUserMetadata userMetadata = jiraConfig.getUserMetadata().get();
		JiraFieldDto[] issueTypes = userMetadata.issueTypes.toArray(new JiraFieldDto[] {});
		issueType.setModel(new DefaultComboBoxModel(issueTypes));
		priority.setModel(new DefaultComboBoxModel(userMetadata.priorities.toArray()));
		project.setModel(new DefaultComboBoxModel(userMetadata.projects.toArray()));

		selectBugIssueType(issueTypes);
	}

	private void selectBugIssueType(JiraFieldDto[] issueTypes) {
		for (JiraFieldDto field : issueTypes) {
			if ("Bug".equals(field.label)) {
				issueType.getModel().setSelectedItem(field);
				break;
			}
		}
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
		issueKeyTextField = new JTextField();

		dialog.add(issueKeyLabel);
		dialog.add(issueKeyTextField, "w 400, wrap");

		createIssueCreateControls();

		dialog.add(createControlPanel("Export"), "span 2, align r");

		createListeners();
		postInit();
	}

	private void createIssueCreateControls() {
		createIssueCheckBox = new JCheckBox("create Issue");

		issueSummaryLabel = new JLabel("Summary");
		issueSummary = new JTextField();

		issueTypeLabel = new JLabel("Type");
		issueType = new JComboBox();

		priorityLabel = new JLabel("Priority");
		priority = new JComboBox();

		projectLabel = new JLabel("Project");
		project = new JComboBox();

		useDescription = new JCheckBox("use Session description as Issue descripiton");

		dialog.add(new JPanel());
		dialog.add(createIssueCheckBox, "wrap");
		dialog.add(new JPanel(), "wrap");

		dialog.add(issueSummaryLabel);
		dialog.add(issueSummary, "wrap");

		dialog.add(projectLabel);
		dialog.add(project, "w 100, wrap");

		dialog.add(issueTypeLabel);
		dialog.add(issueType, "w 100, wrap");

		dialog.add(priorityLabel);
		dialog.add(priority, "w 100, wrap");

		dialog.add(new JPanel());
		dialog.add(useDescription, "wrap");

		enableCreateControls(false);
	}

	private void createListeners() {
		createIssueCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (createIssueCheckBox.isSelected()) {
					issueKeyLabel.setEnabled(false);
					issueKeyTextField.setEnabled(false);

					enableCreateControls(true);
				} else {
					issueKeyLabel.setEnabled(true);
					issueKeyTextField.setEnabled(true);

					enableCreateControls(false);
				}
			}
		});
	}

	private void enableCreateControls(boolean show) {
		issueSummaryLabel.setEnabled(show);
		issueSummary.setEnabled(show);

		issueTypeLabel.setEnabled(show);
		issueType.setEnabled(show);

		priorityLabel.setEnabled(show);
		priority.setEnabled(show);

		projectLabel.setEnabled(show);
		project.setEnabled(show);

		useDescription.setEnabled(show);
	}

	@Override
	public void okAction() {
		if (createIssueCheckBox.isSelected()) {
			createIssueAndExport();
		} else {
			exportToExistingIssue();
		}
	}

	private void createIssueAndExport() {
		Optional<String> validationErrorOpt = validateIssueCreate();
		if (validationErrorOpt.isPresent()) {
			JOptionPane.showMessageDialog(dialog, validationErrorOpt.get());
			return;
		}

		try {
			String issueKey = createIssue();
			exportSession(issueKey);
		} catch (Exception e) {
			log.error("Issue create error", e);
			JOptionPane.showMessageDialog(dialog, e.getLocalizedMessage());
		}
	}

	private void exportToExistingIssue() {
		if (StringUtils.isEmpty(issueKeyTextField.getText())) {
			JOptionPane.showMessageDialog(dialog, InfoMsgs.JIRA_EXPORT_ISSUE_KEY_EMPTY);
			issueKeyTextField.requestFocus();
			return;
		}

		String issueKey = issueKeyTextField.getText();
		if (confirmSessionExport(issueKey)) {
			exportSession(issueKey);
		}
	}

	private Optional<String> validateIssueCreate() {
		if (StringUtils.isEmpty(issueSummary.getText().trim())) {
			issueSummary.requestFocus();
			return Optional.of(InfoMsgs.JIRA_EXPORT_NO_SUMMARY);
		}

		return Optional.<String> absent();
	}

	private String createIssue() throws URISyntaxException, InterruptedException, ExecutionException,
			UnsupportedEncodingException, IllegalStateException, IllegalArgumentException, JiraExportException,
			JSONException {
		String description;
		if (useDescription.isSelected()) {
			description = activeSession.getSession().getDescription();
		} else {
			description = "";
		}

		return jiraExportService.createIssue(jiraConfig, ((JiraFieldDto) project.getSelectedItem()).value,
				((JiraFieldDto) issueType.getSelectedItem()).value, ((JiraFieldDto) priority.getSelectedItem()).value,
				issueSummary.getText(), description);
	}

	private boolean confirmSessionExport(String issueKey) {
		try {
			boolean sessionExist = jiraExportService.checkSessionExist(jiraConfig.getUsername(), jiraConfig
					.getPassword().get(), issueKey, activeSession.getSession().getName(), jiraConfig.getInstanceUrl());
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

	private void exportSession(String issueKey) {
		try {
			showWaitingCursor();
			String stream = compressedSession.get();

			jiraExportService.exportSession(jiraConfig.getUsername(), jiraConfig.getPassword().get(), issueKey,
					activeSession.getSession().getName(), stream, jiraConfig.getInstanceUrl());
			JOptionPane.showMessageDialog(dialog, MessageFormat.format(InfoMsgs.JIRA_EXPORT_SUCCESS, issueKey));
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
