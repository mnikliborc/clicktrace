package com.niklim.clicktrace.view.dialog.settings;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.JiraConfig;
import com.niklim.clicktrace.props.UserProperties.ViewScaling;
import com.niklim.clicktrace.view.MainFrameHolder;
import com.niklim.clicktrace.view.dialog.AbstractDialog;
import com.niklim.clicktrace.view.dialog.settings.CaptureRectangleFrame.CaptureRectangleCallback;

@Singleton
public class SettingsDialog extends AbstractDialog {

	@Inject
	private UserProperties props;

	@Inject
	private CaptureRectangleFrame captureRectangleFrame;

	JSpinner captureFrequency;
	JTextField imageEditorPath;
	JFileChooser imageEditorFileChooser;

	JTextField jiraUrl;
	JTextField jiraUsername;

	JCheckBox captureMouseClicks;
	JCheckBox captureFullScreen;

	private JRadioButton horizontalScreenshotViewScalingRadio;
	private JRadioButton verticalScreenshotViewScalingRadio;
	private ButtonGroup screenshotViewScaling;

	private Rectangle captureRectangle;

	@Inject
	public void init() {
		dialog.getContentPane().setLayout(new MigLayout("", "[]rel[fill]rel[]"));
		dialog.setResizable(false);
		dialog.setTitle("Settings");

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setBounds((int) (dim.getWidth() / 2) - 300, (int) (dim.getHeight() / 2) - 200, 450, 310);

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
		captureFullScreen = new JCheckBox("full screen");

		final JButton changeRectangle = new JButton("change");
		final CaptureRectangleCallback callback = new CaptureRectangleCallback() {
			public void done(Optional<Rectangle> rOpt) {
				if (rOpt.isPresent()) {
					captureRectangle = rOpt.get();
					captureFullScreen.setSelected(false);
				} else {
					if (captureRectangle == null) {
						captureFullScreen.setSelected(true);
					}
				}

				MainFrameHolder.get().setVisible(true);
				dialog.setVisible(true);
			}
		};
		final ActionListener rectangleChanger = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(dialog, "Press Enter to save or Esc to cancel");
				dialog.setVisible(false);
				MainFrameHolder.get().setVisible(false);

				captureRectangleFrame.open(captureRectangle, callback);
			}

		};

		changeRectangle.addActionListener(rectangleChanger);

		captureFullScreen.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				changeRectangle.setEnabled(!captureFullScreen.isSelected());
				if (!captureFullScreen.isSelected() && captureRectangle == null) {
					rectangleChanger.actionPerformed(null);
				}
			}
		});

		dialog.add(new JLabel("Capture area"));
		dialog.add(captureFullScreen);
		dialog.add(changeRectangle, "wrap");
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
		if (props.getImageEditorPath() != null) {
			imageEditorFileChooser.setSelectedFile(new File(props.getImageEditorPath()));
			imageEditorPath.setText(props.getImageEditorPath());
		}

		captureMouseClicks.setSelected(props.getCaptureMouseClicks());
		captureFullScreen.setSelected(props.getCaptureFullScreen());
		captureRectangle = props.getCaptureRectangle();

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
		props.setCaptureFullScreen(captureFullScreen.isSelected());

		if (captureRectangle != null) {
			props.setCaptureRectangle(captureRectangle);
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