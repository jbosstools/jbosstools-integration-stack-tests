package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;

/**
 * 
 * @author apodhrad
 *
 */
public class DefaultServiceWizard extends ServiceWizard<DefaultServiceWizard> {

	public DefaultServiceWizard() {
		super("New Service");
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

}
