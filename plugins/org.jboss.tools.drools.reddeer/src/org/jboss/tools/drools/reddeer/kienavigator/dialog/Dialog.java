package org.jboss.tools.drools.reddeer.kienavigator.dialog;

import org.eclipse.reddeer.swt.impl.button.PushButton;

public class Dialog {

	public void ok() {
		// TODO provide a better logic for buttons "OK" and "Click and Apply"
		try {
			new PushButton("OK").click();
		} catch (Exception e) {
			new PushButton("Apply and Close").click();
		}
	}

	public void cancel() {
		new PushButton("Cancel").click();
	}
}
