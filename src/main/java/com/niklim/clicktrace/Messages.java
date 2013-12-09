package com.niklim.clicktrace;

public interface Messages {
	public static final String NO_EDITOR_PATH_SET = "What image editing program do you want to use? Please set its path and try again.";
	public static final String SESSION_NAME_ALREADY_EXIST = "Session already exists.";
	public static final String SESSION_NAME_WRONG_FOLDER = "Can not create folder with given name.";

	public static final String EXPORT_SUCCESS = "Session successfully exported to JIRA.";
	public static final Object EXPORT_ISSUE_KEY_EMPTY = "Issue key must not be empty.";
}
