package com.niklim.clicktrace.tray;

public interface TrayController {
	void startSession();

	void stopSession();

	String getSessionName();

	void setSessionName(String name);

	void openEditor();
}
