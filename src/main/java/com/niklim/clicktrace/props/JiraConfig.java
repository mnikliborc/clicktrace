package com.niklim.clicktrace.props;

import com.google.common.base.Optional;

public class JiraConfig {
	private String instanceUrl;
	private String username;

	private Optional<String> passwordOpt = Optional.<String> absent();

	public JiraConfig(String url, String username) {
		this.instanceUrl = url;
		this.username = username;
	}

	public String getInstanceUrl() {
		return instanceUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.passwordOpt = Optional.of(password);
	}

	public Optional<String> getPassword() {
		return passwordOpt;
	}

}