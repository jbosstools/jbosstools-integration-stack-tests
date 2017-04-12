package org.jboss.tools.common.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * Wait condition which try toogle a radio button with defined label
 * 
 * @author djelinek
 *
 */
public class RadioButtonIsAvailable extends AbstractWaitCondition {

	private String label;

	public RadioButtonIsAvailable(String label) {
		this.label = label;
	}

	@Override
	public boolean test() {
		try {
			new RadioButton(label).toggle(true);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
