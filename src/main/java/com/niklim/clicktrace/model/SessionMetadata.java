package com.niklim.clicktrace.model;

import java.util.Date;

/**
 * Stores Clicktrace session metadata - creation and modification dates, number
 * of screenshots.
 */
public class SessionMetadata {
	private final Date modified;
	private final int size;

	public SessionMetadata(Date modified, int size) {
		this.modified = modified;
		this.size = size;
	}

	public Date getModified() {
		return modified;
	}

	public int getSize() {
		return size;
	}

}
