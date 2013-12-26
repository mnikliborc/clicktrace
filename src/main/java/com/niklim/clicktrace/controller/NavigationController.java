package com.niklim.clicktrace.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.view.MainView;

/**
 * Switches the screenshot being displayed.
 */
@Singleton
public class NavigationController {
	@Inject
	private MainView mainView;

	@Inject
	private ActiveSession activeSession;

	public void showFirstScreenShot() {
		showScreenShot(0);
	}

	public void showPrevScreenShot() {
		int selectedIndex = activeSession.getActiveShotIndex();
		int nextIndex = Math.max(0, selectedIndex - 1);

		if (selectedIndex != nextIndex) {
			showScreenShot(nextIndex);
		}
	}

	public void showNextScreenShot() {
		Session session = activeSession.getSession();
		int selectedIndex = activeSession.getActiveShotIndex();
		int nextIndex = Math.min(selectedIndex + 1, session.getShots().size() - 1);

		if (selectedIndex != nextIndex) {
			showScreenShot(nextIndex);
		}
	}

	public void showLastScreenShot() {
		Session session = activeSession.getSession();
		int nextIndex = session.getShots().size() - 1;

		showScreenShot(nextIndex);
	}

	public void showScreenShot(int i) {
		ScreenShot shot = activeSession.getSession().getShots().get(i);
		activeSession.setActiveShot(shot);
		mainView.showScreenShot(shot, activeSession.isShotSelected(shot));
		mainView.refresh();
	}
}
