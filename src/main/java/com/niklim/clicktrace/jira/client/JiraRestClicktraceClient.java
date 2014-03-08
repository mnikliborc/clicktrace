package com.niklim.clicktrace.jira.client;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.niklim.clicktrace.props.AppProperties;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

/**
 * Client of Clicktrace Link for JIRA REST service. Based on jersey rest-client
 * for increasing connection timeout (Atlassian's rest client didn't work as
 * predicted).
 */
public class JiraRestClicktraceClient {
	private static final Logger log = LoggerFactory.getLogger(JiraRestClicktraceClient.class);

	static final String JSON_CLICKTRACE_STREAM_FIELD_NAME = "stream";
	private static final String UNEXPECTED_ERROR_MSG = "Unexpected error while checking session in JIRA";

	private static final String JIRA_RESPONSE_MSG_FIELD = "msg";
	private static final String JIRA_RESPONSE_STATUS_FIELD = "status";

	private Client client;

	@Inject
	private AppProperties props;

	void setProps(AppProperties props) {
		this.props = props;
	}

	@Inject
	public void init() {
		client = Client.create();
		client.setConnectTimeout(props.getJiraClientConnectTimeout());
		client.setReadTimeout(props.getJiraClientReadTimeout());
	}

	public ExportResult checkSession(ExportParams params) {
		try {
			String uri = createUriString(params);
			log.debug("Check session: URI='{}'", uri);

			return getAndParse(uri, params.username, params.password);
		} catch (Throwable e) {
			log.error(UNEXPECTED_ERROR_MSG, e);
			return new ExportResult(ExportStatus.ERROR, e.getMessage());
		}
	}

	private ExportResult getAndParse(String uri, String username, String password) throws JSONException {
		String auth = createAuthToken(username, password);
		WebResource webResource = client.resource(uri);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).get(ClientResponse.class);
		String responseString = response.getEntity(String.class);

		return parse(new JSONObject(responseString));
	}

	private String createUriString(ExportParams params) {
		return params.jiraUrl + props.getJiraRestClicktraceImportPath() + "/" + params.issueKey + "/"
				+ params.sessionName;
	}

	public ExportResult exportSession(ExportParams params, String stream) {
		try {
			String uri = createUriString(params);
			log.debug("Export session: URI='{}'", uri);

			JSONObject json = new JSONObject();
			json.put(JSON_CLICKTRACE_STREAM_FIELD_NAME, stream);

			return postAndParse(uri, params.username, params.password, json);
		} catch (Throwable e) {
			log.error(UNEXPECTED_ERROR_MSG, e);
			return new ExportResult(ExportStatus.ERROR, e.getMessage());
		}
	}

	private ExportResult postAndParse(String uri, String username, String password, JSONObject json)
			throws JSONException {
		String auth = createAuthToken(username, password);
		WebResource webResource = client.resource(uri);

		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.post(ClientResponse.class, json.toString());

		String responseString = response.getEntity(String.class);

		return parse(new JSONObject(responseString));
	}

	private String createAuthToken(String username, String password) {
		return new String(Base64.encode(username + ":" + password));
	}

	private ExportResult parse(JSONObject json) throws JSONException {
		ExportStatus status = ExportStatus.valueOf(json.getString(JIRA_RESPONSE_STATUS_FIELD));
		String msg = json.optString(JIRA_RESPONSE_MSG_FIELD);
		return new ExportResult(status, msg);
	}
}
