package com.niklim.clicktrace.props;

import java.awt.Rectangle;
import java.io.File;
import java.util.NoSuchElementException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.capture.voter.LineVoter;
import com.niklim.clicktrace.capture.voter.LineVoter.ChangeSensitivity;

@Singleton
public class UserProperties extends AbstractProperties {
	@Inject
	private AppProperties appProps;

	private static final String LAST_SESSION = "sessions.last";
	private static final String SESSIONS_DIR_PATH = "sessions.dirPath";

	private static final String JIRA_USERNAME = "jira.username";
	private static final String JIRA_INSTANCE_URL = "jira.instanceUrl";

	private static final String IMAGE_EDITOR_PATH = "imageEditor.path";
	private static final String SCREENSHOT_VIEW_SCALING = "screenshot.view.scaling";

	private static final String CAPTURE_CLICKS = "capture.recordClicks";
	private static final String CAPTURE_FULLSCREEN = "capture.dimension.fullscreen";
	private static final String CAPTURE_SELECT_ALL = "capture.stop.selectAllShots";
	private static final String CAPTURE_SENSITIVITY = "capture.sensitivity";

	private static final String CAPTURE_RECTANGLE_HEIGHT = "capture.dimension.height";
	private static final String CAPTURE_RECTANGLE_WIDTH = "capture.dimension.width";
	private static final String CAPTURE_RECTANGLE_Y = "capture.dimension.y";
	private static final String CAPTURE_RECTANGLE_X = "capture.dimension.x";

	private static final String HTML_EXPORT_LAST_PATH = "export.html.lastPath";
	private static final String EXPORT_IMAGE_WIDTH = "export.imageWidth";

	private static final String MARKUP_SYNTAX = "markup.syntax";

	static {
		defaults.put(CAPTURE_CLICKS, true);
		defaults.put(CAPTURE_FULLSCREEN, true);
		defaults.put(CAPTURE_SELECT_ALL, false);
		defaults.put(SCREENSHOT_VIEW_SCALING, ViewScaling.VERTICAL.name());
		defaults.put(EXPORT_IMAGE_WIDTH, 800);
		defaults.put(CAPTURE_SENSITIVITY, ChangeSensitivity.NORMAL.name());
		defaults.put(MARKUP_SYNTAX, MarkupSyntax.CONFLUENCE.name());
		defaults.put(SESSIONS_DIR_PATH, "sessions");
	}

	@Inject
	protected void init() {
		super.init();
	}

	public static enum ViewScaling {
		HORIZONTAL, VERTICAL;
	}

	public static enum MarkupSyntax {
		CONFLUENCE, MARKDOWN;
	}

	protected File getPropertiesFilePath() {
		return new File(appProps.getUserPropertiesPath());
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

	public MarkupSyntax getMarkupSyntax() {
		return MarkupSyntax.valueOf(props.getString(MARKUP_SYNTAX));
	}

	public void setMarkupSyntax(MarkupSyntax syntax) {
		props.setProperty(MARKUP_SYNTAX, syntax.name());
	}

	public boolean getCaptureFullScreen() {
		return props.getBoolean(CAPTURE_FULLSCREEN);
	}

	public void setCaptureFullScreen(boolean fullScreen) {
		props.setProperty(CAPTURE_FULLSCREEN, fullScreen);
	}

	public boolean getCaptureSelectAll() {
		return props.getBoolean(CAPTURE_SELECT_ALL);
	}

	public void setCaptureSelectAll(boolean selectAll) {
		props.setProperty(CAPTURE_SELECT_ALL, selectAll);
	}

	public String getHtmlExportLastPath() {
		return props.getString(HTML_EXPORT_LAST_PATH);
	}

	public void setHtmlExportLastPath(String path) {
		props.setProperty(HTML_EXPORT_LAST_PATH, path);
	}

	public String getSessionsDirPath() {
		String rawSessionsPath = props.getString(SESSIONS_DIR_PATH);
		if (rawSessionsPath.endsWith(File.separator)) {
			return rawSessionsPath;
		} else {
			return rawSessionsPath + File.separator;
		}
	}

	public void setSessionsDirPath(String path) {
		props.setProperty(SESSIONS_DIR_PATH, path);
	}

	public Integer getExportImageWidth() {
		return props.getInt(EXPORT_IMAGE_WIDTH);
	}

	public void setExportImageWidth(Integer w) {
		props.setProperty(EXPORT_IMAGE_WIDTH, w);
	}

	public LineVoter.ChangeSensitivity getCaptureSensitivity() {
		return LineVoter.ChangeSensitivity.valueOf(props.getString(CAPTURE_SENSITIVITY));
	}

	public void setCaptureSensitivity(LineVoter.ChangeSensitivity sensitivity) {
		props.setProperty(CAPTURE_SENSITIVITY, sensitivity.name());
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
