package com.niklim.clicktrace.editor;

import java.io.File;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.niklim.clicktrace.capture.ImgManager;
import com.niklim.clicktrace.editor.Editor.TrashFilter;

public class ImageTree {
	public void buildImgTree(JTree tree) {
		DefaultMutableTreeNode top = (DefaultMutableTreeNode) tree.getModel().getRoot();
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

		DefaultTreeModel defaultTreeModel = (DefaultTreeModel) tree.getModel();
		defaultTreeModel.reload();
	}
}
