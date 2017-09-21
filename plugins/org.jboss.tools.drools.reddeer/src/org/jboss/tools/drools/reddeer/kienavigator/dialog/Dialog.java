package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.eclipse.reddeer.swt.impl.button.PushButton;

public class Dialog {

	public void ok() {
		new PushButton("Apply and Close").click();
	}

	public void cancel() {
		new PushButton("Cancel").click();
	}
}
