package org.jboss.tools.switchyard.reddeer.shell;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.switchyard.reddeer.condition.IsButtonEnabled;

/**
 * 
 * @author apodhrad
 * 
 */
public class DomainPropertyShell {

	public static final String TITLE = "Domain Property";
	public static final String NAME = "Name*";
	public static final String VALUE = "Value*";

	public DomainPropertyShell() {
		new DefaultShell(TITLE);
	}

	public void setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		new LabeledText(VALUE).setFocus();
	}

	public void setValue(String value) {
		new LabeledText(VALUE).setFocus();
		new LabeledText(VALUE).setText(value);
		new LabeledText(NAME).setFocus();
	}

	public void ok() {
		new WaitUntil(new IsButtonEnabled("OK", NAME, VALUE));
		new PushButton("OK").click();
		waitForFinish();
	}

	public void cancel() {
		new PushButton("Cancel").click();
		waitForFinish();
	}

	protected void waitForFinish() {
		new WaitWhile(new ShellIsAvailable(TITLE));
		new WaitWhile(new JobIsRunning());
	}

}
