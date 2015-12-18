package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.jface.preference.PreferencePage;

/**
 * Represents <i>Console</i> preference page
 * 
 * @author tsedmik
 */
public class ConsolePreferencePage extends PreferencePage {

	public ConsolePreferencePage() {
		super("Run/Debug", "Console");
	}

	public void toggleShowConsoleStandardWrite(boolean checked) {
		new CheckBox(2).toggle(checked);
	}

	public void toggleShowConsoleErrorWrite(boolean checked) {
		new CheckBox(3).toggle(checked);
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