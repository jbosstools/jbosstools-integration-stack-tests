package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.switchyard.reddeer.debug.ResumeButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class IsSuspended implements WaitCondition {

	@Override
	public boolean test() {
		if (new ResumeButton().isEnabled()) {
			AbstractWait.sleep(TimePeriod.SHORT);
			return true;
		}
		return false;
	}

	@Override
	public String description() {
		return "Debugger didn't suspend";
	}

}
