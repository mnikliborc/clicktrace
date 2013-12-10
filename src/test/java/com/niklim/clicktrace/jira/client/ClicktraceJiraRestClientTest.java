package com.niklim.clicktrace.jira.client;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.niklim.clicktrace.jira.client.ClicktraceJiraRestClient.Result;

public class ClicktraceJiraRestClientTest {
	private static final String JIRA_URL = "http://localhost:9998";
	private static final String JIRA_CLICKTRACE_PATH = "";

	private static ExternalJerseyTestInstance jersey = new ExternalJerseyTestInstance();

	@BeforeClass
	public static void setupJiraMock() throws Exception {
		jersey.setUp();
	}

	@AfterClass
	public static void tearDownJiraMock() throws Exception {
		jersey.tearDown();
	}

	@Test
	public void testCheckNotLogged() throws Exception {
		// given

		String issueKey = "ABC-1";
		String sessionName = "notlogged";

		ClicktraceJiraRestClient cl = createClient();

		// when
		Result res = cl.checkSession(issueKey, sessionName, JIRA_URL + JIRA_CLICKTRACE_PATH);

		// then
		assertThat(res.status).isEqualTo(Result.Status.ERROR);
	}

	private ClicktraceJiraRestClient createClient() throws URISyntaxException {
		String user = "admin";
		String password = "admin";
		ClicktraceJiraRestClient cl = new ClicktraceJiraRestClient(
				new AsynchronousHttpClientFactory().createClient(new URI(JIRA_URL),
						new BasicHttpAuthenticationHandler(user, password)));
		return cl;
	}

	@Test
	public void testCheckExisting() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "existing";

		ClicktraceJiraRestClient cl = createClient();

		// when
		Result res = cl.checkSession(issueKey, sessionName, JIRA_URL + JIRA_CLICKTRACE_PATH);

		// then
		assertThat(res.status).isEqualTo(Result.Status.SESSION_EXISTS);
	}

	@Test
	public void testCheckNonExisting() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "nonExisting";

		ClicktraceJiraRestClient cl = createClient();

		// when
		Result res = cl.checkSession(issueKey, sessionName, JIRA_URL + JIRA_CLICKTRACE_PATH);

		// then
		assertThat(res.status).isEqualTo(Result.Status.NO_SESSION);
	}

	@Test
	public void testCheckError() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "error";

		ClicktraceJiraRestClient cl = createClient();

		// when
		Result res = cl.checkSession(issueKey, sessionName, JIRA_URL + JIRA_CLICKTRACE_PATH);

		// then
		assertThat(res.status).isEqualTo(Result.Status.ERROR);
		assertThat(res.msg).isEqualTo(ClicktraceJiraRestMock.ERROR_MSG);
	}

	@Test
	public void testExport() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "name";
		String stream = ".7 archive";

		ClicktraceJiraRestClient cl = createClient();

		// when
		Result res = cl.exportSession(issueKey, sessionName, stream, JIRA_URL
				+ JIRA_CLICKTRACE_PATH);

		// then
		assertThat(res.status).isEqualTo(Result.Status.OK);
	}
}
