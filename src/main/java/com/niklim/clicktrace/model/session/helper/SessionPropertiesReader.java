package com.niklim.clicktrace.model.session.helper;

import java.util.List;

import com.google.common.base.Strings;
import com.niklim.clicktrace.model.session.Click;
import com.niklim.clicktrace.model.session.Session;
import com.niklim.clicktrace.service.SessionManager;

public class SessionPropertiesReader extends SessionPropertiesIO {
	public SessionPropertiesReader(Session session) {
		super(session);
	}

	public String getLabel(String shotFilename) {
		return props.getString(shotFilename + SessionManager.PROP_SUFFIX_LABEL);
	}

	public String getDescription(String shotFilename) {
		return props.getString(shotFilename + SessionManager.PROP_SUFFIX_DESCRIPTION);
	}

	public List<Click> getClicks(String shotFilename) {
		return Click.getList(Strings.nullToEmpty(props.getString(shotFilename + ".clicks")));
	}
}
