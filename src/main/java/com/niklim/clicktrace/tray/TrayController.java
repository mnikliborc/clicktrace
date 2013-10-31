package com.niklim.clicktrace.tray;

public interface TrayController {
	void startSession();

	void stopSession();

	String getActiveSessionName();

	void setSessionName(String name);

	void openEditor();
}
