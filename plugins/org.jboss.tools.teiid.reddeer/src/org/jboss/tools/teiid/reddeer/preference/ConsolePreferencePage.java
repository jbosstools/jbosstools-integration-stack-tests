package org.jboss.tools.teiid.reddeer.preference;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.jface.preference.PreferencePage;

public class ConsolePreferencePage extends PreferencePage {

	public ConsolePreferencePage() {
		super("Run/Debug", "Console");
	}

	public void toggleShowWhenWriteToStdOut(boolean toggle) {
		open();
		new CheckBox("Show when program writes to standard out").toggle(toggle);
		close();

	}

	public void toggleShowWhenWriteToStdErr(boolean toggle) {
		open();
		new CheckBox("Show when program writes to standard error").toggle(toggle);
		close();
	}

	private void open() {
		WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
		preferences.open();
		preferences.select(this);
	}

	private void close() {
		new WorkbenchPreferenceDialog().ok();
	}

}
