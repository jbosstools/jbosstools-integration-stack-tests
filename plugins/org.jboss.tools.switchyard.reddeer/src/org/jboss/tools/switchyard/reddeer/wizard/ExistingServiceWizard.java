package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.condition.TableHasRow;

/**
 * 
 * @author apodhrad
 * 
 */
public abstract class ExistingServiceWizard<T extends ExistingServiceWizard<?>> extends WizardDialog {

	private String dialogTitle;

	public ExistingServiceWizard() {
		super();
	}

	public ExistingServiceWizard(String dialogTitle) {
		super();
		this.dialogTitle = dialogTitle;
	}

	@SuppressWarnings("unchecked")
	public T activate() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultShell(dialogTitle).setFocus();
		return (T) this;
	}

	public T selectExistingImplementation(String impl) {
		new DefaultShell("");
		new PushButton("Browse...").click();
		new DefaultShell(getSelectionDialogTitle());
		new DefaultText().setText(impl);
		new WaitUntil(new TableHasRow(new DefaultTable(), new RegexMatcher(".*" + impl + ".*")));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(getSelectionDialogTitle()));
		return activate();
	}

	@Override
	public void finish() {
		super.finish(TimePeriod.VERY_LONG);
	}

	protected abstract String getSelectionDialogTitle();

}
