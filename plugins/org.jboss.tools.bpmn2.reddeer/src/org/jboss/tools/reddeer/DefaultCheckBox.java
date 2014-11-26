package org.jboss.tools.reddeer;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.lookup.ButtonLookup;
import org.jboss.reddeer.swt.matcher.WithLabelMatcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;

/**
 * 
 */
public class DefaultCheckBox extends CheckBox {

	public DefaultCheckBox() {
		swtButton = ButtonLookup.getInstance().getButton(null, 0);
	}
	
	/**
	 * 
	 * @param label
	 */
	public DefaultCheckBox(String label) {
		//super(label);
		
		if (swtButton == null) {
			swtButton = ButtonLookup.getInstance().getButton(null, 0, new WithLabelMatcher(label));
		}
	}
	
	public DefaultCheckBox(ReferencedComposite referenceComposite, String label) {
		//super(referenceComposite, label);
		
		if (swtButton == null) {
			swtButton = ButtonLookup.getInstance().getButton(referenceComposite, 0, new WithLabelMatcher(label));
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
