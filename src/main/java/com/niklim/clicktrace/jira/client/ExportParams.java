package com.niklim.clicktrace.jira.client;

/**
 * Stores data required for session export to JIRA.
 */
public class ExportParams {
	public final String username;
	public final String password;
	public final String jiraUrl;

	public final String issueKey;
	public final String sessionName;

	public ExportParams(String username, String password, String jiraUrl, String issueKey, String sessionName) {
		this.username = username;
		this.password = password;
		this.jiraUrl = jiraUrl;
		this.issueKey = issueKey;
		this.sessionName = sessionName;
	}
}
