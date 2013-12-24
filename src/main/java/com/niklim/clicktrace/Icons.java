package com.niklim.clicktrace;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Constants and util methods for icon images.
 */
public class Icons {
	public static final String APP = "app.png";

	public static final String SESSION_NEW = "icons/1383438310_folder_add.png";
	public static final String SESSION_OPEN = "icons/1383438320_folder.png";
	public static final String SESSION_DELETE = "icons/1383438389_folder_delete.png";
	public static final String SESSION_REFRESH = "icons/1383454049_folder.png";
	public static final String SESSION_DESCRIPTION = "icons/session_desc.png";

	public static final String START_RECORDING = "icons/media-record-5.png";
	public static final String STOP_RECORDING = "icons/media-playback-pause.png";

	public static final String SCREENSHOT_REFRESH = "icons/view-refresh-5.png";
	public static final String SCREENSHOT_EDIT = "icons/format-stroke-color.png";
	public static final String SCREENSHOT_DELETE = "icons/window-close-2.png";
	public static final String SCREENSHOT_NEXT = "icons/gnome_go_next.png";
	public static final String SCREENSHOT_PREV = "icons/gnome_go_previous.png";
	public static final String SCREENSHOT_LAST = "icons/gnome_go_last.png";
	public static final String SCREENSHOT_FIRST = "icons/gnome_go_first.png";
	public static final String SCREENSHOT_DESCRIPTION = "icons/desc_edit.png";

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
