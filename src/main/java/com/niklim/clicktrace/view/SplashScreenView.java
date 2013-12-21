package com.niklim.clicktrace.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.Icons;
import com.niklim.clicktrace.controller.operation.session.NewSessionOperation;
import com.niklim.clicktrace.controller.operation.session.OpenSessionOperation;
import com.niklim.clicktrace.controller.operation.session.StartRecordingOperation;

@Singleton
public class SplashScreenView {
	@Inject
	private StartRecordingOperation startRecordingOperation;

	@Inject
	private NewSessionOperation newSessionOperation;

	@Inject
	private OpenSessionOperation openSessionOperation;

	private JPanel panel;

	private JButton newSessionButton;
	private JButton openSessionButton;
	private JButton recordSessionButton;

	public SplashScreenView() {
		panel = new JPanel(new MigLayout("align center", ""));

		newSessionButton = Buttons.create("New session", "", Icons.SESSION_NEW, OperationsShortcutEnum.SESSION_NEW);
		openSessionButton = Buttons.create("Open session", "", Icons.SESSION_OPEN, OperationsShortcutEnum.SESSION_OPEN);
		recordSessionButton = Buttons.create("Record session", "", Icons.START_RECORDING,
				OperationsShortcutEnum.START_RECORDING);

		JPanel buttonsPanel = new JPanel(new MigLayout("align center", ""));
		buttonsPanel.add(newSessionButton);
		buttonsPanel.add(openSessionButton);
		buttonsPanel.add(recordSessionButton);

		panel.add(buttonsPanel, "grow, wrap");

		BufferedImage img;
		try {
			img = ImageIO.read(Thread.currentThread().getContextClassLoader().getResourceAsStream("splash-screen.jpg"));
			panel.add(new SplashPanel(img));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Inject
	public void init() {
		newSessionButton.addMouseListener(newSessionOperation.mouse());
		openSessionButton.addMouseListener(openSessionOperation.mouse());
		recordSessionButton.addMouseListener(startRecordingOperation.mouse());
	}

	public JPanel getPanel() {
		return panel;
	}

	private class SplashPanel extends JPanel {
		BufferedImage image;

		SplashPanel(BufferedImage image) {
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			// setBorder(BorderFactory.createLineBorder(new Color(95, 158, 160),
			// 2));
			this.image = image;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);
		}
	}
}
