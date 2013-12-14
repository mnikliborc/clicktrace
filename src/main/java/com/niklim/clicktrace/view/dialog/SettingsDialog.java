package com.niklim.clicktrace.view.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.JiraConfig;
import com.niklim.clicktrace.view.MainView;

@Singleton
public class SettingsDialog {
	JDialog dialog;

	@Inject
	private UserProperties props;

	@Inject
	private MainView mainView;

	JSpinner captureFrequency;
	JTextField captureDimension;
	JTextField imageEditorPath;
	JFileChooser imageEditorFileChooser;

	JTextField jiraUrl;
	JTextField jiraUsername;

	JCheckBox recordMouseClicks;

	public SettingsDialog() {
	}

	@Inject
	public void init() {
		dialog = new JDialog(mainView.getFrame(), true);
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]rel[]"));
		dialog.setResizable(false);
		dialog.setTitle("Settings");

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 490,
				270);

		imageEditorFileChooser = new JFileChooser();

		createCaptureFrequencyPanel();
		createCaptureMouseClicksPanel();
		// createCaptureDimensionPanel();
		createImageEditorPathPanel();
		createJiraPanel();

		createControlPanel();

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	private void createCaptureMouseClicksPanel() {
		recordMouseClicks = new JCheckBox();

		dialog.add(new JLabel("Record mouse clicks"));
		dialog.add(recordMouseClicks, "wrap");
	}

	private void createControlPanel() {
		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Cancel");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveModel();
				dialog.setVisible(false);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		cancelButton.setToolTipText("[Esc]");

		JPanel controlPanel = new JPanel(new MigLayout("align right"));
		controlPanel.add(saveButton, "w 80, tag apply");
		controlPanel.add(cancelButton, "w 80, tag cancel");
		dialog.add(controlPanel, "span 3, grow, h 50");
	}

	private void createCaptureDimensionPanel() {
		captureDimension = new JTextField();
		JButton captureDimensionChangeButton = new JButton("change");
		captureDimensionChangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		dialog.add(new JLabel("Capture dimension"));
		dialog.add(captureDimension, "w 400");
		dialog.add(captureDimensionChangeButton, "wrap");
	}

	private void createCaptureFrequencyPanel() {
		captureFrequency = new JSpinner(new SpinnerListModel(Lists.newArrayList(1d, 2d, 3d, 4d)));
		((DefaultEditor) captureFrequency.getEditor()).getTextField().setEditable(false);

		JLabel laabel = new JLabel("Screenshot");
		Font font = laabel.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		laabel.setFont(boldFont);
		dialog.add(laabel);
		dialog.add(new JSeparator(), "span 2, wrap");
		JLabel label = new JLabel("Capture frequency");
		label.setToolTipText("Shots per second");
		dialog.add(label);
		dialog.add(captureFrequency, "w 400, wrap");
	}

	private void createImageEditorPathPanel() {
		imageEditorPath = new JTextField();

		JButton imageEditorPathChangeButton = new JButton("change");
		imageEditorPathChangeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = imageEditorFileChooser.showOpenDialog(dialog);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = imageEditorFileChooser.getSelectedFile();
					imageEditorPath.setText(file.getAbsolutePath());
				}
			}
		});

		dialog.add(new JLabel("Image editing program"));
		dialog.add(imageEditorPath, "");
		dialog.add(imageEditorPathChangeButton, "wrap");
	}

	private void createJiraPanel() {
		jiraUrl = new JTextField();
		jiraUrl.setName("jiraUrl");
		jiraUsername = new JTextField();
		jiraUsername.setName("jiraUsername");

		JLabel label = new JLabel("JIRA");
		Font font = label.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		label.setFont(boldFont);
		dialog.add(label);
		dialog.add(new JSeparator(), "span 2, wrap");
		dialog.add(new JLabel("URL"));
		dialog.add(jiraUrl, "wrap");
		dialog.add(new JLabel("Username"));
		dialog.add(jiraUsername, "wrap");
	}

	public void open() {
		loadModel();
		dialog.setVisible(true);
	}

	private void loadModel() {
		captureFrequency.setValue(props.getCaptureFrequency());
		if (props.getImageEditorPath() != null) {
			imageEditorFileChooser.setSelectedFile(new File(props.getImageEditorPath()));
			imageEditorPath.setText(props.getImageEditorPath());
		}

		jiraUrl.setText(props.getJiraConfig().getInstanceUrl());
		jiraUsername.setText(props.getJiraConfig().getUsername());
		recordMouseClicks.setSelected(props.getRecordMouseClicks());
	}

	private void saveModel() {
		props.setCaptureFrequency((Double) captureFrequency.getValue());
		props.setImageEditorPath(imageEditorPath.getText());
		props.setRecordMouseClicks(recordMouseClicks.isSelected());

		String jiraRestPath = props.getJiraConfig().getRestPath();
		props.setJiraConfig(new JiraConfig(jiraUrl.getText(), jiraUsername.getText(), jiraRestPath));

		props.save();
	}
}