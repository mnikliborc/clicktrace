package com.niklim.clicktrace.props;

import java.io.File;

import com.google.inject.Singleton;

/**
 * App specific properties. Very unlikely to be modified by the user.
 */
@Singleton
public class AppProperties extends AbstractProperties {
	private static final String APP_PROPERTIES_PATH = "app.properties";
	private static final String JIRA_REST_CLICKTRACE_IMPORT_PATH = "jira.rest.clicktrace.import.path";

	public AppProperties() {
		super();
	}

	static {
		defaults.put(JIRA_REST_CLICKTRACE_IMPORT_PATH,
				"/rest/clicktrace/1.0/clicktrace/import");
	}

	@Override
	protected File getPropertiesFilePath() {
		return new File(APP_PROPERTIES_PATH);
	}

	public String getJiraRestClicktraceImportPath() {
		return props.getString(JIRA_REST_CLICKTRACE_IMPORT_PATH);
	}
}
