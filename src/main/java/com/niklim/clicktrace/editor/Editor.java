package com.niklim.clicktrace.editor;

import java.awt.GridLayout;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.niklim.clicktrace.capture.ImgManager;

public class Editor {
	private final class TrashFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return !".".equals(name) && !"..".equals(name);
		}
	}

	private JFrame frame;
	private JScrollPane treeView;
	private JTree tree;
	private JPanel mainPanel;
	private JPanel rightPanel;

	public Editor() {
		frame = new JFrame("Frame");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		mainPanel = new JPanel(new GridLayout(0, 2));
		rightPanel = new JPanel();

		tree = new JTree(new DefaultMutableTreeNode("sessions"));
		treeView = new JScrollPane(tree);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setTopComponent(treeView);
		splitPane.setBottomComponent(rightPanel);

		splitPane.setDividerLocation(100);
		mainPanel.add(splitPane);
		frame.add(mainPanel);
	}

	private void buildImgTree() {
		DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
		top.removeAllChildren();

		File rootDir = new File(ImgManager.SESSIONS_DIR);

		File[] sessionDirs = rootDir.listFiles(new TrashFilter());

		for (File sessionDir : sessionDirs) {
			DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(sessionDir.getName());
			File[] imgs = sessionDir.listFiles(new TrashFilter());
			for (File img : imgs) {
				dirNode.add(new DefaultMutableTreeNode(img.getName()));
			}
			top.add(dirNode);
		}

		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree.getModel();
		defaultTreeModel.reload();
	}

	public void open(String sessionName) {
		frame.setVisible(true);
		buildImgTree();
	}
}
