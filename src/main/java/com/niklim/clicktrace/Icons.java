package com.niklim.clicktrace;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class Icons {
	public static final String NEW_SESSION = "icons/1383438310_folder_add.png";
	public static final String OPEN_SESSION = "icons/1383438320_folder.png";
	public static final String DELETE_SESSION = "icons/1383438389_folder_delete.png";
	public static final String REFRESH_SESSION = "icons/1383454049_folder.png";
	public static final String REFRESH_SCREENSHOT = "icons/view-refresh-5.png";
	public static final String START_SESSION = "icons/media-record-5.png";
	public static final String STOP_SESSION = "icons/media-playback-pause.png";
	public static final String EDIT_SCREENSHOT = "icons/format-stroke-color.png";
	public static final String DELETE_SCREENSHOT = "icons/window-close-2.png";
	public static final String NEXT_SCREENSHOT = "icons/gnome_go_next.png";
	public static final String PREV_SCREENSHOT = "icons/gnome_go_previous.png";
	public static final String LAST_SCREENSHOT = "icons/gnome_go_last.png";
	public static final String FIRST_SCREENSHOT = "icons/gnome_go_first.png";
	public static final String DESCRIPTION_SCREENSHOT = "icons/desc_edit.png";
	public static final String SEARCH = "icons/search.png";
	public static final String MOUSE_MARK_RED = "icons/mouse_red.png";
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
