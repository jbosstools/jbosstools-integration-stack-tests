package org.jboss.tools.runtime.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

/**
 * Represents jBPM runtime preference page.
 * 
 * Window -> Preferences -> jBPM -> Installed jBPM Runtimes
 * 
 * @author dhanak@redhat.com
 *
 */
public class JbpmRuntimePreferencePage extends PreferencePage {
	
	public JbpmRuntimePreferencePage() {
		super("jBPM", "Installed jBPM Runtimes");
	}
	
	public JbpmRuntimeWizard addRuntime() {
		new PushButton("Add...").click();
		return new JbpmRuntimeWizard();
	}

	public List<String> getJbpmRuntimes() {
		List<String> jbpmRuntimes = new ArrayList<String>();
		List<TableItem> items = new DefaultTable().getItems();
		for (TableItem item : items) {
			jbpmRuntimes.add(item.getText(0));
		}
		return jbpmRuntimes;
	}

	public void open() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(this);
	}

	public void setJbpmRuntimeAsDefault(String name) {
		for (TableItem item : new DefaultTable().getItems()) {
			if (item.getText(0).equals(name)) {
				item.setChecked(true);
			}
		}
	}

	public void ok() {
		String title = new DefaultShell().getText();
		new OkButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
