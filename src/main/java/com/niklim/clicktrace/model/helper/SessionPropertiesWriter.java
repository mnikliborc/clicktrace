package com.niklim.clicktrace.model.helper;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import com.google.common.base.Joiner;
import com.niklim.clicktrace.ErrorNotifier;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.model.ScreenShot;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.msg.ErrorMsgs;
import com.niklim.clicktrace.service.SessionManager;

/**
 * Writes to {@link Session}'s properties file.
 */
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

	public void saveSessionDescription() {
		saveSessionProperty(SessionManager.PROP_SESSION_DESCRIPTION, session.getDescription());
	}

	public void saveShotClicks(String shotFilename, List<Click> clicks) {
		if (!clicks.isEmpty()) {
			saveSessionProperty(shotFilename + SessionManager.PROP_SUFFIX_CLICKS, Click.getString(clicks));
		}
	}

	public void saveOrdering(List<String> imgFilenames) {
		saveSessionProperty(SessionManager.PROP_SESSION_ORDERING, Joiner.on(";").join(imgFilenames));
	}

	private void saveSessionProperty(String key, String value) {
		props.setProperty(key, value);
		save();
	}

	private void save() {
		try {
			props.save();
		} catch (ConfigurationException e) {
			log.error(ErrorMsgs.SESSION_SAVE_PROPS_ERROR, e);
			ErrorNotifier.notify(ErrorMsgs.SESSION_SAVE_PROPS_ERROR);
		}
	}
}
