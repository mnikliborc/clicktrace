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
import com.niklim.clicktrace.Messages;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient.Result.Status;

public class JiraRestClicktraceClient extends AbstractAsynchronousRestClient {
	private static final Logger log = LoggerFactory.getLogger(JiraRestClicktraceClient.class);

	static final String JSON_CLICKTRACE_STREAM_FIELD_NAME = "stream";

	private final String jiraInstanceUrl;
	private final String jiraRestClicktraceImportPath;

	public JiraRestClicktraceClient(HttpClient client, String jiraInstanceUrl,
			String jiraRestClicktraceImportPath) {
		super(client);
		this.jiraInstanceUrl = jiraInstanceUrl;
		this.jiraRestClicktraceImportPath = jiraRestClicktraceImportPath;
	}

	public Result checkSession(String issueKey, String sessionName) {
		try {
			log.debug("Check session: issueKey='{}', sessionName='{}', URL='{}'", issueKey,
					sessionName, jiraInstanceUrl + jiraRestClicktraceImportPath);

			String uri = createUriString(issueKey, sessionName);
			Promise<Result> p = getAndParse(new URI(uri), new JsonResponseParser());
			return p.claim();
		} catch (RestClientException e) {
			return handleRestClientException(e);
		} catch (Throwable e) {
			e.printStackTrace();
			return new Result(Result.Status.ERROR, e.getMessage());
		}
	}

	private String createUriString(String issueKey, String sessionName) {
		return jiraInstanceUrl + jiraRestClicktraceImportPath + "/" + issueKey + "/" + sessionName;
	}

	private Result handleRestClientException(RestClientException e) {
		if (e.getStatusCode().or(0) == 401) {
			return new Result(Result.Status.ERROR, Messages.EXPORT_AUTHORIZATION_ERROR);
		} else if (e.getStatusCode().or(0) == 404) {
			return new Result(Result.Status.ERROR, Messages.EXPORT_WRONG_URL_ERROR);
		} else if (e.getStatusCode().or(0) == 403) {
			return new Result(Result.Status.ERROR, Messages.EXPORT_CAPTCHA_ERROR);
		} else {
			e.printStackTrace();
			return new Result(Result.Status.ERROR, Messages.EXPORT_UNKNOWN_SERVER_ERROR);
		}
	}

	public Result exportSession(String issueKey, String sessionName, String stream) {

		try {
			log.debug("Export session: issueKey='{}', sessionName='{}', URL='{}'", issueKey,
					sessionName, jiraInstanceUrl + jiraRestClicktraceImportPath);

			JSONObject json = new JSONObject();
			json.put(JSON_CLICKTRACE_STREAM_FIELD_NAME, stream);

			String uri = createUriString(issueKey, sessionName);
			Promise<Result> p = postAndParse(new URI(uri), json, new JsonResponseParser());
			return p.claim();
		} catch (RestClientException e) {
			return handleRestClientException(e);
		} catch (Throwable e) {
			e.printStackTrace();
			return new Result(Result.Status.ERROR, e.getMessage());
		}
	}

	public class JsonResponseParser implements JsonObjectParser<Result> {
		private static final String JIRA_RESPONSE_MSG_FIELD = "msg";
		private static final String JIRA_RESPONSE_STATUS_FIELD = "status";

		@Override
		public Result parse(JSONObject json) throws JSONException {
			Status status = Result.Status.valueOf(json.getString(JIRA_RESPONSE_STATUS_FIELD));
			String msg = json.optString(JIRA_RESPONSE_MSG_FIELD);
			return new Result(status, msg);
		}
	}

	public static class Result {
		public static enum Status {
			NO_SESSION, SESSION_EXISTS, OK, ERROR
		}

		public Result(Status status, String msg) {
			this.msg = msg;
			this.status = status;
		}

		public Result(Status status) {
			this.status = status;
			this.msg = null;
		}

		public final String msg;
		public final Status status;
	}
}
