package com.niklim.clicktrace.dialog.settings;

import java.awt.Rectangle;
import java.io.File;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.dialog.AbstractDialog;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.props.UserProperties.ViewScaling;

@Singleton
public class SettingsDialog extends AbstractDialog<SettingsView> {

	@Inject
	private UserProperties props;

	@Inject
	public void init() {
		postInit();
	}

	public void open() {
		loadModel();

		center();
		view.dialog().setVisible(true);
	}

	private void loadModel() {
		loadRecordingModel();
		loadExportModel();
		loadEditingModel();
		loadJiraModel();
		loadAdvancedModel();
	}

	private void loadAdvancedModel() {
		view.advancedSettingsComponent.init(props.getCaptureSensitivity(), props.getMarkupSyntax());
	}

	private void loadJiraModel() {
		String url = Strings.nullToEmpty(props.getJiraConfig().getInstanceUrl());
		view.jiraUrlTextField.setText(url);
		view.jiraUsernameTextField.setText(props.getJiraConfig().getUsername());
	}

	private void loadExportModel() {
		view.imageExportWidth.setText(String.valueOf(props.getExportImageWidth()));
	}

	private void loadEditingModel() {
		if (props.getImageEditorPath() != null) {
			view.imageEditorFileChooser.setSelectedFile(new File(props.getImageEditorPath()));
			view.imageEditorPath.setText(props.getImageEditorPath());
		}

		if (props.getScreenshotViewScaling() == ViewScaling.HORIZONTAL) {
			view.screenshotViewScaling.setSelected(view.horizontalScreenshotViewScalingRadio.getModel(), true);
		} else {
			view.screenshotViewScaling.setSelected(view.verticalScreenshotViewScalingRadio.getModel(), true);
		}
	}

	private void loadRecordingModel() {
		view.captureMouseClicks.setSelected(props.getCaptureMouseClicks());
		view.captureAreaComponent.init(props.getCaptureFullScreen(), props.getCaptureRectangle());
		view.captureSelectAll.setSelected(props.getCaptureSelectAll());
	}

	private void saveModel() {
		saveEditingModel();
		saveExportModel();
		saveRecordingModel();
		saveJiraModel();
		saveAdvancedModel();

		props.save();
	}

	private void saveAdvancedModel() {
		props.setCaptureSensitivity(view.advancedSettingsComponent.getChangeSensitivity());
		props.setMarkupSyntax(view.advancedSettingsComponent.getMarkupSyntax());
	}

	private void saveJiraModel() {
		props.setJiraConfig(new JiraConfig(view.jiraUrlTextField.getText(), view.jiraUsernameTextField.getText()));
	}

	private void saveExportModel() {
		props.setExportImageWidth(Integer.valueOf(view.imageExportWidth.getText()));
	}

	private void saveRecordingModel() {
		props.setCaptureMouseClicks(view.captureMouseClicks.isSelected());
		props.setCaptureSelectAll(view.captureSelectAll.isSelected());

		Optional<Rectangle> captureRectangleOpt = view.captureAreaComponent.getCaptureRectangleOpt();
		if (captureRectangleOpt.isPresent()) {
			props.setCaptureRectangle(captureRectangleOpt.get());
			props.setCaptureFullScreen(false);
		} else {
			props.setCaptureFullScreen(true);
		}
	}

	private void saveEditingModel() {
		props.setImageEditorPath(view.imageEditorPath.getText());

		if (view.screenshotViewScaling.isSelected(view.horizontalScreenshotViewScalingRadio.getModel())) {
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
		view.captureAreaComponent.clear();
	}

	@Override
	protected SettingsView createView() {
		return new SettingsView();
	}
}