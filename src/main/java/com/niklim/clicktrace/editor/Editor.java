package com.niklim.clicktrace.editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.miginfocom.swing.MigLayout;

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
	private JPanel rightPanel;

	@Inject
	private SessionView sessionView;

	@Inject
	private ControlPanel controlPanel;

	@Inject
	public void init() {
		frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		rightPanel = new JPanel(new MigLayout());
		rightPanel.setBackground(new Color(10));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(controlPanel.getPanel());
		splitPane.setBottomComponent(new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		splitPane.setBackground(new Color(200));

		frame.add(splitPane);

		open(null);
	}

	public void open(String sessionName) {
		frame.setVisible(true);
		controlPanel.setSessions(SessionsManager.loadSessions(), sessionName);
	}

	void showSession(String sessionName) {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));

		rightPanel.removeAll();

		sessionView.showSession(sessionName, rightPanel);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();

		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void edit(String imageName) {
	}

	public void delete(String sessionName, String imageName) {
		SessionsManager.deleteImage(sessionName, imageName);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public Dimension getEditorDimension() {
		return frame.getSize();
	}
}
