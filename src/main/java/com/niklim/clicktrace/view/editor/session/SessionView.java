package com.niklim.clicktrace.view.editor.session;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.imgscalr.Scalr;

import com.google.inject.Inject;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.Editor;

public class SessionView {
	// TODO refactor me!
	@Inject
	private Editor editor;

	private double heightWidthRatio;
	private int sessionPanelWidth;

	private JScrollPane sessionScrollPanel;

	private JPanel sessionPanel;
	private List<ThumbPanel> thumbs = new ArrayList<ThumbPanel>();

	public SessionView() {
		sessionPanel = new JPanel(new MigLayout());
		sessionPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		sessionScrollPanel = new JScrollPane(sessionPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}

	public void showSession(Session session) {
		heightWidthRatio = (double) sessionPanel.getHeight() / sessionPanel.getWidth();
		sessionPanelWidth = (int) sessionPanel.getWidth();

		sessionPanel.removeAll();
		thumbs.clear();
		for (ScreenShot shot : session.getShots()) {
			try {
				ThumbPanel thumb = new ThumbPanel(sessionPanel, shot);
				thumbs.add(thumb);
				sessionPanel.add(thumb, "wrap");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public BufferedImage scaleImage(BufferedImage image, int thumbWidth) throws IOException {
		BufferedImage scaledImage = Scalr.resize(image, thumbWidth);
		return scaledImage;
	}

	private class ThumbPanel extends JPanel {
		private JCheckBox checkbox = new JCheckBox();
		private JLabel nameLabel;
		private JButton deleteButton = new JButton("delete", new ImageIcon(Icons.createIconImage(Icons.DELETE_SCREENSHOT, "delete")));
		private JButton editButton = new JButton("edit", new ImageIcon(Icons.createIconImage(Icons.EDIT_SCREENSHOT, "edit")));
		private JButton refreshButton = new JButton("refresh", new ImageIcon(Icons.createIconImage(Icons.REFRESH_SCREENSHOT, "refresh")));

		private BufferedImage image;

		private JPanel thumb;
		private ScreenShot shot;

		private int thumbWidth;

		private class InnerThumbPanel extends JPanel {
			InnerThumbPanel(int width) {
				setPreferredSize(new Dimension(width, (int) (width * heightWidthRatio)));
				setBorder(BorderFactory.createLineBorder(new Color(95, 158, 160), 2));
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}

			public int getWidth() {
				return (int) getSize().getWidth();
			}
		};

		public ThumbPanel(final JPanel sessionPanel, final ScreenShot shot) throws IOException {
			super(new MigLayout());

			this.shot = shot;

			thumbWidth = (int) sessionPanel.getSize().getWidth() - 20;
			image = scaleImage(shot.getImage(), thumbWidth);
			thumb = new InnerThumbPanel(thumbWidth);
			nameLabel = new JLabel(shot.getFilename());

			refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			refreshButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					try {
						ThumbPanel.this.image = scaleImage(shot.loadImage(), thumb.getWidth());
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
					editor.edit(shot);
				}
			});

			deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			deleteButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
				}
			});

			layComopnents();
		}

		private void layComopnents() {
			JPanel control = new JPanel(new MigLayout());
			control.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

			control.add(nameLabel);

			int gap = sessionPanelWidth - 580;
			control.add(refreshButton, "gapleft " + gap);
			control.add(editButton);
			control.add(deleteButton);
			control.add(checkbox, "wrap");

			control.setPreferredSize(new Dimension(sessionPanelWidth - 40, 50));

			int height = (int) (thumbWidth * heightWidthRatio) + 50;
			setPreferredSize(new Dimension((int) thumb.getWidth(), height));

			add(control, "wrap");
			add(thumb);
		}
	}

	public Component getComponent() {
		return sessionScrollPanel;
	}

	public void showScreenShot(int i) {
		JScrollBar scroll = sessionScrollPanel.getVerticalScrollBar();
		scroll.setValue((int) (thumbs.get(i).getBounds().getY()));
	}

	public void hideSession() {
		sessionPanel.removeAll();
	}

	public void setSelectedAllScreenShots(boolean selected) {
		for (ThumbPanel thumb : thumbs) {
			thumb.checkbox.setSelected(selected);
		}
	}

}
