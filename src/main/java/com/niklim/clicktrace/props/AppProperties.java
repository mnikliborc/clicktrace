package com.niklim.clicktrace.props;

import java.io.File;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * App specific properties. Very unlikely to be modified by the user. For
 * Windows installation it stores path to user.properties and log file.
 */
@Singleton
public class AppProperties extends AbstractProperties {
	private static final String LOG4J_FILE_APPENDER_NAME = "FA";

	private static final String APP_PROPERTIES_PATH = "app.properties";
	private static final String JIRA_REST_CLICKTRACE_IMPORT_PATH = "jira.rest.clicktrace.import.path";
	private static final String USER_PROPERTIES_PATH = "user.properties.path";
	private static final String LOG_FILE_PATH = "log.file.path";

	@Inject
	protected void init() {
		super.init();

		initLog4j();
	}

	private void initLog4j() {
		FileAppender fa = new FileAppender();
		fa.setName(LOG4J_FILE_APPENDER_NAME);
		fa.setFile(getLogPath());
		fa.setLayout(new PatternLayout("%d{dd.MM.yyyy HH:mm:ss,SS} %-5p %C{1} - %m%n"));
		fa.setThreshold(Level.DEBUG);
		fa.setAppend(true);
		fa.activateOptions();

		Logger.getRootLogger().addAppender(fa);
	}

	static {
		defaults.put(JIRA_REST_CLICKTRACE_IMPORT_PATH, "/rest/clicktrace/1.0/clicktrace/import");
		defaults.put(USER_PROPERTIES_PATH, "user.properties");
		defaults.put(LOG_FILE_PATH, "clicktrace.log");
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

	public String getLogPath() {
		return props.getString(LOG_FILE_PATH);
	}
}
