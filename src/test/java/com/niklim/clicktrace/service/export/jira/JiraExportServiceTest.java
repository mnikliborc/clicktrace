package com.niklim.clicktrace.service.export.jira;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.codehaus.jettison.json.JSONException;
import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.service.exception.JiraExportException;

@Ignore("Integration test. Use as a smoke test.")
public class JiraExportServiceTest {
	JiraExportService service = new JiraExportService();

	@Test
	public void createIssueTest() throws UnsupportedEncodingException, URISyntaxException, InterruptedException,
			ExecutionException, IllegalStateException, IllegalArgumentException, JiraExportException, JSONException {
		// given
		JiraConfig jiraConfig = new JiraConfig("http://localhost:2990/jira", "admin");
		jiraConfig.setPassword("admin");

		// when
		String issueKey = service.createIssue(jiraConfig, "CT", "Bug", "Blocker", "summmmmary", "descseseription");

		// then
		Assertions.assertThat(issueKey).isNotEmpty();
	}
}
