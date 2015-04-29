package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * Represents <i>Console</i> preference page
 * 
 * @author tsedmik
 */
public class ConsolePreferencePage extends WorkbenchPreferencePage {

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
}