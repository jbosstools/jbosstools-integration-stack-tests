package org.jboss.tools.esb.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.esb.reddeer.widget.LabeledComboExt;
import org.jboss.tools.esb.reddeer.widget.LabeledTextExt;

/**
 * 
 * @author apodhrad
 * 
 */
public class ESBWizard extends WizardDialog {

	public static final String NAME = "Name:*";

	private String title;

	public ESBWizard(String title) {
		super();
		this.title = title;
		new DefaultShell(title);
	}

	public void setName(String name) {
		new LabeledTextExt(NAME).setText(name);
	}

	public void setText(String label, String text) {
		new LabeledTextExt(label).setText(text);
	}

	public void getText(String label) {
		new LabeledTextExt(label).getText();
	}

	public void setCombo(String label, String text) {
		new LabeledComboExt(label).setSelection(text);
	}

	public void ok() {
		new DefaultShell(title);
		Button button = new PushButton("OK");
		new WaitWhile(new JobIsRunning());
		button.click();

		new WaitWhile(new ShellWithTextIsAvailable(title), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}
