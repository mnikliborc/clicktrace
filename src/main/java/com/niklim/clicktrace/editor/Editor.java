package com.niklim.clicktrace.editor;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.google.inject.Inject;

public class Editor implements TreeExpansionListener {
	public static class TrashFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return !".".equals(name) && !"..".equals(name);
		}
	}

	private JFrame frame;
	private JTree tree;
	private JPanel rightPanel;

	@Inject
	private SessionView sessionView;

	@Inject
	private ImageTree imageTree;

	public Editor() {
		frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		rightPanel = new JPanel(new GridLayout(0, 3));
		rightPanel.setBackground(new Color(10));

		tree = new JTree(new DefaultMutableTreeNode("sessions"));

		tree.addTreeExpansionListener(this);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setTopComponent(new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		splitPane.setBottomComponent(new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

		splitPane.setDividerLocation(200);
		splitPane.setBackground(new Color(200));

		frame.add(splitPane);
		open(null);
	}

	public void open(String sessionName) {
		frame.setVisible(true);
		imageTree.buildImgTree(tree);
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
}
