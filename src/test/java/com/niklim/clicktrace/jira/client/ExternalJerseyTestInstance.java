package com.niklim.clicktrace.jira.client;

import com.sun.jersey.test.framework.JerseyTest;

public class ExternalJerseyTestInstance extends JerseyTest {

	public static final int PORT = 9998;

	public ExternalJerseyTestInstance() {
		super("com.niklim.clicktrace.jira.client");
	}

}