package com.niklim.clicktrace.dialog.jira;

import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.google.common.collect.Lists;
import com.niklim.clicktrace.dialog.AbstractDialogView;
import com.niklim.clicktrace.msg.InfoMsgs;

public class JiraExportView extends AbstractDialogView {

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

	List<JComponent> createControls;

	public JiraExportView() {
		dialog.setTitle("Export to " + InfoMsgs.JIRA_ADDON_NAME);
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]"));
		dialog.setResizable(false);

		issueKeyLabel = new JLabel("Issue key");
		issueKeyTextField = new JTextField();

		dialog.add(issueKeyLabel);
		dialog.add(issueKeyTextField, "w 400, wrap");

		createIssueCreateControls();

		dialog.add(createControlPanel("Export"), "span 2, align r");
	}

	private void createIssueCreateControls() {
		createIssueCheckBox = new JCheckBox("create Issue");

		issueSummaryLabel = new JLabel("Summary");
		issueSummary = new JTextField();

		issueTypeLabel = new JLabel("Type");
		issueType = new JComboBox();
		AutoCompleteDecorator.decorate(issueType);

		priorityLabel = new JLabel("Priority");
		priority = new JComboBox();
		AutoCompleteDecorator.decorate(priority);

		projectLabel = new JLabel("Project");
		project = new JComboBox();
		AutoCompleteDecorator.decorate(project);

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

		createControls = Lists.<JComponent> newArrayList(issueSummaryLabel, issueSummary, issueTypeLabel, issueType,
				priorityLabel, priority, projectLabel, project, useDescription);
		enableCreateControls(false);
	}

	void enableCreateControls(boolean show) {
		for (JComponent createControl : createControls) {
			createControl.setEnabled(show);
		}
	}

	public void setExistingIssueComponentEnabled(boolean show) {
		issueKeyLabel.setEnabled(show);
		issueKeyTextField.setEnabled(show);
	}
}
