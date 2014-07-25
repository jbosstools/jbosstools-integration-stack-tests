package org.jboss.tools.drools.ui.bot.test.util;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;

public class ApplicationIsTerminated implements WaitCondition {
	private ConsoleView console;

	public ApplicationIsTerminated() {
		console = new ConsoleView();
		console.open();
	}

	public boolean test() {
		// TODO Please verify if it is a correct implementation
		// return console.getTitle().contains("<terminated>");
		console.activate();
		return new DefaultLabel().getText().contains("<terminated>");
	}

	public String description() {
		return "Tests that currently running application is in terminated state";
	}

}
