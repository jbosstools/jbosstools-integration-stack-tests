package org.jboss.tools.switchyard.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;

/**
 * 
 * @author apodhrad
 * 
 */
public class JUnitHasFinished implements WaitCondition {
	
	private JUnitView jUnitView;
	
	public JUnitHasFinished() {
		jUnitView = new JUnitView();
	}

	@Override
	public boolean test() {
		jUnitView.open();
		String status = jUnitView.getViewStatus();
		return status.startsWith("Finished");
	}

	@Override
	public String description() {
		return "JUnit test has not finished yet";
	}
}
