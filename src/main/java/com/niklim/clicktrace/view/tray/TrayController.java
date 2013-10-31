package com.niklim.clicktrace.view.tray;

public interface TrayController {
	void startSession();

	void stopSession();

	String getActiveSessionName();

	void setSessionName(String name);

	void openEditor();
}
