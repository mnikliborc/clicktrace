package com.niklim.clicktrace.props;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Base class for handling general properties.
 */
public abstract class AbstractProperties {
	protected PropertiesConfiguration props;
	protected static final Map<String, Object> defaults = new HashMap<String, Object>();

	public AbstractProperties() {
		try {
			props = new PropertiesConfiguration(getPropertiesFilePath());
		} catch (ConfigurationException e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
}
