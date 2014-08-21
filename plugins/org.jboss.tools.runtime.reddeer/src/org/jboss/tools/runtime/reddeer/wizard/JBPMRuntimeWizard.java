package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 *
 */
public class JBPMRuntimeWizard {

	public JBPMRuntimeWizard() {
		super();
		new DefaultShell("Add Location");
	}

	public void setName(String name) {
		new LabeledText("Name :").setText(name);
	}

	public void setLocation(String location) {
		new LabeledText("Location :").setText(location);
	}

	public void ok() {
		new DefaultShell("Add Location");
		new PushButton("OK").click();
		new WaitWhile((new ShellWithTextIsAvailable("Add Location")));
	}
}
