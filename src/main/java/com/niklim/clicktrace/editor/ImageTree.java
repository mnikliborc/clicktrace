package com.niklim.clicktrace.editor;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.niklim.clicktrace.ImgManager;
import com.niklim.clicktrace.editor.Editor.TrashFilter;

public class ImageTree {
	private JTree tree = new JTree(new DefaultMutableTreeNode("sessions"));;

	public void buildImgTree() {
		DefaultMutableTreeNode top = (DefaultMutableTreeNode) getTree().getModel().getRoot();
		top.removeAllChildren();

		File rootDir = new File(ImgManager.SESSIONS_DIR);

		File[] sessionDirs = rootDir.listFiles(new TrashFilter());

		for (File sessionDir : sessionDirs) {
			DefaultMutableTreeNode dirNode = new DefaultMutableTreeNode(sessionDir.getName());
			File[] imgs = sessionDir.listFiles(new TrashFilter());
			for (File img : imgs) {
				DefaultMutableTreeNode imgNode = new DefaultMutableTreeNode(img.getName());
				dirNode.add(imgNode);
			}
			top.add(dirNode);
		}

		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) getTree().getModel();
		defaultTreeModel.reload();
	}

	public void deleteImage(String sessionName, String imageName) {
		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) getTree().getModel();

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) defaultTreeModel.getRoot();
		DefaultMutableTreeNode sessionNode = findNode(root, sessionName);
		DefaultMutableTreeNode imageNode = findNode(sessionNode, imageName);

		sessionNode.remove(imageNode);
		defaultTreeModel.reload();
	}

	private DefaultMutableTreeNode findNode(DefaultMutableTreeNode parent, String nodeName) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent.getChildAt(i);
			if (((String) node.getUserObject()).equals(nodeName)) {
				return node;
			}
		}
		return null;
	}

	public void openSession(String sessionName) {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getTree().getModel().getRoot();
		DefaultMutableTreeNode node = findNode(root, sessionName);
		tree.expandPath(new TreePath(new Object[] { root, node }));
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}
}
