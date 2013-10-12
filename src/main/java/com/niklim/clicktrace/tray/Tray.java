package com.niklim.clicktrace.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.inject.Inject;

public class Tray {
	@Inject
	private TrayController controller;

	public Tray() {
		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// adding TrayIcon.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		// Check the SystemTray support
		if (!SystemTray.isSupported()) {
			System.out.println("SystemTray is not supported");
			return;
		}
		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("icon.png", "tray icon"));
		final SystemTray tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem nameItem = new MenuItem("Set session name");
		MenuItem startItem = new MenuItem("Start session");
		MenuItem stopItem = new MenuItem("Stop session");
		MenuItem editorItem = new MenuItem("Open editor");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to popup menu
		popup.add(nameItem);
		popup.add(startItem);
		popup.add(stopItem);
		popup.addSeparator();
		popup.add(editorItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.out.println("TrayIcon could not be added.");
			return;
		}

		nameItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = JOptionPane.showInputDialog("Set session name", controller.getSessionName());
				if (name != null) {
					controller.setSessionName(name);
				}
			}
		});

		startItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.startSession();
			}
		});

		stopItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.stopSession();
			}
		});

		editorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.openEditor();
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});
	}

	// Obtain the image URL
	protected Image createImage(String path, String description) {
		URL imageURL = Thread.currentThread().getContextClassLoader().getResource(path);

		if (imageURL == null) {
			System.err.println("Resource not found: " + path);
			return null;
		} else {
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
}
