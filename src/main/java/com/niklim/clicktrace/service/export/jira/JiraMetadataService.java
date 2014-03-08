package com.niklim.clicktrace.service.export.jira;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.IssueType;
import com.atlassian.jira.rest.client.domain.Priority;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousMetadataRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousProjectRestClient;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.niklim.clicktrace.props.JiraConfig;
import com.niklim.clicktrace.props.JiraConfig.JiraUserMetadata;

/**
 * Loads JIRA user available data.
 */
public class JiraMetadataService {

	public JiraUserMetadata loadUserMetadata(JiraConfig jiraConfig) throws URISyntaxException, InterruptedException,
			ExecutionException {
		URI restApiUri = new URI(jiraConfig.getInstanceUrl() + "/rest/api/2/");
		HttpClient httpClient = createHttpClient(jiraConfig, restApiUri);

		AsynchronousMetadataRestClient metadataClient = createMetadataClient(httpClient, restApiUri);
		Collection<JiraFieldDto> issueTypes = loadIssueTypes(jiraConfig, metadataClient);
		Collection<JiraFieldDto> priorities = loadPriorities(jiraConfig, metadataClient);

		AsynchronousProjectRestClient projectClient = createProjectClient(httpClient, restApiUri);
		Collection<JiraFieldDto> projects = loadProjects(projectClient);

		return new JiraUserMetadata(projects, issueTypes, priorities);
	}

	private Collection<JiraFieldDto> loadProjects(AsynchronousProjectRestClient projectClient)
			throws InterruptedException, ExecutionException {
		Iterable<BasicProject> projects = projectClient.getAllProjects().get();
		return projectToJiraFieldDtos(projects);
	}

	private Collection<JiraFieldDto> projectToJiraFieldDtos(Iterable<BasicProject> projects) {
		return Collections2.transform(Lists.newArrayList(projects), new Function<BasicProject, JiraFieldDto>() {
			public JiraFieldDto apply(BasicProject input) {
				return new JiraFieldDto(input.getKey(), input.getKey());
			}
		});
	}

	private AsynchronousProjectRestClient createProjectClient(HttpClient httpClient, URI restApiUri) {
		return new AsynchronousProjectRestClient(restApiUri, httpClient);
	}

	private Collection<JiraFieldDto> loadPriorities(JiraConfig jiraConfig, AsynchronousMetadataRestClient client)
			throws InterruptedException, ExecutionException {
		Iterable<Priority> issueTypes = client.getPriorities().get();
		return prioritiesToStrings(issueTypes);
	}

	private Collection<JiraFieldDto> prioritiesToStrings(Iterable<Priority> priorities) {
		return Collections2.transform(Lists.newArrayList(priorities), new Function<Priority, JiraFieldDto>() {
			public JiraFieldDto apply(Priority input) {
				return new JiraFieldDto(input.getName(), input.getName());
			}
		});
	}

	private AsynchronousMetadataRestClient createMetadataClient(HttpClient httpClient, URI restApiUri)
			throws URISyntaxException {
		return new AsynchronousMetadataRestClient(restApiUri, httpClient);
	}

	private HttpClient createHttpClient(JiraConfig jiraConfig, URI restApiUri) {
		BasicHttpAuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(
				jiraConfig.getUsername(), jiraConfig.getPassword().get());
		HttpClient httpClient = new AsynchronousHttpClientFactory().createClient(restApiUri, authenticationHandler);
		return httpClient;
	}

	private Collection<JiraFieldDto> loadIssueTypes(JiraConfig jiraConfig, AsynchronousMetadataRestClient client)
			throws URISyntaxException, InterruptedException, ExecutionException {
		Iterable<IssueType> issueTypes = client.getIssueTypes().get();
		return issueTypesToStrings(issueTypes);
	}

	private Collection<JiraFieldDto> issueTypesToStrings(Iterable<IssueType> issueTypes) {
		return Collections2.transform(Lists.newArrayList(issueTypes), new Function<IssueType, JiraFieldDto>() {
			public JiraFieldDto apply(IssueType input) {
				return new JiraFieldDto(input.getName(), input.getName());
			}
		});
	}

}
