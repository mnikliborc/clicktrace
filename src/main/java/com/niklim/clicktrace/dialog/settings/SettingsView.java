package com.niklim.clicktrace.dialog.settings;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.dialog.AbstractDialogView;

public class SettingsView extends AbstractDialogView {
	CaptureAreaComponent captureAreaComponent;
	AdvancedSettingsComponent advancedSettingsComponent;

	JTextField imageEditorPath;
	JFileChooser imageEditorFileChooser;

	JTextField jiraUrlTextField;
	JTextField jiraUsernameTextField;

	JCheckBox captureMouseClicks;
	JCheckBox captureSelectAll;

	JRadioButton horizontalScreenshotViewScalingRadio;
	JRadioButton verticalScreenshotViewScalingRadio;
	ButtonGroup screenshotViewScaling;

	JFormattedTextField imageExportWidth;

	public SettingsView() {
		dialog.getContentPane().setLayout(new MigLayout("hidemode 1", "[]rel[fill]rel[]"));
		dialog.setResizable(false);
		dialog.setTitle("Settings");

		imageEditorFileChooser = new JFileChooser();

		createSectionLabel("Recording");
		createCaptureMouseClicksPanel();
		createCaptureAreaComponent();
		createCaptureSelectAllPanel();

		createSpacePanel();

		createSectionLabel("Editing");
		createImageEditorPathPanel();
		createScreenshotViewScalingPanel();

		createSpacePanel();

		createSectionLabel("Export");
		createImageExportWidthPanel();

		createSpacePanel();

		createSectionLabel("JIRA");
		createJiraPanel();

		createSpacePanel();

		createAdvancedPanel();

		dialog.add(createControlPanel("Save"), "align r, span 3");
	}

	private void createAdvancedPanel() {
		advancedSettingsComponent = new AdvancedSettingsComponent(dialog);
	}

	private void createSpacePanel() {
		JPanel panel = new JPanel();
		dialog.add(panel, "h 10, span 3, wrap");
	}

	private void createImageExportWidthPanel() {
		imageExportWidth = new JFormattedTextField();
		NumberFormat longFormat = NumberFormat.getIntegerInstance();
		longFormat.setGroupingUsed(false);

		NumberFormatter numberFormatter = new NumberFormatter(longFormat);
		numberFormatter.setAllowsInvalid(false);
		numberFormatter.setMinimum(0);
		numberFormatter.setMaximum(9999);

		imageExportWidth = new JFormattedTextField(numberFormatter);
		dialog.add(new JLabel("Image initial width [px]"));
		dialog.add(imageExportWidth, "wrap");
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
		JPanel radioPanel = new JPanel(new MigLayout("fill, insets 0"));

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

	private void createCaptureSelectAllPanel() {
		captureSelectAll = new JCheckBox();

		String tooltip = "Select all screenshots on stop recording";
		JLabel label = new JLabel("Select all screenshots");
		label.setToolTipText(tooltip);
		dialog.add(label);
		captureSelectAll.setToolTipText(tooltip);
		dialog.add(captureSelectAll, "wrap");
	}

	private void createCaptureAreaComponent() {
		captureAreaComponent = new CaptureAreaComponent(dialog);
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
		jiraUrlTextField = new JTextField();
		jiraUrlTextField.setName("jiraUrl");
		jiraUsernameTextField = new JTextField();
		jiraUsernameTextField.setName("jiraUsername");

		dialog.add(new JLabel("URL"));
		dialog.add(jiraUrlTextField, "wrap");
		dialog.add(new JLabel("Username"));
		dialog.add(jiraUsernameTextField, "wrap");
	}
}
