package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;

public class DroolsServiceWizard extends ServiceWizard<DroolsServiceWizard> {

	public static final String DIALOG_TITLE = "New File";

	public DroolsServiceWizard() {
		super(DIALOG_TITLE);
	}

	@Override
	protected void browse() {
		new PushButton("Browse...").click();
	}
}
