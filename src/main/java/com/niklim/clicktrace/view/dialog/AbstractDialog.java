package com.niklim.clicktrace.view.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.view.MainFrameHolder;

public abstract class AbstractDialog {
	protected JDialog dialog;
	protected JButton okButton;
	protected JButton cancelButton;

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

	protected JPanel createControlPanel(String okText) {
		okButton = new JButton(okText);
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("[Esc]");

		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(cancelButton, "tag cancel");
		buttonPanel.add(okButton, "tag apply");

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

		return buttonPanel;
	}

	protected void okAction() {

	}

	protected void cancelAction() {
		close();
	}
}
