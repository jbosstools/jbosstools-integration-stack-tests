package org.jboss.tools.esb.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 * 
 */
public class ESBActionWizard extends WizardDialog {

	public static final String NAME = "Name:*";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();
	private String title;

	public ESBActionWizard(String title) {
		super();
		this.title = title;
		new DefaultShell(title);
	}

	public void setName(String name) {
		bot.textWithLabel(NAME).setText(name);
	}

	public void setText(String label, String text) {
		bot.textWithLabel(label).setText(text);
	}

	public void getText(String label) {
		bot.textWithLabel(label).getText();
	}

	public void setCombo(String label, String text) {
		bot.comboBoxWithLabel(label).setSelection(text);
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
