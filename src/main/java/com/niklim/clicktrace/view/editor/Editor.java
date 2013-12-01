package com.niklim.clicktrace.view.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.control.ControlView;
import com.niklim.clicktrace.view.editor.control.Toolbar;
import com.niklim.clicktrace.view.editor.control.menu.Menu;
import com.niklim.clicktrace.view.editor.session.ScreenShotView;

@Singleton
public class Editor {

	private JFrame frame;

	@Inject
	private ScreenShotView screenShotView;

	@Inject
	private ControlView controlView;

	@Inject
	private Menu menu;

	@Inject
	private Toolbar toolbar;

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

		frame.add(new JScrollPane(splitPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		frame.setJMenuBar(menu.getMenuBar());

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
		showWaitingCursor();

		resetControl(session);
		if (session.getShots().size() > 0) {
			ScreenShot shot = session.getShots().get(0);
			screenShotView.show(shot);
			controlView.setActiveScreenShot(shot);
		} else {
			screenShotView.clear();
		}

		refresh();

		hideWaitingCursor();
	}

	private void hideWaitingCursor() {
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	private void showWaitingCursor() {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	public void refresh() {
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public void showScreenShot(ScreenShot screenShot, boolean selected) {
		if (screenShot != null) {
			showWaitingCursor();
			screenShotView.show(screenShot);
			controlView.setActiveScreenShotSelected(selected);
			controlView.setActiveScreenShot(screenShot);
			hideWaitingCursor();
		} else {
			screenShotView.clear();
		}
	}

	public void edit(ScreenShot shot) {
		try {
			ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\mspaint.exe", "sessions\\"
					+ shot.getSession().getName() + "\\" + shot.getFilename());
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Dimension getEditorDimension() {
		return frame.getSize();
	}

	public void hideSession() {
		controlView.hide();
		screenShotView.clear();
		refresh();
	}

	public void setSelectedActiveScreenShot(boolean selected) {
		controlView.setActiveScreenShotSelected(selected);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void sessionStateChanged() {
		menu.sessionStateChanged();
		toolbar.sessionStateChanged();
	}

	public void resetControl(Session session) {
		controlView.showImagesCombobox(session);
		if (session.getShots().isEmpty()) {
			controlView.hide();
		}
		refresh();
	}

	public void hide() {
		frame.setState(JFrame.ICONIFIED);
	}

}
