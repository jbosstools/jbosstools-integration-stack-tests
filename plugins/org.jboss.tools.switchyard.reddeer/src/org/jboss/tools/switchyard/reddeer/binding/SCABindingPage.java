package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.reddeer.swt.api.Combo;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;

/**
 * SCA binding page
 * 
 * @author apodhrad
 * 
 */
public class SCABindingPage extends OperationOptionsPage<SCABindingPage> {

	public Combo getClustered() {
		return new LabeledCombo(new DefaultGroup("Clustering"), "Clustered");
	}
}
