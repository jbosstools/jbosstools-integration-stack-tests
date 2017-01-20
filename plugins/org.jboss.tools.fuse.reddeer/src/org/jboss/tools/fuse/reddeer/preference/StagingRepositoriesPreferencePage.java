package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

/**
 * Represents the "Fuse Tooling --> Staging Repositories" preference page
 * 
 * @author tsedmik
 */
public class StagingRepositoriesPreferencePage extends PreferencePage {

	public StagingRepositoriesPreferencePage() {
		super("Fuse Tooling", "Staging Repositories");
	}

	/**
	 * Switch "Enable Staging Repositories" according to the given value
	 * @param value true - turns on staging repositories, false - turns off staging repositories
	 */
	public void toggleStagingRepositories(boolean value) {
		new CheckBox("Enable Staging Repositories").toggle(value);
	}

	public void open() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(this);
	}

	public void ok() {
		String title = new DefaultShell().getText();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
