package org.jboss.tools.drools.ui.bot.test.util;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;

public class ApplicationIsTerminated extends AbstractWaitCondition {
	private ConsoleView console;

	public ApplicationIsTerminated() {
		console = new ConsoleView();
		console.open();
	}

	public boolean test() {
		console.activate();
		return new DefaultLabel().getText().contains("<terminated>");
	}

	public String description() {
		return "Tests that currently running application is in terminated state";
	}

}
