package com.niklim.clicktrace.model.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.niklim.clicktrace.model.Click;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.service.SessionManager;

/**
 * Reads {@link Session}'s properties file.
 */
public class SessionPropertiesReader extends SessionPropertiesIO {
	public SessionPropertiesReader(Session session) {
		super(session);
	}

	public String getLabel(String shotFilename) {
		return props.getString(shotFilename + SessionManager.PROP_SUFFIX_LABEL);
	}

	public String getShotDescription(String shotFilename) {
		return props.getString(shotFilename + SessionManager.PROP_SUFFIX_DESCRIPTION);
	}

	public String getSessionDescription() {
		return props.getString(SessionManager.PROP_SESSION_DESCRIPTION);
	}

	public List<Click> getClicks(String shotFilename) {
		return Click.getList(Strings.nullToEmpty(props.getString(shotFilename + SessionManager.PROP_SUFFIX_CLICKS)));
	}

	public Optional<Map<String, Integer>> getOrdering() {
		String ordString = props.getString(SessionManager.PROP_SESSION_ORDERING);
		if (StringUtils.isEmpty(ordString)) {
			return Optional.<Map<String, Integer>> absent();
		}

		Map<String, Integer> ordering = new HashMap<String, Integer>();
		int i = 0;
		for (String imgFilename : ordString.split(";")) {
			ordering.put(imgFilename, i++);
		}
		return Optional.of(ordering);
	}
}
