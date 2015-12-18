package org.jboss.tools.fuse.reddeer.debug;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;

/**
 * Checks whether debugging is running
 * 
 * @author tsedmik
 */
public class IsRunning extends AbstractWaitCondition {

	@Override
	public boolean test() {

		if (new SuspendButton().isEnabled()) {
			AbstractWait.sleep(TimePeriod.SHORT);
			return true;
		}
		return false;
	}

	@Override
	public String description() {

		return "Debugger didn't running";
	}
}
