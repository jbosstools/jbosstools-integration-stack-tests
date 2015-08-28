package org.jboss.tools.bpmn2.reddeer;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.core.reference.ReferencedComposite;

/**
 * 
 */
public class DefaultCombo extends LabeledCombo {

	/**
	 * 
	 * @param label
	 */
	public DefaultCombo(String label) {
		super(label);	
	}
	
	
	public DefaultCombo(ReferencedComposite section, String label) {
		super(section, label);	
	}
	/**
	 * 
	 * @param text
	 * @return
	 */
	public boolean contains(String text) {
		return getItems().contains(text);
	}
	
}
