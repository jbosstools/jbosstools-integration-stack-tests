package org.jboss.tools.switchyard.reddeer.preference;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class CompositePropertiesPage extends PreferencePage {

	public CompositePropertiesPage activate() {
		new DefaultShell("Properties for ");
		return this;
	}
	
	public ValidatorsPage selectValidators() {
		activate();
		new DefaultTreeItem("Validators").select();
		return new ValidatorsPage();
	}

	@Override
	public void ok() {
		activate();
		super.ok();
	}

}
