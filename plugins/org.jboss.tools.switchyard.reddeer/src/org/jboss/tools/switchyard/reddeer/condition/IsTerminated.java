package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.tools.switchyard.reddeer.debug.TerminateButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class IsTerminated extends AbstractWaitCondition {

	@Override
	public boolean test() {
		return !new TerminateButton().isEnabled();
	}

	@Override
	public String description() {
		return "Tool item with tooltip 'Terminate' is enabled";
	}

}
