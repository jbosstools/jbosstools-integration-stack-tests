package org.jboss.tools.switchyard.reddeer.condition;

import org.eclipse.reddeer.common.condition.AbstractWaitCondition;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.jboss.tools.switchyard.reddeer.debug.ResumeButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class IsSuspended extends AbstractWaitCondition {

	private ResumeButton resumeButton;

	public IsSuspended() {
		this(new ResumeButton());
	}

	public IsSuspended(ResumeButton resumeButton) {
		this.resumeButton = resumeButton;
	}

	@Override
	public boolean test() {
		if (resumeButton.isEnabled()) {
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
