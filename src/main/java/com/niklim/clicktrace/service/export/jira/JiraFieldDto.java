package com.niklim.clicktrace.service.export.jira;

public class JiraFieldDto {
	public final String label;
	public final String value;

	public JiraFieldDto(String label, String value) {
		this.label = label;
		this.value = value;
	}

	@Override
	public String toString() {
		return label;
	}
}
