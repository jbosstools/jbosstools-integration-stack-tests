package org.jboss.tools.common.reddeer;

/**
 * 
 * @author apodhrad
 *
 */
public class JiraIssue extends AssertionError {

	private static final long serialVersionUID = 1L;

	public JiraIssue(String issue) {
		super("Please see https://issues.jboss.org/browse/" + issue);
	}

	public JiraIssue(String issue, Throwable cause) {
		super("Please see https://issues.jboss.org/browse/" + issue, cause);
	}

}