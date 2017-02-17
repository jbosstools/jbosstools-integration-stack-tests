package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents Runtime Wizard dialog jBPM runtime preference page.
 * 
 * @author dhanak@redhat.com
 *
 */
public class JbpmRuntimeWizard {
	public static final String ADD_RUNTIME_LABEL = "Add Runtime";

	public JbpmRuntimeWizard() {
		super();
		new DefaultShell(ADD_RUNTIME_LABEL);
	}

	public void setName(String name) {
		new LabeledText("Name:").setText(name);
	}

	public void setPath(String path) {
		new LabeledText("Path:").setText(path);
	}

	public void ok() {
		new DefaultShell(ADD_RUNTIME_LABEL);
		new PushButton("OK").click();
		new WaitWhile((new ShellWithTextIsAvailable(ADD_RUNTIME_LABEL)));
	}
}
