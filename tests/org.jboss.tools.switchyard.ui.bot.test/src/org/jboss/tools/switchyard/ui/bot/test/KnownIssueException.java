package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.common.exception.RedDeerException;

public class KnownIssueException extends RedDeerException {

	private static final long serialVersionUID = 1L;

	public KnownIssueException(String issue, Throwable cause) {
		super(getMessage(issue), cause);
	}

	private static String getMessage(String issue) {
		StringBuffer message = new StringBuffer("This could be a known issue.");
		message.append(" Please see https://issues.jboss.org/browse/").append(issue);
		return message.toString();
	}
}
