package org.jboss.tools.bpel.reddeer.activity;

import org.eclipse.reddeer.swt.impl.ccombo.DefaultCCombo;

/**
 * 
 * @author apodhrad
 * 
 */
public class CompensateScope extends Activity {

	public CompensateScope(String name) {
		super(name, COMPENSATE_SCOPE);
	}

	public CompensateScope setTargetActivity(String targetActivity) {
		select();
		openProperties().selectDetails();
		new DefaultCCombo().setSelection(targetActivity);
		save();

		return this;
	}
}
