package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.swt.condition.ShellIsActive;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.switchyard.reddeer.condition.TableHasRow;

/**
 * 
 * @author apodhrad
 * 
 */
public abstract class ExistingServiceWizard<T extends ExistingServiceWizard<?>> extends WizardDialog {

	private String dialogTitle;

//	public ExistingServiceWizard() {
//		super();
//	}

	public ExistingServiceWizard(String dialogTitle) {
		super(dialogTitle);
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
		new WaitWhile(new ShellIsActive(getSelectionDialogTitle()));
		return activate();
	}

	@Override
	public void finish() {
		super.finish(TimePeriod.VERY_LONG);
	}

	protected abstract String getSelectionDialogTitle();

}
