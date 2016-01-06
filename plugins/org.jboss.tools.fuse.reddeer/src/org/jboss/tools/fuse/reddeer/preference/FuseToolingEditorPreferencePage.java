package org.jboss.tools.fuse.reddeer.preference;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.jface.preference.PreferencePage;

/**
 * Represents the "Fuse Tooling --> Editor" preference page
 * 
 * @author tsedmik
 */
public class FuseToolingEditorPreferencePage extends PreferencePage {

	public FuseToolingEditorPreferencePage() {
		super("Fuse Tooling", "Editor");
	}

	/**
	 * Sets checkbox "If enabled the ID values will be used for labels if existing"
	 * 
	 * @param value
	 *            true - check the checkbox, false - uncheck the checkbox
	 */
	public void setShowIDinEditor(boolean value) {

		new CheckBox("If enabled the ID values will be used for labels if existing").toggle(value);
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
