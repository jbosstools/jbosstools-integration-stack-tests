package org.jboss.tools.fuse.reddeer.debug;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;

/**
 * Checks whether debugging is suspended
 * 
 * @author apodhrad
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
