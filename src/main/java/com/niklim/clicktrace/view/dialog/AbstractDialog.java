package com.niklim.clicktrace.view.dialog;

import java.awt.Rectangle;
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
		// TODO fixme: MainFrameHolder.get() == null
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

	protected void pack() {
		dialog.pack();
	}

	protected void center() {
		Rectangle mainFrameRect = MainFrameHolder.get().getBounds();
		int x = mainFrameRect.x + (int) (mainFrameRect.getWidth() - dialog.getWidth()) / 2;
		int y = mainFrameRect.y + (int) (mainFrameRect.getHeight() - dialog.getHeight()) / 2;
		dialog.setBounds(x, y, dialog.getWidth(), dialog.getHeight());
	}

	protected JPanel createControlPanel(String okText) {
		okButton = new JButton(okText);
		cancelButton = new JButton("Cancel");
		cancelButton.setToolTipText("[Esc]");

		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(cancelButton, "tag cancel");
		buttonPanel.add(okButton, "tag ok");

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

	protected abstract void okAction();

	protected void cancelAction() {
		close();
	}
}
