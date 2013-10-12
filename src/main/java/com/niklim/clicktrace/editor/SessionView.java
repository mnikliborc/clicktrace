package com.niklim.clicktrace.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.imgscalr.Scalr;

import com.niklim.clicktrace.capture.ImgManager;

public class SessionView {
	private static final int THUMB_SIZE = 300;

	public void showSession(String filename, JPanel rightPanel) {
		File sessionDir = new File(ImgManager.SESSIONS_DIR + filename);
		File[] imgs = sessionDir.listFiles(new Editor.TrashFilter());
		for (File file : imgs) {
			try {
				BufferedImage image = ImageIO.read(file);
				rightPanel.add(new ThumbPanel(Scalr.resize(image, THUMB_SIZE), file.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static class ThumbPanel extends JPanel {
		private JCheckBox checkbox = new JCheckBox();
		private JLabel nameLabel;
		private JLabel deleteLabel = new JLabel("delete");
		private JLabel editLabel = new JLabel("edit");

		private BufferedImage image;

		private JPanel thumb = new InnerThumbPanel();

		private class InnerThumbPanel extends JPanel {
			InnerThumbPanel() {
				setPreferredSize(new Dimension(THUMB_SIZE, THUMB_SIZE));
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		public ThumbPanel(BufferedImage image, String name) {
			super(new GridLayout(0, 1));

			nameLabel = new JLabel(name);

			JPanel control = new JPanel();
			control.setBackground(new Color(200, 100, 0));
			control.setMaximumSize(new Dimension(THUMB_SIZE, 30));
			control.setBorder(new BevelBorder(BevelBorder.LOWERED));
			control.add(checkbox);
			control.add(deleteLabel);
			control.add(editLabel);
			control.add(nameLabel);
			add(control);
			add(thumb);

			this.image = image;
		}
	}
}
