package org.jboss.tools.runtime.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

public class DroolsRuntimePreferencePage extends PreferencePage {

	public DroolsRuntimePreferencePage() {
		super("Drools", "Installed Drools Runtimes");
	}

	public DroolsRuntimeWizard addRuntime() {
		new PushButton("Add...").click();
		return new DroolsRuntimeWizard();
	}

	public List<String> getDroolsRuntimes() {
		List<String> droolsRuntimes = new ArrayList<String>();
		List<TableItem> items = new DefaultTable().getItems();
		for (TableItem item : items) {
			droolsRuntimes.add(item.getText(0));
		}
		return droolsRuntimes;
	}

	public void open() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(this);
	}

	public void setDroolsRuntimeAsDefault(String name) {
		for (TableItem item : new DefaultTable().getItems()) {
			if (item.getText(0).equals(name)) {
				item.setChecked(true);
			}
		}
	}

	public void ok() {
		String title = new DefaultShell().getText();
		new PushButton("Apply and Close").click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
