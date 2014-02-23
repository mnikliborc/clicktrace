package com.niklim.clicktrace.service.export.jira;

import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.atlassian.util.concurrent.Effect;
import com.atlassian.util.concurrent.Promise;
import com.google.common.net.UrlEscapers;
import com.google.inject.Inject;
import com.niklim.clicktrace.jira.client.ExportResult;
import com.niklim.clicktrace.jira.client.ExportStatus;
import com.niklim.clicktrace.jira.client.JiraClientFactory;
import com.niklim.clicktrace.jira.client.JiraRestClicktraceClient;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.AppProperties;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.SessionCompressor;

public class JiraExporter {
	@Inject
	private SessionCompressor sessionCompressor;

	@Inject
	private UserProperties props;

	@Inject
	private AppProperties appProps;

	private Session session;

	private ExecutorService executor;
	private Future<String> compressedSession;

	private Promise<ExportResult> resultPromise;

	public static interface JiraExportCallback {
		void success();

		void failure(String msg);
	}

	public JiraExporter() {
		executor = Executors.newFixedThreadPool(1);
	}

	public void initExport(final Session session) {
		this.session = session;
		compressedSession = compressFuture(session, props.getExportImageWidth());
	}

	private Future<String> compressFuture(final Session session, final Integer imageWidth) {
		return executor.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				return sessionCompressor.compressWithImageWidth(session, new SessionFileLoader(imageWidth));
			}
		});
	}

	public void export(String username, String password, String issueKey, String jiraUrl,
			final JiraExportCallback callback) throws InterruptedException, ExecutionException {
		try {
			String stream = compressedSession.get();
			resultPromise = exportSession(username, password, issueKey, session.getName(), stream, jiraUrl);
			resultPromise.done(new Effect<ExportResult>() {
				public void apply(ExportResult res) {
					handleExportResult(res, callback);
				}
			}).fail(new Effect<Throwable>() {
				public void apply(Throwable arg0) {
					callback.failure(arg0.getLocalizedMessage());
				}
			});

			resultPromise.claim();
		} catch (URISyntaxException e) {
			callback.failure(e.getLocalizedMessage());
		}
	}

	private Promise<ExportResult> exportSession(String username, String password, String issueKey, String sessionName,
			String stream, String jiraInstanceUrl) throws URISyntaxException {
		sessionName = UrlEscapers.urlFragmentEscaper().escape(sessionName);

		JiraRestClicktraceClient client = JiraClientFactory.create(username, password, jiraInstanceUrl,
				appProps.getJiraRestClicktraceImportPath());
		return client.exportSession(issueKey, sessionName, stream);
	}

	private void handleExportResult(ExportResult result, JiraExportCallback callback) {
		if (result.status == ExportStatus.ERROR) {
			callback.failure(result.msg);
		} else {
			callback.success();
		}
	}

	public void cancelExport() {
		if (resultPromise != null && !resultPromise.isDone() && !resultPromise.isCancelled()) {
			resultPromise.cancel(true);
		}
	}

	public void cleanup() {
		compressedSession = null;
		resultPromise = null;
	}
}
