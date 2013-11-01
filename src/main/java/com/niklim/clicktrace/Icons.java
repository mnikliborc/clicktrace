package com.niklim.clicktrace;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class Icons {
	public static final String NEW_SESSION = "icons/folder-new-7.png";

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
