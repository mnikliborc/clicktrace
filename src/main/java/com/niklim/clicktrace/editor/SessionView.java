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

import net.miginfocom.swing.MigLayout;

import org.imgscalr.Scalr;

import com.google.inject.Inject;
import com.niklim.clicktrace.SessionsManager;

public class SessionView {

	@Inject
	private Editor editor;

	private double widthHeightRatio;
	private int rightPanelWidth;

	public void showSession(String sessionName, JPanel rightPanel) {
		widthHeightRatio = (double) rightPanel.getHeight() / rightPanel.getWidth();
		rightPanelWidth = (int) rightPanel.getWidth();

		for (String imgName : SessionsManager.loadSession(sessionName)) {
			try {
				BufferedImage image = ImageIO
						.read(new File(SessionsManager.SESSIONS_DIR + sessionName + "/" + imgName));
				rightPanel.add(new ThumbPanel(Scalr.resize(image, rightPanelWidth - 30), rightPanel, sessionName,
						imgName), "wrap");
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

		private JPanel thumb;

		private class InnerThumbPanel extends JPanel {
			InnerThumbPanel(int width) {
				setPreferredSize(new Dimension(width, (int) (width * widthHeightRatio)));
				setBackground(new Color(50, 100, 200));
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		public ThumbPanel(BufferedImage image, final JPanel rightPanel, final String sessionName, final String imageName) {
			super(new MigLayout());

			thumb = new InnerThumbPanel((int) rightPanel.getSize().getWidth());
			this.image = image;
			nameLabel = new JLabel(imageName);

			editLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			editLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					editor.edit(imageName);
				}
			});

			deleteLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
			deleteLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					editor.delete(sessionName, imageName);

					rightPanel.remove(ThumbPanel.this);
				}
			});

			layComopnents();
		}

		private void layComopnents() {
			JPanel control = new JPanel(new MigLayout());
			control.setBackground(new Color(200, 100, 0));

			control.setBorder(new BevelBorder(BevelBorder.LOWERED));
			control.add(nameLabel);

			int gap = rightPanelWidth - 300;
			control.add(deleteLabel, "gapleft " + gap);
			control.add(editLabel);
			control.add(checkbox, "wrap");

			control.setPreferredSize(new Dimension(rightPanelWidth, 50));

			int height = (int) (rightPanelWidth * widthHeightRatio) + 50;
			setPreferredSize(new Dimension(rightPanelWidth, height));

			add(control, "wrap");
			add(thumb, BorderLayout.CENTER);
		}
	}
}
