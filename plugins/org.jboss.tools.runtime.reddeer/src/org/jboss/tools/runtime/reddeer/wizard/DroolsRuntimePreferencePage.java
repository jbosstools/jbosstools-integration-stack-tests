package org.jboss.tools.runtime.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

public class DroolsRuntimePreferencePage extends WorkbenchPreferencePage {
	
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
}
