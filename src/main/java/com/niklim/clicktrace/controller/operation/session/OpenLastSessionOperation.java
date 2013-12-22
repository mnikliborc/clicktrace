package com.niklim.clicktrace.controller.operation.session;

import com.google.inject.Inject;
import com.niklim.clicktrace.controller.MainController;
import com.niklim.clicktrace.controller.operation.AbstractOperation;
import com.niklim.clicktrace.model.Session;
import com.niklim.clicktrace.props.UserProperties;
import com.niklim.clicktrace.service.SessionManager;

public class OpenLastSessionOperation extends AbstractOperation {
	@Inject
	private UserProperties props;

	@Inject
	private SessionManager sessionManager;

	@Inject
	private MainController mainController;

	@Override
	public void perform() {
		final Session session = sessionManager.findSessionByName(props.getLastSessionName());
		if (session != null) {
			mainController.showSession(session);
		}
	}

	public boolean lastSessionExists() {
		Session session = sessionManager.findSessionByName(props.getLastSessionName());
		return session != null;
	}

}
