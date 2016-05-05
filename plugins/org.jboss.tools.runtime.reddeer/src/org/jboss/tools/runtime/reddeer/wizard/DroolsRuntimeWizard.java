package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitWhile;

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
		new WaitWhile((new ShellWithTextIsAvailable(DROOLS_LABEL)));
	}
}
