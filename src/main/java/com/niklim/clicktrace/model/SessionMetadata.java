package com.niklim.clicktrace.model;

import java.util.Date;

/**
 * Stores Clicktrace session metadata - creation and modification dates, number
 * of screenshots.
 */
public class SessionMetadata {
	private final Date created;
	private final Date modified;
	private final int size;

	public SessionMetadata(Date created, Date modified, int size) {
		this.created = created;
		this.modified = modified;
		this.size = size;
	}

	public Date getCreated() {
		return created;
	}

	public Date getModified() {
		return modified;
	}

	public int getSize() {
		return size;
	}

}
