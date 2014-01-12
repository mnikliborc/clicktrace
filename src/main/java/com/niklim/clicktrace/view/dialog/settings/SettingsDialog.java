package com.niklim.clicktrace.view.dialog.settings;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.JiraConfig;
import com.niklim.clicktrace.props.UserProperties.ViewScaling;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

@Singleton
public class SettingsDialog extends AbstractDialog {

	@Inject
	private UserProperties props;

	private CaptureAreaComponent captureAreaComponent;

	JSpinner captureFrequency;
	JTextField imageEditorPath;
	JFileChooser imageEditorFileChooser;

	JTextField jiraUrl;
	JTextField jiraUsername;

	JCheckBox captureMouseClicks;

	private JRadioButton horizontalScreenshotViewScalingRadio;
	private JRadioButton verticalScreenshotViewScalingRadio;
	private ButtonGroup screenshotViewScaling;

	@Inject
	public void init() {
		captureAreaComponent = new CaptureAreaComponent(dialog);
		dialog.getContentPane().setLayout(new MigLayout("hidemode 1", "[]rel[fill]rel[]"));
		dialog.setResizable(false);
		dialog.setTitle("Settings");

		imageEditorFileChooser = new JFileChooser();

		createSectionLabel("Recording");
		createCaptureMouseClicksPanel();
		createCaptureAreaPanel();

		createSectionLabel("Screenshot");
		createImageEditorPathPanel();
		createScreenshotViewScalingPanel();

		createSectionLabel("JIRA");
		createJiraPanel();

		dialog.add(createControlPanel("Save"), "align r, span 3");
		pack();
	}

	private void createSectionLabel(String label) {
		JLabel laabel = new JLabel(label);
		Font font = laabel.getFont();
		// same font but bold
		Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
		laabel.setFont(boldFont);
		dialog.add(laabel);
		dialog.add(new JSeparator(), "span 2, wrap");
	}

	private void createScreenshotViewScalingPanel() {
		horizontalScreenshotViewScalingRadio = new JRadioButton("full width");
		verticalScreenshotViewScalingRadio = new JRadioButton("full height");
		horizontalScreenshotViewScalingRadio.setToolTipText("show screenshots in full width");
		verticalScreenshotViewScalingRadio.setToolTipText("show screenshots in full height");

		dialog.add(new JLabel("Image display"));
		JPanel radioPanel = new JPanel(new MigLayout());

		radioPanel.add(verticalScreenshotViewScalingRadio);
		radioPanel.add(horizontalScreenshotViewScalingRadio);
		dialog.add(radioPanel, "align l, wrap");

		screenshotViewScaling = new ButtonGroup();
		screenshotViewScaling.add(horizontalScreenshotViewScalingRadio);
		screenshotViewScaling.add(verticalScreenshotViewScalingRadio);
	}

	private void createCaptureMouseClicksPanel() {
		captureMouseClicks = new JCheckBox();

		dialog.add(new JLabel("Capture mouse clicks"));
		dialog.add(captureMouseClicks, "wrap");
	}

	private void createCaptureAreaPanel() {
		captureAreaComponent.layoutWidgets(dialog);
	}

	private void createImageEditorPathPanel() {
		imageEditorPath = new JTextField();

		JButton imageEditorPathChangeButton = new JButton("set path");
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

		dialog.add(new JLabel("URL"));
		dialog.add(jiraUrl, "wrap");
		dialog.add(new JLabel("Username"));
		dialog.add(jiraUsername, "wrap");
	}

	public void open() {
		loadModel();

		center();
		pack();
		dialog.setVisible(true);
	}

	private void loadModel() {
		if (props.getImageEditorPath() != null) {
			imageEditorFileChooser.setSelectedFile(new File(props.getImageEditorPath()));
			imageEditorPath.setText(props.getImageEditorPath());
		}

		captureMouseClicks.setSelected(props.getCaptureMouseClicks());
		captureAreaComponent.initModel(props.getCaptureFullScreen(), props.getCaptureRectangle());

		jiraUrl.setText(props.getJiraConfig().getInstanceUrl());
		jiraUsername.setText(props.getJiraConfig().getUsername());

		if (props.getScreenshotViewScaling() == ViewScaling.HORIZONTAL) {
			screenshotViewScaling.setSelected(horizontalScreenshotViewScalingRadio.getModel(), true);
		} else {
			screenshotViewScaling.setSelected(verticalScreenshotViewScalingRadio.getModel(), true);
		}
	}

	private void saveModel() {
		props.setImageEditorPath(imageEditorPath.getText());

		if (screenshotViewScaling.isSelected(horizontalScreenshotViewScalingRadio.getModel())) {
			props.setScreenshotViewScaling(ViewScaling.HORIZONTAL);
		} else {
			props.setScreenshotViewScaling(ViewScaling.VERTICAL);
		}

		props.setCaptureMouseClicks(captureMouseClicks.isSelected());

		Optional<Rectangle> captureRectangleOpt = captureAreaComponent.getCaptureRectangleOpt();
		if (captureRectangleOpt.isPresent()) {
			props.setCaptureRectangle(captureRectangleOpt.get());
			props.setCaptureFullScreen(false);
		} else {
			props.setCaptureFullScreen(true);
		}

		props.setJiraConfig(new JiraConfig(jiraUrl.getText(), jiraUsername.getText()));

		props.save();
	}

	@Override
	protected void okAction() {
		saveModel();
		close();
	}
}