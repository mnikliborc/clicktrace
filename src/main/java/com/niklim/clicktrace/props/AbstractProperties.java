package com.niklim.clicktrace.props;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for handling general properties.
 */
public abstract class AbstractProperties {
	private static final Logger log = LoggerFactory.getLogger(AbstractProperties.class);
	protected PropertiesConfiguration props;
	protected static final Map<String, Object> defaults = new HashMap<String, Object>();

	public AbstractProperties() {
		try {
			props = new PropertiesConfiguration(getPropertiesFilePath());
		} catch (ConfigurationException e) {
			log.error("Unable to load properties file", e);
		}
		initDefaults();
	}

	protected abstract File getPropertiesFilePath();

	private void initDefaults() {
		for (String key : defaults.keySet()) {
			Object property = props.getProperty(key);
			if (property == null) {
				props.setProperty(key, defaults.get(key));
			}
		}
	}

	public void save() {
		try {
			props.save();
		} catch (ConfigurationException e) {
			log.error("Unable to save properties file", e);
		}
	}
}
