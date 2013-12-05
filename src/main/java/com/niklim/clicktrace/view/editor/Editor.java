package com.niklim.clicktrace.view.editor;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.controller.KeyboardController;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.view.editor.control.ControlView;
import com.niklim.clicktrace.view.editor.control.Toolbar;
import com.niklim.clicktrace.view.editor.control.menu.Menu;
import com.niklim.clicktrace.view.editor.session.ScreenShotView;

@Singleton
public class Editor {

	private static final String APP_NAME = "Clicktrace";
	private JFrame frame;
	private JScrollPane scrollPane;

	@Inject
	private ScreenShotView screenShotView;

	@Inject
	private ControlView controlView;

	@Inject
	private Menu menu;

	@Inject
	private Toolbar toolbar;

	@Inject
	private KeyboardController keyboardController;

	public Editor() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);
	}

	@Inject
	public void init() {
		frame = new JFrame(APP_NAME);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(100, 100, (int) (dim.getWidth() * 0.7), (int) (dim.getHeight() * 0.7));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setEnabled(false);
		splitPane.setTopComponent(controlView.getComponent());
		splitPane.setBottomComponent(screenShotView.getPanel());

		scrollPane = new JScrollPane(splitPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		frame.add(scrollPane);
		frame.setJMenuBar(menu.getMenuBar());

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				open(null);
			}
		});

		keyboardController.registerKeyboardHooks(frame);
	}

	public void open(Session session) {
		frame.setVisible(true);
		if (session != null) {
			showSession(session);
		}
	}

	public void showSession(Session session) {
		showWaitingCursor();
		frame.setTitle(APP_NAME + " - " + session.getName());

		resetControl(session);
		if (session.getShots().size() > 0) {
			ScreenShot shot = session.getShots().get(0);
			screenShotView.show(shot);
			controlView.setActiveScreenShot(shot);
		} else {
			screenShotView.clear();
		}

		refresh();

		hideWaitingCursor();
	}

	private void hideWaitingCursor() {
		frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	private void showWaitingCursor() {
		frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	public void refresh() {
		frame.getContentPane().revalidate();
		frame.getContentPane().repaint();
	}

	public void showScreenShot(ScreenShot screenShot, boolean selected) {
		if (screenShot != null) {
			showWaitingCursor();
			screenShotView.show(screenShot);
			controlView.setActiveScreenShotSelected(selected);
			controlView.setActiveScreenShot(screenShot);
			hideWaitingCursor();
		} else {
			screenShotView.clear();
		}
	}

	public Dimension getEditorDimension() {
		return frame.getSize();
	}

	public void hideSession() {
		controlView.hide();
		screenShotView.clear();
		frame.setTitle(APP_NAME);
		refresh();
	}

	public void setSelectedActiveScreenShot(boolean selected) {
		controlView.setActiveScreenShotSelected(selected);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void sessionStateChanged() {
		menu.sessionStateChanged();
		toolbar.sessionStateChanged();
	}

	public void resetControl(Session session) {
		controlView.showImagesCombobox(session);
		if (session.getShots().isEmpty()) {
			controlView.hide();
		}
		refresh();
	}

	public void hide() {
		frame.setState(JFrame.ICONIFIED);
		frame.toFront();
	}

	public void scrollUp() {
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMinimum());
	}

	public void scrollDown() {
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

}
