package com.niklim.clicktrace.msg;

public interface InfoMsgs {

	public static final String CONFIG_NO_EDITOR_PATH = "What image editing program do you want to use? Please set its path and try again.";

	public static final String JIRA_EXPORT_SUCCESS = "Session successfully exported to Issue {0}.";
	public static final String JIRA_EXPORT_ISSUE_KEY_EMPTY = "Issue key must not be empty.";
	public static final String JIRA_EXPORT_AUTHENTICATION_FAILURE = "Wrong username or password.";
	public static final String JIRA_EXPORT_WRONG_URL = "Wrong JIRA URL.";
	public static final String JIRA_EXPORT_CAPTCHA_NEEDED = "It looks like we have to fill CAPTCHA to log in JIRA. Please go to your JIRA and log in.";
	public static final String JIRA_EXPORT_NO_SUMMARY = "Please provide Issue summary.";

	public static final String HTML_EXPORT_SUCCESS = "Session successfully exported to HTML.";
	public static final String HTML_EXPORT_FOLDER_NOT_EXISTS = "Directory does not exist.";

	public static final String SESSION_NO_NAME = "Please provide session name.";

	public static final String EDITOR_APP_ERROR = "An error occured while opening the screenshot image.";

}