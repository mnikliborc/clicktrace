package com.niklim.clicktrace.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.google.inject.Inject;
import com.niklim.clicktrace.ImgManager;

public class Editor implements TreeExpansionListener {
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
	private ImageTree imageTree;

	@Inject
	public void init() {
		frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		rightPanel = new JPanel(new GridLayout(0, 3));
		rightPanel.setBackground(new Color(10));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(new JScrollPane(imageTree.getTree(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		splitPane.setRightComponent(new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		splitPane.setDividerLocation(200);
		splitPane.setBackground(new Color(200));

		frame.add(splitPane);

		imageTree.getTree().addTreeExpansionListener(this);

		open(null);
	}

	public void open(String sessionName) {
		imageTree.buildImgTree();
		frame.setVisible(true);
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();

		if (node == null)
			return;

		Object nodeInfo = node.getUserObject();
		String filename = (String) nodeInfo;
		if (node.isLeaf()) {
			// do nothing right now
		} else {
			showSession(filename);
		}
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
	}

	private void showSession(String filename) {
		rightPanel.removeAll();

		sessionView.showSession(filename, rightPanel);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public void edit(String imageName) {
	}

	public void delete(String sessionName, String imageName) {
		ImgManager.deleteImage(sessionName, imageName);
		imageTree.deleteImage(sessionName, imageName);

		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();

		imageTree.openSession(sessionName);
	}
}
