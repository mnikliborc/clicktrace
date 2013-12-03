package com.niklim.clicktrace.model.session.helper;

import java.io.File;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.niklim.clicktrace.FileManager;
import com.niklim.clicktrace.model.session.Session;

public abstract class SessionPropertiesIO {
	protected PropertiesConfiguration props;

	public SessionPropertiesIO(Session session) {
		try {
			props = new PropertiesConfiguration(FileManager.SESSIONS_DIR + session.getName() + File.separator
					+ FileManager.SESSION_PROPS_FILENAME);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			props = new PropertiesConfiguration();
		}
	}
}
