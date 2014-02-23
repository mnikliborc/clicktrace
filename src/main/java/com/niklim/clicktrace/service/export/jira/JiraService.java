package com.niklim.clicktrace.service.export.jira;

import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.Request;
import com.atlassian.httpclient.api.Response;
import com.google.common.net.UrlEscapers;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.jira.client.ExportResult;
import com.niklim.clicktrace.jira.client.ExportStatus;
import com.niklim.clicktrace.jira.client.JiraClientFactory;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient;
import com.niklim.clicktrace.props.AppProperties;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.service.exception.JiraExportException;

@Singleton
public class JiraService {
	@Inject
	private AppProperties appProps;

	private static final String CREATE_SUCCESS = "Created";

	public String createIssue(JiraConfig jiraConfig, String project, String issueType, String priority, String summary,
			String description) throws URISyntaxException, IllegalStateException, IllegalArgumentException,
			InterruptedException, ExecutionException, JiraExportException, JSONException {
		String jiraInstanceUrl = jiraConfig.getInstanceUrl();
		String username = jiraConfig.getUsername();
		String password = jiraConfig.getPassword().get();

		HttpClient httpClient = JiraClientFactory.createHttpClient(username, password, jiraInstanceUrl);
		String jsonObj = createRequestEntity(project, issueType, priority, summary, description);
		Request r = createRequest(jiraInstanceUrl, httpClient, jsonObj);

		Response response = r.post().get();
		if (!StringUtils.equals(CREATE_SUCCESS, response.getStatusText())) {
			throw new JiraExportException(MessageFormat.format(
					"Unable to create the Issue: {0}.\nProbably not all required fields were set.",
					response.getStatusText()));
		}

		JSONObject jsonObject = new JSONObject(response.getEntity());
		return jsonObject.getString("key");
	}

	private Request createRequest(String jiraInstanceUrl, HttpClient httpClient, String jsonObj) {
		Request request = httpClient.newRequest(jiraInstanceUrl + "/rest/api/2/issue");
		request.setContentType("application/json");
		request.setEntity(jsonObj);
		return request;
	}

	private String createRequestEntity(String project, String issueType, String priority, String summary,
			String description) {
		//@formatter:off
		String jsonObj = "{"
				+ "\"fields\": {"
				+ "\"project\":"
				+ "{"
				+ "\"key\": \""+project+"\""
				+ "},"
				+ "\"summary\": \""+summary+"\","
				+ "\"description\": \""+StringEscapeUtils.escapeJava(description)+"\","
				+ "\"issuetype\": {" + "\"name\": \""+issueType+"\"" + "},"
				+ "\"priority\": {" + "\"name\": \""+priority+"\"" + "}" + "}" + "}";
		//@formatter:on
		return jsonObj;
	}

	public boolean checkSessionExist(String username, String password, String issueKey, String sessionName,
			String jiraInstanceUrl) throws JiraExportException {
		sessionName = UrlEscapers.urlFragmentEscaper().escape(sessionName);

		try {
			JiraRestClicktraceClient client = JiraClientFactory.createClicktraceClient(username, password,
					jiraInstanceUrl, appProps.getJiraRestClicktraceImportPath());
			ExportResult res = client.checkSession(issueKey, sessionName);

			return handleCheckSessionExistsResult(res);
		} catch (URISyntaxException e) {
			throw new JiraExportException(e.getMessage());
		}
	}

	private boolean handleCheckSessionExistsResult(ExportResult res) throws JiraExportException {
		if (res.status == ExportStatus.NO_SESSION) {
			return false;
		} else if (res.status == ExportStatus.SESSION_EXISTS) {
			return true;
		} else {
			throw new JiraExportException(res.msg);
		}
	}

}
