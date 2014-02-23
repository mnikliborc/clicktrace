package com.niklim.clicktrace.jira.client;

public class ExportResult {
	public ExportResult(ExportStatus status, String msg) {
		this.msg = msg;
		this.status = status;
	}

	public ExportResult(ExportStatus status) {
		this.status = status;
		this.msg = null;
	}

	public final String msg;
	public final ExportStatus status;
}