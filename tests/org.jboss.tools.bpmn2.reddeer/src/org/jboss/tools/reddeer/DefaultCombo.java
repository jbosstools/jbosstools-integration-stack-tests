package org.jboss.tools.reddeer;

import org.jboss.reddeer.swt.impl.combo.LabeledCombo;

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
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public boolean contains(String text) {
		return getItems().contains(text);
	}
	
}
