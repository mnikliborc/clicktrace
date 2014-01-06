package com.niklim.clicktrace.view.dialog.settings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.google.common.base.Optional;
import com.google.inject.Singleton;

@Singleton
public class CaptureRectangleFrame extends JFrame {

	public static interface CaptureRectangleCallback {
		public void done(Optional<Rectangle> r);
	}

	private class MouseDragListener implements MouseListener, MouseMotionListener {
		private Point start;
		private Point end;

		public void mouseReleased(MouseEvent arg0) {
			end = arg0.getPoint();
			paintSelection();
		}

		public void mousePressed(MouseEvent arg0) {
			start = arg0.getPoint();
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseClicked(MouseEvent arg0) {
		}

		public void mouseDragged(MouseEvent e) {
			end = e.getPoint();
			paintSelection();
		}

		public void mouseMoved(MouseEvent e) {
		}

	}

	private void paintSelection() {
		Graphics g = CaptureRectangleFrame.this.getBufferStrategy().getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g2.clearRect(0, 0, getWidth(), getHeight());
		if (mouseDragListener.start != null && mouseDragListener.end != null) {
			g2.setColor(new Color(0.01f, 0.01f, 0.01f, 0.9f));
			g2.fill(getSelectionRect());
		}
		getBufferStrategy().show();
	}

	private Rectangle getSelectionRect() {
		int leftTopX = Math.min(mouseDragListener.start.x, mouseDragListener.end.x);
		int leftTopY = Math.min(mouseDragListener.start.y, mouseDragListener.end.y);
		int width = Math.abs(mouseDragListener.start.x - mouseDragListener.end.x);
		int height = Math.abs(mouseDragListener.start.y - mouseDragListener.end.y);

		return new Rectangle(leftTopX, leftTopY, width, height);
	}

	@Override
	public void paint(Graphics g) {
		paintSelection();
	}

	private GraphicsDevice defaultScreen;
	private MouseDragListener mouseDragListener;
	private CaptureRectangleCallback callback;

	public CaptureRectangleFrame() {
		doMagic();
		createListeners();

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		defaultScreen = ge.getDefaultScreenDevice();
		defaultScreen.setFullScreenWindow(null);
	}

	// it should be done here, not sure what, not sure why:)
	private void doMagic() {
		dispose();
		setUndecorated(true);
		setBackground(new Color(0.9f, 0.9f, 0.9f, 0.9f));
		pack();
		createBufferStrategy(2);
	}

	private void createListeners() {
		mouseDragListener = new MouseDragListener();
		getRootPane().addMouseListener(mouseDragListener);
		getRootPane().addMouseMotionListener(mouseDragListener);

		ActionListener cancel = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				defaultScreen.setFullScreenWindow(null);
				setVisible(false);

				callback.done(Optional.<Rectangle> absent());
			}
		};
		ActionListener save = new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				defaultScreen.setFullScreenWindow(null);
				setVisible(false);

				callback.done(Optional.of(getSelectionRect()));
			}
		};
		getRootPane().registerKeyboardAction(cancel, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		getRootPane().registerKeyboardAction(save, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void open(Rectangle captureRect, CaptureRectangleCallback callback) {
		this.callback = callback;
		setVisible(true);

		if (captureRect != null) {
			mouseDragListener.start = new Point(captureRect.x, captureRect.y);
			mouseDragListener.end = new Point(captureRect.x + captureRect.width, captureRect.y
					+ captureRect.height);
		}

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		defaultScreen = ge.getDefaultScreenDevice();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		defaultScreen.setFullScreenWindow(this);
	}
}
