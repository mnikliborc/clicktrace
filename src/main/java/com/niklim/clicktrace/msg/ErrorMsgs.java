package com.niklim.clicktrace.msg;

/**
 * Constants for GUI messages.
 */
public interface ErrorMsgs {
	public static final String SESSION_NAME_ALREADY_EXIST = "Session already exists.";
	public static final String SESSION_NAME_WRONG_FOLDER = "Can not create folder with given name.";
	public static final String SESSION_DELETE_DIR_ERROR = "Unable to delete session folder";
	public static final String SESSION_DELETE_PROPS_ERROR = "Unable to load session properties configuration.";
	public static final String SESSION_SAVE_PROPS_ERROR = "Unable to save session properties.";

	public static final String SCREENSHOT_DELETE_IMAGE_ERROR = "Unable to delete screenshot image.";
	public static final String SCREENSHOT_LOAD_IMAGE_ERROR = "Unable to load screenshot image.";
	public static final String SCREENSHOT_SAVE_ERROR = "Error occured while saving the screenshot.";

	public static final String PROPS_SAVE_ERROR = "Unable to save properties file.";
	public static final String PROPS_LOAD_ERROR = "Unable to load properties file.";

	public static final String HTML_EXPORT_IO_ERROR = "Unable to export session to given directory.";
	public static final String UNABLE_TO_REGISTER_NATIVE_HOOK_ERROR = "Unable to register JNativeHook.";
	public static final String JIRA_EXPORT_UNKNOWN_SERVER_ERROR = "Unknown server error occured.";
}
