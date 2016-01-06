package org.jboss.tools.bpmn2.ui.bot.complex.test;

import java.util.Arrays;

public class JBPM6ComplexException extends Exception {

	private static final long serialVersionUID = 6690405439725106144L;

	public JBPM6ComplexException(String[] knownIssues, Throwable cause) {
		super("Known issues: " + Arrays.toString(knownIssues) + "\n" + cause.getMessage(), cause);
	}
}
