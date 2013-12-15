package com.niklim.clicktrace.service;

import java.net.URI;
import java.net.URISyntaxException;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient.Result;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient.Result.Status;
import com.niklim.clicktrace.props.AppProperties;
import com.niklim.clicktrace.service.exception.JiraExportException;

@Singleton
public class JiraExportService {
	@Inject
	private AppProperties appProps;

	public boolean checkSessionExist(String username, String password, String issueKey,
			String sessionName, String jiraInstanceUrl) throws JiraExportException {
		try {
			JiraRestClicktraceClient client = createClient(username, password, jiraInstanceUrl);
			Result res = client.checkSession(issueKey, sessionName);

			return handleCheckSessionExistsResult(res);
		} catch (URISyntaxException e) {
			throw new JiraExportException(e.getMessage());
		}
	}

	private boolean handleCheckSessionExistsResult(Result res) throws JiraExportException {
		if (res.status == Status.NO_SESSION) {
			return false;
		} else if (res.status == Status.SESSION_EXISTS) {
			return true;
		} else {
			throw new JiraExportException(res.msg);
		}
	}

	public void exportSession(String username, String password, String issueKey,
			String sessionName, String stream, String jiraInstanceUrl) throws JiraExportException {
		try {
			JiraRestClicktraceClient client = createClient(username, password, jiraInstanceUrl);
			Result res = client.exportSession(issueKey, sessionName, stream);

			if (res.status == Result.Status.ERROR) {
				throw new JiraExportException(res.msg);
			}
		} catch (URISyntaxException e) {
			throw new JiraExportException(e.getMessage());
		}
	}

	private JiraRestClicktraceClient createClient(String username, String password,
			String jiraInstanceUrl) throws URISyntaxException {
		URI serverUri = new URI(jiraInstanceUrl);
		BasicHttpAuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(
				username, password);
		HttpClient httpClient = new AsynchronousHttpClientFactory().createClient(serverUri,
				authenticationHandler);

		return new JiraRestClicktraceClient(httpClient, jiraInstanceUrl,
				appProps.getJiraRestClicktraceImportPath());
	}

}
