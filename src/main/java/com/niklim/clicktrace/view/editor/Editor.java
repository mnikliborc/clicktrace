package com.niklim.clicktrace.view.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.model.session.SessionManager;
import com.niklim.clicktrace.view.editor.control.ControlView;
import com.niklim.clicktrace.view.editor.session.SessionView;

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
	public void init() {
		frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(controlView.getComponent());
		splitPane.setBottomComponent(sessionView.getComponent());

		frame.add(splitPane);

		open(null);
	}

	public void open(String sessionName) {
		frame.setVisible(true);
		controlView.setSessions(sessionManager.loadAll(), sessionName);
	}

	public void showSession(Session session) {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		sessionView.showSession(session);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();

		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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

}
