package org.jboss.tools.reddeer;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.lookup.ButtonLookup;
import org.jboss.reddeer.swt.matcher.WithLabelMatcher;

/**
 * 
 */
public class DefaultCheckBox extends CheckBox {

	/**
	 * 
	 */
	public DefaultCheckBox() {
		super();
	}
	
	/**
	 * 
	 * @param label
	 */
	public DefaultCheckBox(String label) {
		super();
		
		if (swtButton == null) {
			ButtonLookup.getInstance().getButton(null, 0, new WithLabelMatcher(label));
		}
	}

	/**
	 * 
	 * @param select
	 */
	public void setChecked(boolean select) {
		if ((isChecked() && !select) || (!isChecked() && select)) {
			click();
		}
	}
	
}
