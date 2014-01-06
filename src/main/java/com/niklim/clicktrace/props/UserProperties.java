package com.niklim.clicktrace.props;

import java.awt.Rectangle;
import java.io.File;
import java.util.NoSuchElementException;

import com.google.inject.Singleton;

@Singleton
public class UserProperties extends AbstractProperties {
	private static final String USER_PROPERTIES_PATH = "user.properties";

	private static final String LAST_SESSION = "lastSession";

	private static final String JIRA_USERNAME = "jira.username";
	private static final String JIRA_INSTANCE_URL = "jira.instanceUrl";

	private static final String IMAGE_EDITOR_PATH = "imageEditor.path";
	private static final String SCREENSHOT_VIEW_SCALING = "screenshot.view.scaling";

	private static final String CAPTURE_CLICKS = "capture.recordClicks";
	private static final String CAPTURE_FULLSCREEN = "capture.dimension.fullscreen";

	private static final String CAPTURE_RECTANGLE_HEIGHT = "capture.dimension.height";
	private static final String CAPTURE_RECTANGLE_WIDTH = "capture.dimension.width";
	private static final String CAPTURE_RECTANGLE_Y = "capture.dimension.y";
	private static final String CAPTURE_RECTANGLE_X = "capture.dimension.x";

	static {
		defaults.put(CAPTURE_CLICKS, true);
		defaults.put(CAPTURE_FULLSCREEN, true);
		defaults.put(SCREENSHOT_VIEW_SCALING, ViewScaling.VERTICAL.name());
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

	public JiraConfig getJiraConfig() {
		return new JiraConfig(props.getString(JIRA_INSTANCE_URL), props.getString(JIRA_USERNAME));
	}

	public void setJiraConfig(JiraConfig conf) {
		props.setProperty(JIRA_INSTANCE_URL, conf.getInstanceUrl());
		props.setProperty(JIRA_USERNAME, conf.getUsername());
	}

	public static class JiraConfig {
		private String instanceUrl;
		private String username;

		public JiraConfig(String url, String username) {
			this.instanceUrl = url;
			this.username = username;
		}

		public String getInstanceUrl() {
			return instanceUrl;
		}

		public String getUsername() {
			return username;
		}

	}

	public boolean getCaptureMouseClicks() {
		return props.getBoolean(CAPTURE_CLICKS);
	}

	public void setCaptureMouseClicks(boolean record) {
		props.setProperty(CAPTURE_CLICKS, record);
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

	public boolean getCaptureFullScreen() {
		return props.getBoolean(CAPTURE_FULLSCREEN);
	}

	public void setCaptureFullScreen(boolean fullScreen) {
		props.setProperty(CAPTURE_FULLSCREEN, fullScreen);
	}

	public Rectangle getCaptureRectangle() {
		try {
			int x = props.getInt(CAPTURE_RECTANGLE_X);
			int y = props.getInt(CAPTURE_RECTANGLE_Y);
			int width = props.getInt(CAPTURE_RECTANGLE_WIDTH);
			int height = props.getInt(CAPTURE_RECTANGLE_HEIGHT);
			return new Rectangle(x, y, width, height);
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	public void setCaptureRectangle(Rectangle r) {
		props.setProperty(CAPTURE_RECTANGLE_X, r.x);
		props.setProperty(CAPTURE_RECTANGLE_Y, r.y);
		props.setProperty(CAPTURE_RECTANGLE_WIDTH, r.width);
		props.setProperty(CAPTURE_RECTANGLE_HEIGHT, r.height);
	}
}
