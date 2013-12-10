package com.niklim.clicktrace;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.inject.Singleton;

@Singleton
public class UserProperties extends AbstractProperties {
	private static final String USER_PROPERTIES_PATH = "user.properties";

	private static final String JIRA_USERNAME = "jira.username";
	private static final String JIRA_INSTANCE_URL = "jira.instanceUrl";
	private static final String JIRA_REST_PATH = "jira.restPath";
	// private static final String CAPTURE_DIM_RIGHT_BOTTOM_Y =
	// "capture.dim.rightBottomY";
	// private static final String CAPTURE_DIM_RIGHT_BOTTOM_X =
	// "capture.dim.rightBottomX";
	// private static final String CAPTURE_DIM_LEFT_TOP_Y =
	// "capture.dim.leftTopY";
	// private static final String CAPTURE_DIM_LEFT_TOP_X =
	// "capture.dim.leftTopX";
	// private static final String SESSIONS_DIR = "sessions.dir";
	private static final String CAPTURE_FREQUENCY = "capture.frequency";
	private static final String IMAGE_EDITOR_PATH = "imageEditor.path";
	private static final String RECORD_CLICKS = "capture.recordClicks";
	private static final String LAST_SESSION = "lastSession";

	private static final Map<String, Object> defaults;
	static {
		defaults = new HashMap<String, Object>();
		defaults.put(CAPTURE_FREQUENCY, 1.0);
		defaults.put(RECORD_CLICKS, true);
		defaults.put(JIRA_REST_PATH, "/rest/clicktrace/1.0");
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
		BigDecimal freq = props.getBigDecimal(CAPTURE_FREQUENCY);
		if (freq == null) {
			return 1.0;
		} else {
			return freq.doubleValue();
		}
	}

	public void setCaptureFrequency(double captureFrequency) {
		props.setProperty(CAPTURE_FREQUENCY, captureFrequency);
	}

	// public String getSessionsDir() {
	// return prop.getString(SESSIONS_DIR);
	// }
	//
	// public void setSessionsDir(String sessionsDir) {
	// prop.setProperty(SESSIONS_DIR, sessionsDir);
	// }

	// public Rectangle getCaptureRectangle() {
	// int leftTopX = prop.getInt(CAPTURE_DIM_LEFT_TOP_X);
	// int leftTopY = prop.getInt(CAPTURE_DIM_LEFT_TOP_Y);
	// int rightBottomX = prop.getInt(CAPTURE_DIM_RIGHT_BOTTOM_X);
	// int rightBottomY = prop.getInt(CAPTURE_DIM_RIGHT_BOTTOM_Y);
	//
	// return new Rectangle(leftTopX, leftTopY, rightBottomX - leftTopX,
	// rightBottomY - leftTopY);
	// }
	//
	// public void setCaptureRectangle(Rectangle rect) {
	// prop.setProperty(CAPTURE_DIM_LEFT_TOP_X, String.valueOf(rect.getMinX()));
	// prop.setProperty(CAPTURE_DIM_LEFT_TOP_Y, String.valueOf(rect.getMinY()));
	// prop.setProperty(CAPTURE_DIM_RIGHT_BOTTOM_X,
	// String.valueOf(rect.getMaxX()));
	// prop.setProperty(CAPTURE_DIM_RIGHT_BOTTOM_Y,
	// String.valueOf(rect.getMaxY()));
	// }

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
		String rec = props.getString(RECORD_CLICKS);
		if (rec == null) {
			return true;
		} else {
			return Boolean.valueOf(rec);
		}
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
}
