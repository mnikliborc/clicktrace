package com.niklim.clicktrace;

import javax.swing.JOptionPane;


/**
 * Shows errors to the user. Decouples View and the rest of the system.
 */
public class ErrorNotifier {
	public void notify(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
}
