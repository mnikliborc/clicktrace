package com.niklim.clicktrace.model.session.helper;


public class SessionHelperFactory {

	private static ImageLoader imageLoader = new ImageLoader();
	private static ScreenShotLoader screenShotLoader = new ScreenShotLoader();
	private static SessionSaver sessionSaver = new SessionSaver();
	private static SessionMetadataHelper sessionMetadataHelper = new SessionMetadataHelper();

	public static ImageLoader getImageLoader() {
		return imageLoader;
	}

	public static ScreenShotLoader getScreenShotLoader() {
		return screenShotLoader;
	}

	public static SessionSaver getSessionSaver() {
		return sessionSaver;
	}

	public static SessionMetadataHelper getSessionMetadataHelper() {
		return sessionMetadataHelper;
	}
}
