package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.jboss.reddeer.swt.impl.button.PushButton;

public class Dialog {

	public void ok() {
		new PushButton("OK").click();
	}

	public void cancel() {
		new PushButton("Cancel").click();
	}
}
