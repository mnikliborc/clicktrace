package com.niklim.clicktrace.session.helper;

import com.google.inject.Inject;

public class SessionHelperFactory {

	@Inject
	private static ImageLoader imageLoader = new ImageLoader();
	@Inject
	private static ScreenShotLoader screenShotLoader = new ScreenShotLoader();
	@Inject
	private static SessionSaver sessionSaver = new SessionSaver();

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static ScreenShotLoader getScreenShotLoader() {
		return screenShotLoader;
	}

	public static SessionSaver getSessionSaver() {
		return sessionSaver;
	}
}
