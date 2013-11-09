package com.niklim.clicktrace.view.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.view.editor.control.ControlView;
import com.niklim.clicktrace.view.editor.control.Menu;
import com.niklim.clicktrace.view.editor.control.Toolbar;
import com.niklim.clicktrace.view.editor.session.ScreenShotView;
import com.niklim.clicktrace.view.editor.session.SessionView;

@Singleton
public class Editor {
	public static class TrashFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return !".".equals(name) && !"..".equals(name);
		}
	}

	private JFrame frame;

	@Inject
	private SessionView sessionView;

	@Inject
	private ScreenShotView screenShotView;

	@Inject
	private ControlView controlView;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private Menu menu;

	@Inject
	private Toolbar toolbar;

	@Inject
	private ActiveSession activeSession;

	public Editor() {
		/* Use an appropriate Look and Feel */
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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

	}

	@Inject
	public void init() {
		frame = new JFrame("Clicktrace");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(controlView.getComponent());
		splitPane.setBottomComponent(screenShotView.getPanel());

		frame.add(splitPane);
		frame.setJMenuBar(menu.getMenu());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				open(null);
			}
		});

	}

	public void open(Session session) {
		frame.setVisible(true);
		if (session != null) {
			showSession(session);
		}
	}

	public void showSession(Session session) {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		if (session.getShots().size() > 0) {
			screenShotView.show(session.getShots().get(0));
		} else {
			screenShotView.clear();
		}
		controlView.showImagesCombobox(session);

		refresh();

		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void refresh() {
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public void showScreenShot(ScreenShot screenShot, boolean selected) {
		screenShotView.show(screenShot);
		controlView.setSelectedActiveScreenShot(selected);
	}

	public void edit(ScreenShot shot) {
		try {
			ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\mspaint.exe", "sessions\\" + shot.getSession().getName() + "\\"
					+ shot.getName());
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Dimension getEditorDimension() {
		return frame.getSize();
	}

	public void hideSession() {
		controlView.hideSession();
		screenShotView.clear();
		refresh();
	}

	public void setSelectedActiveScreenShot(boolean selected) {
		controlView.setSelectedActiveScreenShot(selected);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void sessionStateChanged() {
		menu.sessionStateChanged();
		toolbar.sessionStateChanged();
	}

	public void refreshCombobox(Session session) {
		controlView.showImagesCombobox(session);
		refresh();
	}

}
