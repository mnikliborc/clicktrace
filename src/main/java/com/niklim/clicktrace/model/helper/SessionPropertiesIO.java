package com.niklim.clicktrace.model.helper;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		File file = new File(FileManager.SESSIONS_DIR + session.getName() + File.separator
				+ FileManager.SESSION_PROPS_FILENAME);
		try {
			props = new PropertiesConfiguration();
			props.setListDelimiter((char) 0);
			props.setFile(file);
			props.load();
		} catch (ConfigurationException e) {
			log.error(ErrorMsgs.SESSION_DELETE_PROPS_ERROR, e);
			createPropertiesFile(file);
		}
	}

	private void createPropertiesFile(File file) {
		try {
			props = new PropertiesConfiguration(file);
			props.setListDelimiter((char) 0);
			props.save();
		} catch (ConfigurationException e) {
			log.error(ErrorMsgs.SESSION_SAVE_PROPS_ERROR, e);
		}
	}
}
