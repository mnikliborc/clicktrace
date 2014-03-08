package com.niklim.clicktrace.service.export.jira;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.UrlEscapers;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.niklim.clicktrace.dialog.jira.JiraExportDialog;
import com.niklim.clicktrace.jira.client.ExportParams;
import com.niklim.clicktrace.jira.client.ExportResult;
import com.niklim.clicktrace.jira.client.ExportStatus;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.SessionCompressor;
import com.niklim.clicktrace.service.exception.JiraExportException;

/**
 * Exports Clicktrace session to JIRA. Compresses the session in separate thread
 * (starts when the user enters {@link JiraExportDialog}).
 */
@Singleton
public class JiraExporter {
	private static final Logger log = LoggerFactory.getLogger(JiraExporter.class);

	@Inject
	private SessionCompressor sessionCompressor;

	@Inject
	private UserProperties props;

	@Inject
	private JiraRestClicktraceClient client;

	private ExecutorService executor;
	private Future<String> compressedSession;

	public JiraExporter() {
		executor = Executors.newFixedThreadPool(1);
	}

	public void initExport(final Session session) {
		compressedSession = compressFuture(session, props.getExportImageWidth());
	}

	private Future<String> compressFuture(final Session session, final Integer imageWidth) {
		return executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				InitImageWidthPropertyExportHandler.handle(props.getSessionsDirPath(), session, imageWidth);
				return sessionCompressor.compress(session);
			}
		});
	}

	public void exportSession(String username, String password, String issueKey, String sessionName,
			String jiraInstanceUrl) throws JiraExportException {
		sessionName = UrlEscapers.urlFragmentEscaper().escape(sessionName);

		try {
			String stream = compressedSession.get();
			// client
			ExportResult res = client.exportSession(new ExportParams(username, password, jiraInstanceUrl, issueKey,
					sessionName), stream);

			if (res.status == ExportStatus.ERROR) {
				throw new JiraExportException(res.msg);
			}
		} catch (Exception e) {
			log.error("", e);
			throw new JiraExportException(e.getLocalizedMessage());
		}
	}

	public void cleanup() {
		compressedSession = null;
	}
}
