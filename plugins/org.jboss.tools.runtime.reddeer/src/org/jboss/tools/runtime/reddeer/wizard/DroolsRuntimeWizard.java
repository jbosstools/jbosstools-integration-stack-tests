package org.jboss.tools.runtime.reddeer.wizard;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;

public class DroolsRuntimeWizard {

	public static final String DROOLS_LABEL = "Add Runtime";

	public DroolsRuntimeWizard() {
		super();
		new DefaultShell(DROOLS_LABEL);
	}

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public void setPath(String path) {
		new LabeledText("Path:").setText(path);
	}

	public void ok() {
		new DefaultShell(DROOLS_LABEL);
		new PushButton("OK").click();
		new WaitWhile((new ShellIsAvailable(DROOLS_LABEL)));
	}
}
