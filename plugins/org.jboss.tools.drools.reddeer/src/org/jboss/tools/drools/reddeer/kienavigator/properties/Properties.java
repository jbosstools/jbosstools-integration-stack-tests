package org.jboss.tools.drools.reddeer.kienavigator.properties;

import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.drools.reddeer.kienavigator.dialog.Dialog;

public abstract class Properties extends Dialog {

	public void restoreDefaults() {
		new PushButton("Restore Defaults").click();
	}

	public void apply() {
		new PushButton("Apply").click();
	}
}
