package org.jboss.tools.runtime.reddeer.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * 
 * @author apodhrad
 * 
 */
public class ESBRuntimePreferencePage extends WorkbenchPreferencePage {

	public ESBRuntimePreferencePage() {
		super("JBoss Tools", "JBoss ESB Runtimes");
	}

	public ESBRuntimeWizard addESBRuntime() {
		new PushButton("Add...").click();
		return new ESBRuntimeWizard();
	}

	public void removeESBRuntime(String name) {
		Table table = new DefaultTable();
		List<TableItem> items = table.getItems();
		boolean runtimeFound = false;
		for (TableItem item : items) {
			String runtime = item.getText(1);
			if (runtime.equals(name)) {
				item.select();
				runtimeFound = true;
				break;
			}
		}

		if (!runtimeFound) {
			throw new RuntimeException("Cannot find ESB runtime '" + name + "'");
		}

		new PushButton("Remove").click();
		new DefaultShell("Confirm Runtime Delete");
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsAvailable("Confirm Runtime Delete"));
	}

	public List<String> getESBRuntimes() {
		List<String> esbRuntimes = new ArrayList<String>();
		List<TableItem> items = new DefaultTable().getItems();
		for (TableItem item : items) {
			esbRuntimes.add(item.getText(1));
		}
		return esbRuntimes;
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
