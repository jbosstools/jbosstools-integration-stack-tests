package org.jboss.tools.common.reddeer.debug;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;

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
