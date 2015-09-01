package org.jboss.tools.teiid.reddeer.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * Condition that specifies if a progress window is still present
 * 
 * @author apodhrad
 * 
 */
public class RadioButtonEnabled implements WaitCondition {

	private String LABEL;

	public RadioButtonEnabled(String label){
		this.LABEL = label;
	}
	
	@Override
	public boolean test() {
		try {
			new RadioButton(LABEL).isEnabled();
			return true;
		} catch (Exception e) {
			// ok, not enabled
			return false;
		}
	}

	@Override
	public String description() {
		return "Radio button with label " + LABEL + " not enabled yet";
	}

}
