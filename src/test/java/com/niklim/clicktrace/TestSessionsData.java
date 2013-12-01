package com.niklim.clicktrace;

public enum TestSessionsData {
	EMPTY("test-data/empty-sessions", new String[] {}), SOME("test-data/some-sessions", new String[] { "one", "two" });

	private String path;
	private String[] sessionNames;

	TestSessionsData(String path, String[] sessionNames) {
		this.path = path;
		this.sessionNames = sessionNames;
	}

	public String getPath() {
		return path;
	}

	public String[] getSessionNames() {
		return sessionNames;
	}

}
