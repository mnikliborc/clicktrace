package com.niklim.clicktrace.view.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import com.niklim.clicktrace.view.MainFrameHolder;

public class AbstractDialog {
	protected JDialog dialog;

	public AbstractDialog() {
		dialog = new JDialog(MainFrameHolder.get(), true);

		dialog.getRootPane().registerKeyboardAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	public void close() {
		dialog.setVisible(false);
	}
}
