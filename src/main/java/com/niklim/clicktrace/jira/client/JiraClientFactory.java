package com.niklim.clicktrace.jira.client;

import java.net.URI;
import java.net.URISyntaxException;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;

public class JiraClientFactory {
	public static JiraRestClicktraceClient create(String username, String password, String jiraInstanceUrl,
			String jiraRestClicktraceImportPath) throws URISyntaxException {
		HttpClient httpClient = createHttpClient(username, password, jiraInstanceUrl);
		return new JiraRestClicktraceClient(httpClient, jiraInstanceUrl, jiraRestClicktraceImportPath);
	}

	public static HttpClient createHttpClient(String username, String password, String jiraInstanceUrl)
			throws URISyntaxException {
		URI serverUri = new URI(jiraInstanceUrl);
		BasicHttpAuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(username, password);
		HttpClient httpClient = new AsynchronousHttpClientFactory().createClient(serverUri, authenticationHandler);
		return httpClient;
	}
}
