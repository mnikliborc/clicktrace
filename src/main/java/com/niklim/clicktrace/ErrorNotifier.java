package com.niklim.clicktrace;

import javax.swing.JOptionPane;

import com.niklim.clicktrace.view.MainFrameHolder;

/**
 * Shows errors to the user. Decouples View and the rest of the system.
 */
public class ErrorNotifier {
	public static void interrupt(String message, Object... params) {
		message = composeMessage(message, params);
		JOptionPane.showMessageDialog(MainFrameHolder.get(), message);
	}

	public static void notify(String message) {
		// TODO implement non interrupting error notification
	}

	private static String composeMessage(String message, Object... params) {
		if (params.length > 0) {
			for (Object param : params) {
				message = message.replaceFirst("{}", param.toString());
			}
		}
		return message;
	}
}
