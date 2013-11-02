package com.niklim.clicktrace;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class Icons {
	public static final String NEW_SESSION = "icons/1383438310_folder_add.png";
	public static final String OPEN_SESSION = "icons/1383438320_folder.png";
	public static final String DELETE_SESSION = "icons/1383438389_folder_delete.png";
	public static final String REFRESH = "icons/view-refresh-5.png";

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
