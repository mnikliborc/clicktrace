package com.niklim.clicktrace.dialog;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public abstract class AbstractDialog<V extends AbstractDialogView> {

	protected V view;

	public AbstractDialog() {
		view = createView();
		createControlListeners();
		view.dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	protected abstract V createView();

	public void postInit() {
		pack();
	}

	protected void initTextWrapping(JTextArea textarea) {
		textarea.setWrapStyleWord(true);
		textarea.setLineWrap(true);
	}

	public void close() {
		view.dialog.setVisible(false);
	}

	private void pack() {
		view.dialog.pack();
	}

	protected void center() {
		view.center();
	}

	protected void createControlListeners() {
		view.okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okAction();
			}
		});

		view.cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
	}

	protected abstract void okAction();

	protected void cancelAction() {
		close();
	}

	protected void showWaitingCursor() {
		view.dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	protected void hideWaitingCursor() {
		view.dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}
