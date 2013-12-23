package com.niklim.clicktrace.model.helper;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.service.FileManager;

/**
 * Base class for Session's properties file operations.
 */
public abstract class SessionPropertiesIO {
	protected static final Logger log = LoggerFactory.getLogger(SessionPropertiesIO.class);
	protected PropertiesConfiguration props;
	protected final Session session;

	public SessionPropertiesIO(Session session) {
		this.session = session;
		try {
			props = new PropertiesConfiguration(new File(FileManager.SESSIONS_DIR + session.getName() + File.separator
					+ FileManager.SESSION_PROPS_FILENAME));
		} catch (ConfigurationException e) {
			log.error(ErrorMsgs.SESSION_DELETE_PROPS_ERROR, e);
			ErrorNotifier.notify(ErrorMsgs.SESSION_DELETE_PROPS_ERROR);
		}
	}
}
