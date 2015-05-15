package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

/**
 * 
 * @author apodhrad
 *
 */
public class BPMServiceWizard extends ServiceWizard<BPMServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	public BPMServiceWizard activate() {
		new DefaultShell(DIALOG_TITLE);
		return this;
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

}
