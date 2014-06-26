package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * SCA binding page
 * 
 * @author apodhrad
 * 
 */
public class SCABindingPage extends OperationOptionsPage<SCABindingPage> {

	public static final String CLUSTERED = "Clustered";

	public SCABindingPage setClustered(boolean clustered) {
		new CheckBox(CLUSTERED).toggle(clustered);
		return this;
	}

	public boolean isClustered() {
		return new CheckBox(CLUSTERED).isChecked();
	}
}
