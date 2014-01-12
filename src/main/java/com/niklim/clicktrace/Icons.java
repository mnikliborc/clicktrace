package com.niklim.clicktrace;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Constants and util methods for icon images.
 */
public class Icons {
	public static final String APP = "app.png";

	public static final String SESSION_NEW = "icons/add_folder.png";
	public static final String SESSION_OPEN = "icons/open_folder.png";
	public static final String SESSION_DELETE = "icons/delete_folder.png";
	public static final String SESSION_REFRESH = "icons/refresh_folder.png";
	public static final String SESSION_DESCRIPTION = "icons/desc_folder.png";
	public static final String SESSION_REORDER = "icons/reorder.png";

	public static final String START_RECORDING = "icons/media_record.png";
	public static final String STOP_RECORDING = "icons/Stop-icon.png";

	public static final String SCREENSHOT_REFRESH = "icons/refresh.png";
	public static final String SCREENSHOT_EDIT = "icons/format-stroke-color.png";
	public static final String SCREENSHOT_DELETE = "icons/delete.png";
	public static final String SCREENSHOT_NEXT = "icons/next.png";
	public static final String SCREENSHOT_PREV = "icons/prev.png";
	public static final String SCREENSHOT_LAST = "icons/last.png";
	public static final String SCREENSHOT_FIRST = "icons/first.png";
	public static final String SCREENSHOT_DESCRIPTION = "icons/edit.png";

	public static final String SEARCH = "icons/search.png";

	public static final String MOUSE_MARK_RED_LEFT = "icons/mouse_red_left.png";
	public static final String MOUSE_MARK_RED_RIGHT = "icons/mouse_red_right.png";
	public static final String MOUSE_MARK_GREEN = "icons/mouse_green.png";

	public static Image createIconImage(String path, String description) {
		URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
}
