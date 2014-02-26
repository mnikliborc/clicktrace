package com.niklim.clicktrace.props;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.msg.ErrorMsgs;

/**
 * Base class for handling general properties.
 */
public abstract class AbstractProperties {
	private static final Logger log = LoggerFactory.getLogger(AbstractProperties.class);
	protected PropertiesConfiguration props;
	protected static final Map<String, Object> defaults = new HashMap<String, Object>();

	protected void init() {
		try {
			props = new PropertiesConfiguration(getPropertiesFilePath());
		} catch (ConfigurationException e) {
			log.error(ErrorMsgs.PROPS_LOAD_ERROR, e);
			ErrorNotifier.notify(ErrorMsgs.PROPS_LOAD_ERROR);
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
			log.error(ErrorMsgs.PROPS_SAVE_ERROR, e);
			ErrorNotifier.notify(ErrorMsgs.PROPS_SAVE_ERROR);
		}
	}
}
