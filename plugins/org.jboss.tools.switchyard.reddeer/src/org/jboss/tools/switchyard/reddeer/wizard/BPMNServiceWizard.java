package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.swt.impl.button.PushButton;

/**
 * 
 * @author apodhrad
 *
 */
public class BPMNServiceWizard extends ServiceWizard<BPMNServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	public BPMNServiceWizard() {
		super(DIALOG_TITLE);
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}

}
