package com.niklim.clicktrace.view.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.niklim.clicktrace.Messages;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.JiraConfig;
import com.niklim.clicktrace.service.JiraExportService;
import com.niklim.clicktrace.service.SessionCompressor;
import com.niklim.clicktrace.service.exception.JiraExportException;
import com.niklim.clicktrace.view.MainFrameHolder;

public class JiraExportDialog extends AbstractDialog {

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

		dialog.setVisible(true);
	}

	@Override
	protected void close() {
		compressedSession = null;
		dialog.setVisible(false);
	}

	@Inject
	public void init() {
		dialog = new JDialog(MainFrameHolder.get(), true);
		dialog.setTitle("Export to Clicktrace on JIRA Plugin");
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]"));

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 480, 200);

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

		JButton cancelButton = new JButton("Cancel");
		JButton exportButton = new JButton("Export");

		JPanel controlPanel = new JPanel(new MigLayout("align right"));
		controlPanel.add(exportButton, "w 80, tag apply");
		controlPanel.add(cancelButton, "w 80, tag cancel");
		dialog.add(controlPanel, "span 2, grow, h 50");

		createActionListeners(cancelButton, exportButton);
	}

	private void createActionListeners(JButton cancelButton, JButton exportButton) {
		exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isEmpty(issueKey.getText())) {
					JOptionPane.showMessageDialog(dialog, Messages.EXPORT_ISSUE_KEY_EMPTY);
					issueKey.requestFocus();
					return;
				}

				if (confirmSessionExport()) {
					exportSession();
				}
			}

		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
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
		if (res == JOptionPane.OK_OPTION) {
			return true;
		} else {
			return false;
		}
	}

	private void exportSession() {
		try {
			showWaitingCursor();
			String stream = compressedSession.get();

			jiraExportService.exportSession(username.getText(), password.getText(), issueKey.getText(), activeSession
					.getSession().getName(), stream, jiraInstanceUrl.getText());
			hideWaitingCursor();
			JOptionPane.showMessageDialog(dialog, Messages.EXPORT_SUCCESS);
			close();
		} catch (JiraExportException e) {
			JOptionPane.showMessageDialog(dialog, e.getMessage());
		} catch (Throwable e) {
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
