package com.niklim.clicktrace.view.dialog.settings;

import java.awt.Font;
import java.awt.Rectangle;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.voter.LineVoter.ChangeSensitivity;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.ViewScaling;
import com.niklim.clicktrace.view.dialog.AbstractDialog;

@Singleton
public class SettingsDialog extends AbstractDialog {

	private static final String JIRA_URL_PREFIX = "http://";

	@Inject
	private UserProperties props;

	private CaptureAreaComponent captureAreaComponent;

	private JSpinner captureFrequency;
	private JTextField imageEditorPath;
	private JFileChooser imageEditorFileChooser;

	private JTextField jiraUrlTextField;
	private JTextField jiraUsernameTextField;

	private JCheckBox captureMouseClicks;
	private JCheckBox captureSelectAll;

	private JRadioButton horizontalScreenshotViewScalingRadio;
	private JRadioButton verticalScreenshotViewScalingRadio;
	private ButtonGroup screenshotViewScaling;

	private JFormattedTextField imageExportWidth;

	private JRadioButton changeSensitivityHighRadio;
	private JRadioButton changeSensitivityNormalRadio;
	private JRadioButton changeSensitivityLowRadio;
	private ButtonGroup changeSensitivityRadioGroup;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout("hidemode 1", "[]rel[fill]rel[]"));
		dialog.setResizable(false);
		dialog.setTitle("Settings");

		imageEditorFileChooser = new JFileChooser();

		createSectionLabel("Recording");
		createCaptureMouseClicksPanel();
		createCaptureAreaComponent();
		createCaptureSelectAllPanel();
		createCaptureChangeDetectionLevelPanel();

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

		dialog.add(createControlPanel("Save"), "align r, span 3");

		postInit();
	}

	private void createSpacePanel() {
		JPanel panel = new JPanel();
		dialog.add(panel, "h 10, span 3, wrap");
	}

	private void createCaptureChangeDetectionLevelPanel() {
		changeSensitivityHighRadio = new JRadioButton("high");
		changeSensitivityNormalRadio = new JRadioButton("normal");
		changeSensitivityLowRadio = new JRadioButton("low");

		JLabel label = new JLabel("Change sensitivity");
		label.setToolTipText("How sensitive change detection is.");
		dialog.add(label);
		JPanel radioPanel = new JPanel(new MigLayout("fill, insets 0"));

		radioPanel.add(changeSensitivityHighRadio);
		radioPanel.add(changeSensitivityNormalRadio);
		radioPanel.add(changeSensitivityLowRadio);
		dialog.add(radioPanel, "grow, wrap");

		changeSensitivityRadioGroup = new ButtonGroup();
		changeSensitivityRadioGroup.add(changeSensitivityHighRadio);
		changeSensitivityRadioGroup.add(changeSensitivityNormalRadio);
		changeSensitivityRadioGroup.add(changeSensitivityLowRadio);
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

		JPanel urlPanel = new JPanel(new MigLayout("insets 5 0 5 0", "[]push[]"));
		urlPanel.add(new JLabel("URL"));
		urlPanel.add(new JLabel(JIRA_URL_PREFIX));

		dialog.add(urlPanel, "grow, h 5");
		dialog.add(jiraUrlTextField, "wrap");
		dialog.add(new JLabel("Username"));
		dialog.add(jiraUsernameTextField, "wrap");
	}

	public void open() {
		loadModel();

		center();
		dialog.setVisible(true);
	}

	private void loadModel() {
		loadRecordingModel();
		loadExportModel();
		loadEditingModel();
		loadJiraModel();
	}

	private void loadJiraModel() {
		String url = Strings.nullToEmpty(props.getJiraConfig().getInstanceUrl());
		jiraUrlTextField.setText(url.replaceFirst(JIRA_URL_PREFIX, ""));
		jiraUsernameTextField.setText(props.getJiraConfig().getUsername());
	}

	private void loadExportModel() {
		imageExportWidth.setText(String.valueOf(props.getHtmlExportImageWidth()));
	}

	private void loadEditingModel() {
		if (props.getImageEditorPath() != null) {
			imageEditorFileChooser.setSelectedFile(new File(props.getImageEditorPath()));
			imageEditorPath.setText(props.getImageEditorPath());
		}

		if (props.getScreenshotViewScaling() == ViewScaling.HORIZONTAL) {
			screenshotViewScaling.setSelected(horizontalScreenshotViewScalingRadio.getModel(), true);
		} else {
			screenshotViewScaling.setSelected(verticalScreenshotViewScalingRadio.getModel(), true);
		}
	}

	private void loadRecordingModel() {
		captureMouseClicks.setSelected(props.getCaptureMouseClicks());
		captureAreaComponent.init(props.getCaptureFullScreen(), props.getCaptureRectangle());
		captureSelectAll.setSelected(props.getCaptureSelectAll());

		if (props.getCaptureSensitivity() == ChangeSensitivity.HIGH) {
			changeSensitivityRadioGroup.setSelected(changeSensitivityHighRadio.getModel(), true);
		} else if (props.getCaptureSensitivity() == ChangeSensitivity.NORMAL) {
			changeSensitivityRadioGroup.setSelected(changeSensitivityNormalRadio.getModel(), true);
		} else if (props.getCaptureSensitivity() == ChangeSensitivity.LOW) {
			changeSensitivityRadioGroup.setSelected(changeSensitivityLowRadio.getModel(), true);
		}
	}

	private void saveModel() {
		saveEditingModel();
		saveExportModel();
		saveRecordingModel();
		saveJiraModel();

		props.save();
	}

	private void saveJiraModel() {
		props.setJiraConfig(new JiraConfig(JIRA_URL_PREFIX + jiraUrlTextField.getText(), jiraUsernameTextField
				.getText()));
	}

	private void saveExportModel() {
		props.setHtmlExportImageWidth(Integer.valueOf(imageExportWidth.getText()));
	}

	private void saveRecordingModel() {
		props.setCaptureMouseClicks(captureMouseClicks.isSelected());
		props.setCaptureSelectAll(captureSelectAll.isSelected());

		Optional<Rectangle> captureRectangleOpt = captureAreaComponent.getCaptureRectangleOpt();
		if (captureRectangleOpt.isPresent()) {
			props.setCaptureRectangle(captureRectangleOpt.get());
			props.setCaptureFullScreen(false);
		} else {
			props.setCaptureFullScreen(true);
		}

		if (changeSensitivityRadioGroup.isSelected(changeSensitivityHighRadio.getModel())) {
			props.setCaptureSensitivity(ChangeSensitivity.HIGH);
		} else if (changeSensitivityRadioGroup.isSelected(changeSensitivityNormalRadio.getModel())) {
			props.setCaptureSensitivity(ChangeSensitivity.NORMAL);
		} else if (changeSensitivityRadioGroup.isSelected(changeSensitivityLowRadio.getModel())) {
			props.setCaptureSensitivity(ChangeSensitivity.LOW);
		}
	}

	private void saveEditingModel() {
		props.setImageEditorPath(imageEditorPath.getText());

		if (screenshotViewScaling.isSelected(horizontalScreenshotViewScalingRadio.getModel())) {
			props.setScreenshotViewScaling(ViewScaling.HORIZONTAL);
		} else {
			props.setScreenshotViewScaling(ViewScaling.VERTICAL);
		}
	}

	@Override
	protected void okAction() {
		saveModel();
		close();
	}

	@Override
	public void close() {
		super.close();
		captureAreaComponent.clear();
	}
}