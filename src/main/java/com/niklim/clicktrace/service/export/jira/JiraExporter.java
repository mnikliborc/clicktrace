package com.niklim.clicktrace.service.export.jira;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.inject.Inject;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.SessionCompressor;
import com.niklim.clicktrace.service.exception.JiraExportException;

public class JiraExporter {
	@Inject
	private JiraExportService jiraExportService;

	@Inject
	private SessionCompressor sessionCompressor;

	@Inject
	private UserProperties props;

	private Session session;

	ExecutorService executor;
	Future<String> compressedSession;

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

	public boolean checkSessionExists(String username, String password, String issueKey, String jiraUrl)
			throws JiraExportException {
		return jiraExportService.checkSessionExist(username, password, issueKey, session.getName(), jiraUrl);
	}

	public void export(String username, String password, String issueKey, String jiraUrl, Integer initImageWidth)
			throws JiraExportException, InterruptedException, ExecutionException {
		String stream = getCompressedSession(initImageWidth);

		jiraExportService.exportSession(username, password, issueKey, session.getName(), stream, jiraUrl);
	}

	private String getCompressedSession(Integer initImageWidth) throws InterruptedException, ExecutionException {
		if (props.getExportImageWidth().equals(initImageWidth)) {
			return compressedSession.get();
		} else {
			return compressFuture(session, initImageWidth).get();
		}
	}

	public void cleanup() {
		compressedSession = null;
	}
}
