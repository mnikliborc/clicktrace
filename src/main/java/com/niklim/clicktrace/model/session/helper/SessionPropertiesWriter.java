package com.niklim.clicktrace.model.session.helper;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import com.niklim.clicktrace.model.session.Click;
import com.niklim.clicktrace.model.session.ScreenShot;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.service.SessionManager;

public class SessionPropertiesWriter extends SessionPropertiesIO {

	public SessionPropertiesWriter(Session session) {
		super(session);
	}

	public void clearShotProps(ScreenShot shot) {
		props.clearProperty(shot.getFilename() + ".clicks");
		props.clearProperty(shot.getFilename() + ".description");
		props.clearProperty(shot.getFilename() + ".label");
		save();
	}

	public void saveShotLabel(ScreenShot shot) {
		saveSessionProperty(shot.getFilename() + SessionManager.PROP_SUFFIX_LABEL, shot.getLabel());
	}

	public void saveShotDescription(ScreenShot shot) {
		saveSessionProperty(shot.getFilename() + SessionManager.PROP_SUFFIX_DESCRIPTION, shot.getDescription());
	}

	public void saveShotClicks(String shotFilename, List<Click> clicks) {
		saveSessionProperty(shotFilename + SessionManager.PROP_SUFFIX_CLICKS, Click.getString(clicks));
	}

	private void saveSessionProperty(String key, String value) {
		props.setProperty(key, value);
		save();
	}

	private void save() {
		try {
			props.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}
