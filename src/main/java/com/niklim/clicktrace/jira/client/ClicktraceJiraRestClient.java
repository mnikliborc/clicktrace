package com.niklim.clicktrace.jira.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.util.concurrent.Promise;
import com.niklim.clicktrace.jira.client.ClicktraceJiraRestClient.Result.Status;

public class ClicktraceJiraRestClient extends AbstractAsynchronousRestClient {
	private static final String CLICKTRACE_IMPORT_RESOURCE = "/clicktrace/import/";

	public ClicktraceJiraRestClient(HttpClient client) {
		super(client);
	}

	public Result checkSession(String issueKey, String sessionName, String jiraRestUrl) {
		try {
			Promise<Result> p = getAndParse(new URI(jiraRestUrl + CLICKTRACE_IMPORT_RESOURCE
					+ issueKey + "/" + sessionName), new JsonResponseParser());
			try {
				return p.claim();
			} catch (RestClientException e) {
				e.printStackTrace();
				return new Result(Result.Status.ERROR, e.getMessage());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return new Result(Result.Status.ERROR, e.getMessage());
		}
	}

	public Result exportSession(String issueKey, String sessionName, String content,
			String jiraRestUrl) {

		JSONObject json = new JSONObject();
		try {
			json.put("content", content);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		try {
			Promise<Result> p = postAndParse(new URI(jiraRestUrl + CLICKTRACE_IMPORT_RESOURCE
					+ issueKey + "/" + sessionName), json, new JsonResponseParser());
			try {
				return p.claim();
			} catch (RestClientException e) {
				e.printStackTrace();
				return new Result(Result.Status.ERROR, e.getMessage());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return new Result(Result.Status.ERROR, e.getMessage());
		}
	}

	public class JsonResponseParser implements JsonObjectParser<Result> {
		@Override
		public Result parse(JSONObject json) throws JSONException {
			Status status = Result.Status.valueOf(json.getString("status"));
			String msg = json.optString("msg");
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
