package com.niklim.clicktrace.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.niklim.clicktrace.Icons;

public class Buttons {
	public static JButton create(String tooltip, String icon, OperationsShortcutEnum shortcut) {
		JButton button = new JButton(new ImageIcon(Icons.createIconImage(icon, tooltip)));
		button.setToolTipText(tooltip + shortcut.text);
		return button;
	}

	public static JButton create(String label, String tooltip, String icon, OperationsShortcutEnum shortcut) {
		JButton button = new JButton(label, new ImageIcon(Icons.createIconImage(icon, label)));
		button.setToolTipText(tooltip + shortcut.text);
		return button;
	}
}
