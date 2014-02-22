package com.niklim.clicktrace.dialog;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.niklim.clicktrace.view.MainFrameHolder;

public class AbstractDialogView {
	protected JDialog dialog;
	protected JButton okButton;
	protected JButton cancelButton;

	public AbstractDialogView() {
		// TODO fixme: MainFrameHolder.get() == null
		dialog = new JDialog(MainFrameHolder.get(), true);
	}

	public JDialog dialog() {
		return dialog;
	}

	public JButton okButton() {
		return okButton;
	}

	public JButton cancelButton() {
		return cancelButton;
	}

	protected void center() {
		Rectangle mainFrameRect = MainFrameHolder.get().getBounds();
		int x = mainFrameRect.x + (int) (mainFrameRect.getWidth() - dialog.getWidth()) / 2;
		int y = mainFrameRect.y + (int) (mainFrameRect.getHeight() - dialog.getHeight()) / 2;
		dialog.setBounds(x, y, dialog.getWidth(), dialog.getHeight());
	}

	protected JPanel createControlPanel(String okText, JComponent... components) {
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
}
