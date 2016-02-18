package org.jboss.tools.switchyard.ui.bot.test;

public class KnownIssue extends AssertionError {

	private static final long serialVersionUID = 1L;

	public KnownIssue(String issue) {
		super("Please see https://issues.jboss.org/browse/" + issue);
	}

	
}
