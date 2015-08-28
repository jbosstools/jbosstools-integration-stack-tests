package org.jboss.tools.switchyard.reddeer.shell;

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
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
		new WaitUntil(new IsButtonEnabled("OK",	NAME, VALUE));
		new PushButton("OK").click();
		waitForFinish();
	}

	public void cancel() {
		new PushButton("Cancel").click();
		waitForFinish();
	}

	protected void waitForFinish() {
		new WaitWhile(new ShellWithTextIsAvailable(TITLE));
		new WaitWhile(new JobIsRunning());
	}

}
