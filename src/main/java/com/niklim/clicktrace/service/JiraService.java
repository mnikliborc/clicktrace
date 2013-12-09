package com.niklim.clicktrace.service;

import java.net.URI;
import java.net.URISyntaxException;

import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.google.inject.Inject;
import com.niklim.clicktrace.AppProperties;
import com.niklim.clicktrace.AppProperties.JiraConfig;
import com.niklim.clicktrace.jira.client.ClicktraceJiraRestClient;
import com.niklim.clicktrace.jira.client.ClicktraceJiraRestClient.Result;
import com.niklim.clicktrace.jira.client.ClicktraceJiraRestClient.Result.Status;

public class JiraService {
	@Inject
	private AppProperties props;

	public boolean checkSessionExist(String username, String password, String issueKey,
			String sessionName, String jiraInstanceUrl) throws JiraException {
		try {
			JiraConfig jiraConfig = props.getJiraConfig();
			ClicktraceJiraRestClient client = createClient(username, password, jiraInstanceUrl);
			Result res = client.checkSession(issueKey, sessionName,
					jiraInstanceUrl + jiraConfig.getRestPath());
			if (res.status == Status.NO_SESSION) {
				return false;
			} else if (res.status == Status.SESSION_EXISTS) {
				return true;
			} else {
				throw new JiraException(res.msg);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new JiraException(e.getMessage());
		}
	}

	public void exportSession(String username, String password, String issueKey,
			String sessionName, String content, String jiraInstanceUrl) throws JiraException {
		JiraConfig jiraConfig = props.getJiraConfig();
		ClicktraceJiraRestClient client;
		try {
			client = createClient(username, password, jiraInstanceUrl);
			Result res = client.exportSession(issueKey, sessionName, content, jiraInstanceUrl
					+ jiraConfig.getRestPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new JiraException(e.getMessage());
		}
	}

	private ClicktraceJiraRestClient createClient(String username, String password,
			String jiraInstanceUrl) throws URISyntaxException {
		ClicktraceJiraRestClient client = new ClicktraceJiraRestClient(
				new AsynchronousHttpClientFactory().createClient(new URI(jiraInstanceUrl),
						new BasicHttpAuthenticationHandler(username, password)));
		return client;
	}

}
