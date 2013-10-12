package com.niklim.clicktrace.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.imgscalr.Scalr;

import com.google.inject.Inject;
import com.niklim.clicktrace.ImgManager;

public class SessionView {
	private static final int THUMB_SIZE = 300;

	@Inject
	private Editor editor;

	public void showSession(String sessionName, JPanel rightPanel) {
		File sessionDir = new File(ImgManager.SESSIONS_DIR + sessionName);
		File[] imgs = sessionDir.listFiles(new Editor.TrashFilter());
		for (File file : imgs) {
			try {
				BufferedImage image = ImageIO.read(file);
				rightPanel
						.add(new ThumbPanel(Scalr.resize(image, THUMB_SIZE), rightPanel, sessionName, file.getName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private class ThumbPanel extends JPanel {
		private JCheckBox checkbox = new JCheckBox();
		private JLabel nameLabel;
		private JLabel deleteLabel = new JLabel("delete");
		private JLabel editLabel = new JLabel("edit");

		private BufferedImage image;

		private JPanel thumb = new InnerThumbPanel();

		private class InnerThumbPanel extends JPanel {
			InnerThumbPanel() {
				setPreferredSize(new Dimension(THUMB_SIZE, THUMB_SIZE));
				setBackground(new Color(50, 100, 200));
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		public ThumbPanel(BufferedImage image, final JPanel rightPanel, final String sessionName, final String imageName) {
			this.image = image;
			nameLabel = new JLabel(imageName);

			editLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			editLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					editor.edit(imageName);
				}
			});

			final ThumbPanel panel = this;
			deleteLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			deleteLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					editor.delete(sessionName, imageName);

					rightPanel.remove(panel);
				}
			});

			layComopnents();
		}

		public void layComopnents() {
			JPanel control = new JPanel();
			control.setBackground(new Color(200, 100, 0));

			control.setBorder(new BevelBorder(BevelBorder.LOWERED));
			control.add(nameLabel);

			JPanel gap = new JPanel();
			gap.setPreferredSize(new Dimension(120, 20));
			control.add(gap);

			control.add(deleteLabel);
			control.add(editLabel);
			control.add(checkbox);

			setPreferredSize(new Dimension(THUMB_SIZE, THUMB_SIZE));

			add(control, BorderLayout.NORTH);
			add(thumb, BorderLayout.CENTER);
		}
	}
}
