package com.niklim.clicktrace.props;

import java.io.File;

import com.google.common.base.Strings;
import com.google.inject.Singleton;

@Singleton
public class UserProperties extends AbstractProperties {
	private static final String USER_PROPERTIES_PATH = "user.properties";

	private static final String JIRA_USERNAME = "jira.username";
	private static final String JIRA_INSTANCE_URL = "jira.instanceUrl";
	private static final String JIRA_REST_PATH = "jira.restPath";
	private static final String CAPTURE_FREQUENCY = "capture.frequency";
	private static final String IMAGE_EDITOR_PATH = "imageEditor.path";
	private static final String RECORD_CLICKS = "capture.recordClicks";
	private static final String LAST_SESSION = "lastSession";
	private static final String SCREENSHOT_VIEW_SCALING = "screenshot.view.scaling";

	static {
		defaults.put(CAPTURE_FREQUENCY, 1.0);
		defaults.put(RECORD_CLICKS, true);
		defaults.put(JIRA_REST_PATH, "/rest/clicktrace/1.0");
		defaults.put(SCREENSHOT_VIEW_SCALING, ViewScaling.HORIZONTAL.name());
	}

	public UserProperties() {
		super();
	}

	public static enum ViewScaling {
		HORIZONTAL, VERTICAL;
	}

	protected File getPropertiesFilePath() {
		return new File(USER_PROPERTIES_PATH);
	}

	public String getImageEditorPath() {
		return props.getString(IMAGE_EDITOR_PATH);
	}

	public void setImageEditorPath(String imageEditorPath) {
		props.setProperty(IMAGE_EDITOR_PATH, imageEditorPath);
	}

	public double getCaptureFrequency() {
		return props.getDouble(CAPTURE_FREQUENCY);
	}

	public void setCaptureFrequency(double captureFrequency) {
		props.setProperty(CAPTURE_FREQUENCY, captureFrequency);
	}

	public JiraConfig getJiraConfig() {
		return new JiraConfig(props.getString(JIRA_INSTANCE_URL), props.getString(JIRA_USERNAME),
				props.getString(JIRA_REST_PATH));
	}

	public void setJiraConfig(JiraConfig conf) {
		props.setProperty(JIRA_INSTANCE_URL, conf.getInstanceUrl());
		props.setProperty(JIRA_USERNAME, conf.getUsername());
	}

	public static class JiraConfig {
		private String instanceUrl;
		private String restPath;
		private String username;

		public JiraConfig(String url, String username, String restPath) {
			this.instanceUrl = url;
			this.username = username;
		}

		public String getInstanceUrl() {
			return instanceUrl;
		}

		public String getUsername() {
			return username;
		}

		public String getRestPath() {
			return Strings.nullToEmpty(restPath);
		}
	}

	public boolean getRecordMouseClicks() {
		return props.getBoolean(RECORD_CLICKS);
	}

	public void setRecordMouseClicks(boolean record) {
		props.setProperty(RECORD_CLICKS, record);
	}

	public String getLastSessionName() {
		return props.getString(LAST_SESSION);
	}

	public void setLastSessionName(String sessionName) {
		props.setProperty(LAST_SESSION, sessionName);
	}

	public ViewScaling getScreenshotViewScaling() {
		return ViewScaling.valueOf(props.getString(SCREENSHOT_VIEW_SCALING));
	}

	public void setScreenshotViewScaling(ViewScaling scaling) {
		props.setProperty(SCREENSHOT_VIEW_SCALING, scaling.name());
	}
}
