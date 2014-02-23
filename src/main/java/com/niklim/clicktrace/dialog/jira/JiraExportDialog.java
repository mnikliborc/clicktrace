package com.niklim.clicktrace.dialog.jira;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.dialog.AbstractDialog;
import com.niklim.clicktrace.msg.InfoMsgs;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.JiraConfig.JiraUserMetadata;
import com.niklim.clicktrace.service.exception.JiraExportException;
import com.niklim.clicktrace.service.export.jira.JiraExporter;
import com.niklim.clicktrace.service.export.jira.JiraExporter.JiraExportCallback;
import com.niklim.clicktrace.service.export.jira.JiraFieldDto;
import com.niklim.clicktrace.service.export.jira.JiraService;

@Singleton
public class JiraExportDialog extends AbstractDialog<JiraExportView> {
	private static final Logger log = LoggerFactory.getLogger(JiraExportDialog.class);

	@Inject
	private ActiveSession activeSession;

	@Inject
	private JiraService jiraService;

	@Inject
	private JiraExporter jiraExporter;

	private JiraConfig jiraConfig;

	public void open(JiraConfig jiraConfig) {
		this.jiraConfig = jiraConfig;

		initModel();
		jiraExporter.initExport(activeSession.getSession());

		center();
		view.dialog().setVisible(true);
	}

	private void initModel() {
		JiraUserMetadata userMetadata = jiraConfig.getUserMetadata().get();
		JiraFieldDto[] issueTypes = userMetadata.issueTypes.toArray(new JiraFieldDto[] {});
		view.issueType.setModel(new DefaultComboBoxModel(issueTypes));
		view.priority.setModel(new DefaultComboBoxModel(userMetadata.priorities.toArray()));
		view.project.setModel(new DefaultComboBoxModel(userMetadata.projects.toArray()));

		selectBugIssueType(issueTypes);
	}

	private void selectBugIssueType(JiraFieldDto[] issueTypes) {
		for (JiraFieldDto field : issueTypes) {
			if ("Bug".equals(field.label)) {
				view.issueType.getModel().setSelectedItem(field);
				break;
			}
		}
	}

	@Override
	public void close() {
		jiraExporter.cleanup();
		view.dialog().setVisible(false);
	}

	@Inject
	public void init() {
		createListeners();
		postInit();
	}

	private void createListeners() {
		view.createIssueCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (view.createIssueCheckBox.isSelected()) {
					view.setExistingIssueComponentEnabled(false);
					view.enableCreateControls(true);
				} else {
					view.setExistingIssueComponentEnabled(true);
					view.enableCreateControls(false);
				}
			}
		});
	}

	@Override
	public void okAction() {
		if (view.createIssueCheckBox.isSelected()) {
			createIssueAndExport();
		} else {
			exportToExistingIssue();
		}
	}

	private void createIssueAndExport() {
		Optional<String> validationErrorOpt = validateIssueCreate();
		if (validationErrorOpt.isPresent()) {
			JOptionPane.showMessageDialog(view.dialog(), validationErrorOpt.get());
			return;
		}

		try {
			if (validateCreateIssue()) {
				String issueKey = createIssue();
				exportSession(issueKey);
			}
		} catch (JiraExportException e) {
			JOptionPane.showMessageDialog(view.dialog(), e.getLocalizedMessage());
		} catch (Exception e) {
			log.error("Issue create error", e);
			JOptionPane.showMessageDialog(view.dialog(), e.getLocalizedMessage());
		}
	}

	private boolean validateCreateIssue() {
		if (view.project.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_EXPORT_NO_PROJECT);
			return false;
		}
		return true;
	}

	private void exportToExistingIssue() {
		if (StringUtils.isEmpty(view.issueKeyTextField.getText())) {
			JOptionPane.showMessageDialog(view.dialog(), InfoMsgs.JIRA_EXPORT_ISSUE_KEY_EMPTY);
			view.issueKeyTextField.requestFocus();
			return;
		}

		String issueKey = view.issueKeyTextField.getText();
		if (confirmSessionExport(issueKey)) {
			exportSession(issueKey);
		}
	}

	private Optional<String> validateIssueCreate() {
		if (StringUtils.isEmpty(view.issueSummary.getText().trim())) {
			view.issueSummary.requestFocus();
			return Optional.of(InfoMsgs.JIRA_EXPORT_NO_SUMMARY);
		}

		return Optional.<String> absent();
	}

	private String createIssue() throws URISyntaxException, InterruptedException, ExecutionException,
			UnsupportedEncodingException, IllegalStateException, IllegalArgumentException, JiraExportException,
			JSONException {
		String description;
		if (view.useDescription.isSelected()) {
			description = activeSession.getSession().getDescription();
		} else {
			description = "";
		}

		return callCreateIssue(description);
	}

	private String callCreateIssue(String description) throws URISyntaxException, InterruptedException,
			ExecutionException, JiraExportException, JSONException {
		String project = ((JiraFieldDto) view.project.getSelectedItem()).value;
		String issueType = ((JiraFieldDto) view.issueType.getSelectedItem()).value;
		String priority = ((JiraFieldDto) view.priority.getSelectedItem()).value;
		String summary = view.issueSummary.getText();

		return jiraService.createIssue(jiraConfig, project, issueType, priority, summary, description);
	}

	private boolean confirmSessionExport(String issueKey) {
		try {
			boolean sessionExists = callCheckSessionExists(issueKey);
			if (sessionExists) {
				return askUserForExportConfirmation();
			} else {
				return true;
			}
		} catch (JiraExportException e) {
			JOptionPane.showMessageDialog(view.dialog(), e.getMessage());
			return false;
		}
	}

	private boolean callCheckSessionExists(String issueKey) throws JiraExportException {
		String username = jiraConfig.getUsername();
		String password = jiraConfig.getPassword().get();
		String sessionName = activeSession.getSession().getName();
		String jiraUrl = jiraConfig.getInstanceUrl();

		return jiraService.checkSessionExists(username, password, issueKey, sessionName, jiraUrl);
	}

	private boolean askUserForExportConfirmation() {
		int res = JOptionPane.showConfirmDialog(view.dialog(), "Session exists. Overwrite?", "Overwrite?",
				JOptionPane.OK_CANCEL_OPTION);
		return res == JOptionPane.OK_OPTION;
	}

	private void exportSession(final String issueKey) {
		String username = jiraConfig.getUsername();
		String password = jiraConfig.getPassword().get();
		String jiraUrl = jiraConfig.getInstanceUrl();

		try {
			showWaitingCursor();
			jiraExporter.export(username, password, issueKey, jiraUrl, new JiraExportCallback() {
				public void success() {
					hideWaitingCursor();
					JOptionPane.showMessageDialog(view.dialog(),
							MessageFormat.format(InfoMsgs.JIRA_EXPORT_SUCCESS, issueKey));
					close();
				}

				@Override
				public void failure(String msg) {
					System.out.println("fail");
					hideWaitingCursor();
					JOptionPane.showMessageDialog(view.dialog(), msg);
				}
			});
		} catch (InterruptedException e) {
			hideWaitingCursor();
			JOptionPane.showMessageDialog(view.dialog(), e.getLocalizedMessage());
		} catch (ExecutionException e) {
			hideWaitingCursor();
			JOptionPane.showMessageDialog(view.dialog(), e.getLocalizedMessage());
		}
	}

	@Override
	protected JiraExportView createView() {
		return new JiraExportView();
	}

}
