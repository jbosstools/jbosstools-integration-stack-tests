package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;

/**
 * 
 * @author apodhrad
 * 
 */
public class ConsoleHasChanged extends AbstractWaitCondition {

	@Override
	public boolean test() {
		ConsoleView consoleView = new ConsoleView();

		consoleView.open();
		String consoleTextBefore = consoleView.getConsoleText();

		AbstractWait.sleep(TimePeriod.SHORT);

		consoleView.open();
		String consoleTextAfter = consoleView.getConsoleText();

		return !consoleTextBefore.equals(consoleTextAfter);
	}

	@Override
	public String description() {
		return "Console is still in work";
	}
}
