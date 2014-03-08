package com.niklim.clicktrace.jira.client;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.niklim.clicktrace.props.AppProperties;

public class ClicktraceJiraRestClientTest {
	private static final String JIRA_URL = "http://localhost:9998";
	private static final String JIRA_REST_CLICKTRACE_IMPORT_PATH = "/clicktrace/import";

	private static ExternalJerseyTestInstance jersey = new ExternalJerseyTestInstance();

	private static JiraRestClicktraceClient cl = new JiraRestClicktraceClient();

	@BeforeClass
	public static void setupJiraMock() throws Exception {
		jersey.setUp();
		setupAppProps();
		cl.init();
	}

	private static void setupAppProps() {
		AppProperties props = new AppProperties();
		props.init();
		props.setJiraRestClicktraceImportPath(JIRA_REST_CLICKTRACE_IMPORT_PATH);
		cl.setProps(props);
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

		// when
		ExportResult res = cl.checkSession(createExportParams(issueKey, sessionName));

		// then
		assertThat(res.status).isEqualTo(ExportStatus.ERROR);
	}

	private ExportParams createExportParams(String issueKey, String sessionName) {
		return new ExportParams("admin", "admin", JIRA_URL, issueKey, sessionName);
	}

	@Test
	public void testCheckExisting() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "existing";

		// when
		ExportResult res = cl.checkSession(createExportParams(issueKey, sessionName));

		// then
		assertThat(res.status).isEqualTo(ExportStatus.SESSION_EXISTS);
	}

	@Test
	public void testCheckNonExisting() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "nonExisting";

		// when
		ExportResult res = cl.checkSession(createExportParams(issueKey, sessionName));

		// then
		assertThat(res.status).isEqualTo(ExportStatus.NO_SESSION);
	}

	@Test
	public void testCheckError() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "error";

		// when
		ExportResult res = cl.checkSession(createExportParams(issueKey, sessionName));

		// then
		assertThat(res.status).isEqualTo(ExportStatus.ERROR);
		assertThat(res.msg).isEqualTo(JiraRestClicktraceImportMock.ERROR_MSG);
	}

	@Test
	public void testExport() throws Exception {
		// given
		String issueKey = "ABC-1";
		String sessionName = "name";
		String stream = JiraRestClicktraceImportMock.FAKE_STREAM;

		// when
		ExportResult result = cl.exportSession(createExportParams(issueKey, sessionName), stream);

		// then
		assertThat(result.status).isEqualTo(ExportStatus.OK);
	}
}
