package com.niklim.clicktrace.jira.client;

import java.net.URI;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.util.concurrent.Promise;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.msg.InfoMsgs;

public class JiraRestClicktraceClient extends AbstractAsynchronousRestClient {
	private static final String UNEXPECTED_ERROR_MSG = "Unexpected error while checking session in JIRA";

	private static final Logger log = LoggerFactory.getLogger(JiraRestClicktraceClient.class);

	static final String JSON_CLICKTRACE_STREAM_FIELD_NAME = "stream";

	private final String jiraInstanceUrl;
	private final String jiraRestClicktraceImportPath;

	JiraRestClicktraceClient(HttpClient client, String jiraInstanceUrl, String jiraRestClicktraceImportPath) {
		super(client);
		this.jiraInstanceUrl = jiraInstanceUrl;
		this.jiraRestClicktraceImportPath = jiraRestClicktraceImportPath;
	}

	public ExportResult checkSession(String issueKey, String sessionName) {
		try {
			log.debug("Check session: issueKey='{}', sessionName='{}', URL='{}'", issueKey, sessionName,
					jiraInstanceUrl + jiraRestClicktraceImportPath);

			String uri = createUriString(issueKey, sessionName);
			Promise<ExportResult> p = getAndParse(new URI(uri), new JsonResponseParser());
			return p.claim();
		} catch (RestClientException e) {
			return handleRestClientException(e);
		} catch (Throwable e) {
			log.error(UNEXPECTED_ERROR_MSG, e);
			return new ExportResult(ExportStatus.ERROR, e.getMessage());
		}
	}

	private String createUriString(String issueKey, String sessionName) {
		return jiraInstanceUrl + jiraRestClicktraceImportPath + "/" + issueKey + "/" + sessionName;
	}

	private ExportResult handleRestClientException(RestClientException e) {
		if (e.getStatusCode().or(0) == 401) {
			return new ExportResult(ExportStatus.ERROR, InfoMsgs.JIRA_EXPORT_AUTHENTICATION_FAILURE);
		} else if (e.getStatusCode().or(0) == 404) {
			return new ExportResult(ExportStatus.ERROR, InfoMsgs.JIRA_EXPORT_WRONG_URL);
		} else if (e.getStatusCode().or(0) == 403) {
			return new ExportResult(ExportStatus.ERROR, InfoMsgs.JIRA_EXPORT_CAPTCHA_NEEDED);
		} else {
			log.error(UNEXPECTED_ERROR_MSG, e);
			return new ExportResult(ExportStatus.ERROR, ErrorMsgs.JIRA_EXPORT_UNKNOWN_SERVER_ERROR);
		}
	}

	public ExportResult exportSession(String issueKey, String sessionName, String stream) {

		try {
			log.debug("Export session: issueKey='{}', sessionName='{}', URL='{}'", issueKey, sessionName,
					jiraInstanceUrl + jiraRestClicktraceImportPath);

			JSONObject json = new JSONObject();
			json.put(JSON_CLICKTRACE_STREAM_FIELD_NAME, stream);

			String uri = createUriString(issueKey, sessionName);
			Promise<ExportResult> p = postAndParse(new URI(uri), json, new JsonResponseParser());
			return p.claim();
		} catch (RestClientException e) {
			return handleRestClientException(e);
		} catch (Throwable e) {
			log.error(UNEXPECTED_ERROR_MSG, e);
			return new ExportResult(ExportStatus.ERROR, e.getMessage());
		}
	}

	public class JsonResponseParser implements JsonObjectParser<ExportResult> {
		private static final String JIRA_RESPONSE_MSG_FIELD = "msg";
		private static final String JIRA_RESPONSE_STATUS_FIELD = "status";

		@Override
		public ExportResult parse(JSONObject json) throws JSONException {
			ExportStatus status = ExportStatus.valueOf(json.getString(JIRA_RESPONSE_STATUS_FIELD));
			String msg = json.optString(JIRA_RESPONSE_MSG_FIELD);
			return new ExportResult(status, msg);
		}
	}
}
