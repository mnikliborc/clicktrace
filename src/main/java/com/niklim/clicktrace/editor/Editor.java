package com.niklim.clicktrace.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import com.google.inject.Inject;
import com.niklim.clicktrace.SessionsManager;

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
		controlView.setSessions(SessionsManager.loadSessions(), sessionName);
	}

	void showSession(String sessionName) {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		sessionView.showSession(sessionName);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();

		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	void showImage(int i) {
		sessionView.showImage(i);
	}

	public void edit(String sessionName, String imageName) {
		try {
			ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\mspaint.exe", "sessions\\" + sessionName
					+ "\\" + imageName);
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Dimension getEditorDimension() {
		return frame.getSize();
	}
}
