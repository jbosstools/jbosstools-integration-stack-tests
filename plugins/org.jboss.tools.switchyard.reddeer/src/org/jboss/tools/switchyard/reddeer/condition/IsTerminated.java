package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.tools.switchyard.reddeer.debug.TerminateButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class IsTerminated implements WaitCondition {

	@Override
	public boolean test() {
		return !new TerminateButton().isEnabled();
	}

	@Override
	public String description() {
		return "Tool item with tooltip 'Terminate' is enabled";
	}

}
