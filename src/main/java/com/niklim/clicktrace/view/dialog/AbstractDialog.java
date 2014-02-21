package com.niklim.clicktrace.view.dialog;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.view.MainFrameHolder;

public abstract class AbstractDialog {
	protected JDialog dialog;
	protected JButton okButton;
	protected JButton cancelButton;

	public AbstractDialog() {
		// TODO fixme: MainFrameHolder.get() == null
		dialog = new JDialog(MainFrameHolder.get(), true);

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void postInit() {
		pack();
	}

	protected void initTextWrapping(JTextArea textarea) {
		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);
	}

	public void close() {
		dialog.setVisible(false);
	}

	private void pack() {
		dialog.pack();
	}

	protected void center() {
		Rectangle mainFrameRect = MainFrameHolder.get().getBounds();
		int x = mainFrameRect.x + (int) (mainFrameRect.getWidth() - dialog.getWidth()) / 2;
		int y = mainFrameRect.y + (int) (mainFrameRect.getHeight() - dialog.getHeight()) / 2;
		dialog.setBounds(x, y, dialog.getWidth(), dialog.getHeight());
	}

	public JPanel createControlPanel(String okText, JComponent... components) {
		okButton = new JButton(okText);
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("[Esc]");

		JPanel buttonPanel = new JPanel(new MigLayout("align r, insets 10 0 0 0"));
		buttonPanel.add(cancelButton, "tag cancel");
		buttonPanel.add(okButton, "tag ok");

		JPanel controlPanel = null;
		if (components.length == 0) {
			controlPanel = buttonPanel;
		} else {
			controlPanel = embedExtraComponents(buttonPanel, components);
		}

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});

		return controlPanel;
	}

	private JPanel embedExtraComponents(JPanel buttonPanel, JComponent... components) {
		JPanel controlPanel = new JPanel(new MigLayout("insets 0", "[]push[]"));

		JPanel extraPanel = new JPanel();
		for (JComponent c : components) {
			extraPanel.add(c);
		}

		controlPanel.add(extraPanel);
		controlPanel.add(buttonPanel);

		return controlPanel;
	}

	protected abstract void okAction();

	protected void cancelAction() {
		close();
	}

	protected void showWaitingCursor() {
		dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	protected void hideWaitingCursor() {
		dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
