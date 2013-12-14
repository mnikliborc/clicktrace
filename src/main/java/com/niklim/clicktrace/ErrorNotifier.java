package com.niklim.clicktrace;

import javax.swing.JOptionPane;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.view.MainView;

/**
 * Shows errors to the user. Decouples View and the rest of the system.
 */
@Singleton
public class ErrorNotifier {
	@Inject
	private MainView mainView;

	public void notify(String message) {
		JOptionPane.showMessageDialog(mainView.getFrame(), message);
	}
}
