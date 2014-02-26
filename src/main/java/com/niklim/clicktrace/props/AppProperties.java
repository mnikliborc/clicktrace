package com.niklim.clicktrace.props;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * App specific properties. Very unlikely to be modified by the user.
 */
@Singleton
public class AppProperties extends AbstractProperties {
	private static final String APP_PROPERTIES_PATH = "app.properties";
	private static final String JIRA_REST_CLICKTRACE_IMPORT_PATH = "jira.rest.clicktrace.import.path";
	private static final String USER_PROPERTIES_PATH = "user.properties.path";

	@Inject
	protected void init() {
		super.init();
	}

	static {
		defaults.put(JIRA_REST_CLICKTRACE_IMPORT_PATH, "/rest/clicktrace/1.0/clicktrace/import");
		defaults.put(USER_PROPERTIES_PATH, "user.properties");
	}

	@Override
	protected File getPropertiesFilePath() {
		return new File(APP_PROPERTIES_PATH);
	}

	public String getJiraRestClicktraceImportPath() {
		return props.getString(JIRA_REST_CLICKTRACE_IMPORT_PATH);
	}

	public String getUserPropertiesPath() {
		return props.getString(USER_PROPERTIES_PATH);
	}
}
