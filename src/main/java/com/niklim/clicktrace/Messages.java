package com.niklim.clicktrace;

/**
 * Constants for GUI messages.
 */
public interface Messages {
	public static final String NO_EDITOR_PATH_SET = "What image editing program do you want to use? Please set its path and try again.";
	public static final String SESSION_NAME_ALREADY_EXIST = "Session already exists.";
	public static final String SESSION_NAME_WRONG_FOLDER = "Can not create folder with given name.";

	public static final String EXPORT_SUCCESS = "Session successfully exported to JIRA.";
	public static final Object EXPORT_ISSUE_KEY_EMPTY = "Issue key must not be empty.";
	public static final String EXPORT_UNKNOWN_SERVER_ERROR = "Unknown server error occured.";
	public static final String EXPORT_AUTHORIZATION_ERROR = "Wrong username or password.";
	public static final String EXPORT_WRONG_URL_ERROR = "Wrong JIRA URL.";
	public static final String EXPORT_CAPTCHA_ERROR = "Sorry dude, it looks like you have to fill CAPTCHA to log in JIRA. Please go to your JIRA and log in.";
	public static final String SCREENSHOT_SAVE_ERROR = "Error occured while saving the screenshot.";
}
