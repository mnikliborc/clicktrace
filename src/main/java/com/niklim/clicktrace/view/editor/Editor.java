package com.niklim.clicktrace.view.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.ActiveSession;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.view.editor.control.ControlView;
import com.niklim.clicktrace.view.editor.menu.Menu;
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
	private ControlView controlView;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private Menu editorMenu;

	@Inject
	private ActiveSession activeSession;

	@Inject
	public void init() {
		frame = new JFrame("Clicktrace");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(controlView.getComponent());
		splitPane.setBottomComponent(sessionView.getComponent());

		frame.add(splitPane);
		frame.setJMenuBar(editorMenu.getMenu());

		open(null);
	}

	public void open(Session session) {
		frame.setVisible(true);
		if (session != null) {
			showSession(session);
		}
	}

	public void showSession(Session session) {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		sessionView.showSession(session);
		controlView.showImagesCombobox(session);

		refresh();

		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void refresh() {
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public void showScreenShot(int i) {
		sessionView.showScreenShot(i);
	}

	public void edit(ScreenShot shot) {
		try {
			ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\mspaint.exe", "sessions\\"
					+ shot.getSession().getName() + "\\" + shot.getName());
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
		sessionView.hideSession();
		refresh();
	}

	public void setSelectedAllScreenShots(boolean selected) {
		sessionView.setSelectedAllScreenShots(selected);
	}

	public void deleteSelectedScreenShots() {
		List<ScreenShot> shotsToRemove = sessionView.deleteSelectedScreenshots();
		Session session = activeSession.getSession();
		session.getShots().removeAll(shotsToRemove);
		controlView.showImagesCombobox(session);
		refresh();
	}

	public void deleteScreenShot(ScreenShot shot) {
		Session session = activeSession.getSession();
		session.getShots().remove(shot);
		controlView.showImagesCombobox(session);
		refresh();
	}

	public JFrame getFrame() {
		return frame;
	}

}
