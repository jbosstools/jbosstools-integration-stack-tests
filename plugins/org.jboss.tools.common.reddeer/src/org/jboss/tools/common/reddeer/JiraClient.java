package org.jboss.tools.common.reddeer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Pattern;

public class JiraClient {

	public static final String JBOSS_JIRA = "https://issues.jboss.org/rest/api/latest";

	private String url;

	public JiraClient() {
		this(JBOSS_JIRA);
	}

	public JiraClient(String url) {
		this.url = url;
	}

	public String getIssue(String issueId) {
		try {
			return new HttpClient(url + "/issue/" + issueId).get();
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public boolean isIssueClosed(String issueId) {
		String issue = getIssue(issueId);
		if (issue != null) {
			Pattern pattern = Pattern.compile("\"name\":\"Closed\"");
			return pattern.matcher(issue).find();
		}
		return false;
	}
}
