package com.niklim.clicktrace.props;

import java.util.Collection;

import com.google.common.base.Optional;
import com.niklim.clicktrace.service.export.jira.JiraFieldDto;

public class JiraConfig {
	private String instanceUrl;
	private String username;

	private Optional<String> passwordOpt = Optional.<String> absent();
	private Optional<JiraUserMetadata> userMetadataOpt = Optional.<JiraUserMetadata> absent();

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

	public void setUserMetadata(JiraUserMetadata userMetadata) {
		this.userMetadataOpt = Optional.of(userMetadata);
	}

	public Optional<JiraUserMetadata> getUserMetadata() {
		return userMetadataOpt;
	}

	public static class JiraUserMetadata {
		public final Collection<JiraFieldDto> issueTypes;
		public final Collection<JiraFieldDto> priorities;
		public final Collection<JiraFieldDto> projects;

		public JiraUserMetadata(Collection<JiraFieldDto> projects, Collection<JiraFieldDto> issueTypes,
				Collection<JiraFieldDto> priorities) {
			this.projects = projects;
			this.issueTypes = issueTypes;
			this.priorities = priorities;
		}
	}
}