package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author apodhrad
 *
 */
public class BPMNServiceWizard extends ServiceWizard<BPMNServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	public BPMNServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

}
