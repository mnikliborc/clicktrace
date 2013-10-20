package com.niklim.clicktrace.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import net.miginfocom.swing.MigLayout;

import org.imgscalr.Scalr;

import com.google.inject.Inject;
import com.niklim.clicktrace.SessionsManager;

public class SessionView {

	@Inject
	private Editor editor;

	private double widthHeightRatio;
	private int sessionPanelWidth;

	private JScrollPane sessionScrollPanel;

	private JPanel sessionPanel;
	private List<ThumbPanel> thumbs = new ArrayList<ThumbPanel>();

	public SessionView() {
		sessionPanel = new JPanel(new MigLayout());
		sessionScrollPanel = new JScrollPane(sessionPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public void showSession(String sessionName) {
		widthHeightRatio = (double) sessionPanel.getHeight() / sessionPanel.getWidth();
		sessionPanelWidth = (int) sessionPanel.getWidth();

		sessionPanel.removeAll();
		thumbs.clear();
		for (String imgName : SessionsManager.loadSession(sessionName)) {
			try {
				BufferedImage scaledImage = loadScaledImage(sessionName, imgName);
				ThumbPanel thumb = new ThumbPanel(scaledImage, sessionPanel, sessionName, imgName);
				thumbs.add(thumb);
				sessionPanel.add(thumb, "wrap");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public BufferedImage loadScaledImage(String sessionName, String imgName) throws IOException {
		BufferedImage image = ImageIO.read(new File(SessionsManager.SESSIONS_DIR + sessionName + "/" + imgName));
		BufferedImage scaledImage = Scalr.resize(image, sessionPanelWidth - 40);
		return scaledImage;
	}

	private class ThumbPanel extends JPanel {
		private JCheckBox checkbox = new JCheckBox();
		private JLabel nameLabel;
		private JButton deleteButton = new JButton("delete");
		private JButton editButton = new JButton("edit");
		private JButton refreshButton = new JButton("refresh");

		private BufferedImage image;

		private JPanel thumb;

		private class InnerThumbPanel extends JPanel {
			InnerThumbPanel(int width) {
				setPreferredSize(new Dimension(width, (int) (width * widthHeightRatio)));
				setBorder(BorderFactory.createLineBorder(Color.black, 2));
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		public ThumbPanel(BufferedImage image, final JPanel sessionPanel, final String sessionName,
				final String imageName) {
			super(new MigLayout());

			thumb = new InnerThumbPanel((int) sessionPanel.getSize().getWidth());
			this.image = image;
			nameLabel = new JLabel(imageName);

			refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			refreshButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					try {
						ThumbPanel.this.image = loadScaledImage(sessionName, imageName);
						revalidate();
						repaint();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			editButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					editor.edit(sessionName, imageName);
				}
			});

			deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			deleteButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					SessionsManager.deleteImage(sessionName, imageName);

					sessionPanel.remove(ThumbPanel.this);
					sessionPanel.revalidate();
					sessionPanel.repaint();
				}
			});

			layComopnents();
		}

		private void layComopnents() {
			setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			JPanel control = new JPanel(new MigLayout());

			control.add(nameLabel);

			int gap = sessionPanelWidth - 400;
			control.add(refreshButton, "gapleft " + gap);
			control.add(editButton);
			control.add(deleteButton);
			control.add(checkbox, "wrap");

			control.setPreferredSize(new Dimension(sessionPanelWidth - 40, 50));

			int height = (int) (sessionPanelWidth * widthHeightRatio) + 50;
			setPreferredSize(new Dimension(sessionPanelWidth - 20, height));

			add(control, "wrap");
			add(thumb);
		}
	}

	public Component getComponent() {
		return sessionScrollPanel;
	}

	public void showImage(int i) {
		JScrollBar scroll = sessionScrollPanel.getVerticalScrollBar();
		scroll.setValue(scroll.getMinimum() + thumbs.get(i).getHeight() * (i - 1));
	}
}
