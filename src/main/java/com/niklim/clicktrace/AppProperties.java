package com.niklim.clicktrace;

import java.awt.Rectangle;
import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.inject.Singleton;

@Singleton
public class AppProperties {
	private static final String JIRA_USERNAME = "jira.username";
	private static final String JIRA_URL = "jira.url";
	private static final String CAPTURE_DIM_RIGHT_BOTTOM_Y = "capture.dim.rightBottomY";
	private static final String CAPTURE_DIM_RIGHT_BOTTOM_X = "capture.dim.rightBottomX";
	private static final String CAPTURE_DIM_LEFT_TOP_Y = "capture.dim.leftTopY";
	private static final String CAPTURE_DIM_LEFT_TOP_X = "capture.dim.leftTopX";
	private static final String SESSIONS_DIR = "sessions.dir";
	private static final String CAPTURE_FREQUENCY = "capture.frequency";
	private static final String IMAGE_EDITOR_PATH = "imageEditor.path";
	private static final String APP_PROPERTIES_PATH = "app.properties";

	private PropertiesConfiguration prop;

	public AppProperties() {
		try {
			prop = new PropertiesConfiguration(new File(APP_PROPERTIES_PATH));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			prop.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public String getImageEditorPath() {
		return prop.getString(IMAGE_EDITOR_PATH);
	}

	public void setImageEditorPath(String imageEditorPath) {
		prop.setProperty(IMAGE_EDITOR_PATH, imageEditorPath);
	}

	public double getCaptureFrequency() {
		return prop.getDouble(CAPTURE_FREQUENCY);
	}

	public void setCaptureFrequency(double captureFrequency) {
		prop.setProperty(CAPTURE_FREQUENCY, captureFrequency);
	}

	public String getSessionsDir() {
		return prop.getString(SESSIONS_DIR);
	}

	public void setSessionsDir(String sessionsDir) {
		prop.setProperty(SESSIONS_DIR, sessionsDir);
	}

	public Rectangle getCaptureRectangle() {
		int leftTopX = prop.getInt(CAPTURE_DIM_LEFT_TOP_X);
		int leftTopY = prop.getInt(CAPTURE_DIM_LEFT_TOP_Y);
		int rightBottomX = prop.getInt(CAPTURE_DIM_RIGHT_BOTTOM_X);
		int rightBottomY = prop.getInt(CAPTURE_DIM_RIGHT_BOTTOM_Y);

		return new Rectangle(leftTopX, leftTopY, rightBottomX - leftTopX, rightBottomY - leftTopY);
	}

	public void setCaptureRectangle(Rectangle rect) {
		prop.setProperty(CAPTURE_DIM_LEFT_TOP_X, String.valueOf(rect.getMinX()));
		prop.setProperty(CAPTURE_DIM_LEFT_TOP_Y, String.valueOf(rect.getMinY()));
		prop.setProperty(CAPTURE_DIM_RIGHT_BOTTOM_X, String.valueOf(rect.getMaxX()));
		prop.setProperty(CAPTURE_DIM_RIGHT_BOTTOM_Y, String.valueOf(rect.getMaxY()));
	}

	public JiraConfig getJiraConfig() {
		return new JiraConfig(prop.getString(JIRA_URL), prop.getString(JIRA_USERNAME));
	}

	public void setJiraConfig(JiraConfig conf) {
		prop.setProperty(JIRA_URL, conf.getUrl());
		prop.setProperty(JIRA_USERNAME, conf.getUsername());
	}

	public static class JiraConfig {
		private String url;
		private String username;

		public JiraConfig(String url, String username) {
			this.url = url;
			this.username = username;
		}

		public String getUrl() {
			return url;
		}

		public String getUsername() {
			return username;
		}

	}
}
