package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;

/**
 * SCA binding page
 * 
 * @author apodhrad
 * 
 */
public class SCABindingPage extends OperationOptionsPage<SCABindingPage> {

	public CheckBox getClustered() {
		return new CheckBox(new DefaultGroup("Clustering"), "Clustered");
	}
}
