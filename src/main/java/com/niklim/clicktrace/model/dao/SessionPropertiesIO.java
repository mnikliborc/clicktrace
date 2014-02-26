package com.niklim.clicktrace.model.dao;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.FileManager;

/**
 * Base class for Session's properties file operations.
 */
public abstract class SessionPropertiesIO {
	protected static final Logger log = LoggerFactory.getLogger(SessionPropertiesIO.class);
	protected PropertiesStore props;
	protected final Session session;

	public SessionPropertiesIO(Session session, UserProperties userProps) {
		this.session = session;

		File file = new File(userProps.getSessionsDirPath() + session.getName() + File.separator
				+ FileManager.SESSION_PROPS_FILENAME);
		try {
			props = new PropertiesStore();
			props.setFile(file);
			props.load();
		} catch (IOException e) {
			log.error(ErrorMsgs.SESSION_DELETE_PROPS_ERROR, e);
			createPropertiesFile(file);
		}
	}

	private void createPropertiesFile(File file) {
		try {
			props = new PropertiesStore(file);
			props.save();
		} catch (IOException e) {
			// no need to log anything
		}
	}
}
