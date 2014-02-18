package com.niklim.clicktrace.msg;

/**
 * Constants for GUI messages.
 */
public interface ErrorMsgs {
	public static final String SESSION_NAME_ALREADY_EXIST = "Session already exists.";
	public static final String SESSION_NAME_WRONG_FOLDER = "Unable to create folder with given name.";
	public static final String SESSION_DELETE_DIR_ERROR = "Unable to delete session folder.";
	public static final String SESSION_DELETE_PROPS_ERROR = "Unable to load session properties.";
	public static final String SESSION_SAVE_PROPS_ERROR = "Unable to save session properties.";

	public static final String SCREENSHOT_DELETE_IMAGE_ERROR = "Unable to delete the screenshot.";
	public static final String SCREENSHOT_LOAD_IMAGE_ERROR = "Unable to load a screenshot.";
	public static final String SCREENSHOT_SAVE_ERROR = "Unable to save the screenshot.";
	public static final String SCREENSHOT_SAVE_FATAL_ERROR = "Error occurred while saving a screenshot. Clicktrace must be restarted to be able to record screenshots.";

	public static final String PROPS_SAVE_ERROR = "Unable to save properties file.";
	public static final String PROPS_LOAD_ERROR = "Unable to load properties file.";

	public static final String HTML_EXPORT_IO_ERROR = "Unable to export the session to the given directory.";
	public static final String UNABLE_TO_REGISTER_NATIVE_HOOK_ERROR = "Unable to register JNativeHook.";
	public static final String JIRA_EXPORT_UNKNOWN_SERVER_ERROR = "Unknown server error occurred.";
}
