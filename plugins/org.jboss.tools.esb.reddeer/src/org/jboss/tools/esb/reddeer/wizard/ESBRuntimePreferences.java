package org.jboss.tools.esb.reddeer.wizard;

import java.util.List;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * 
 * @author apodhrad
 *
 */
public class ESBRuntimePreferences extends PreferencePage {

	public ESBRuntimePreferences() {
		super("JBoss Tools", "JBoss ESB Runtimes");
	}

	public ESBRuntimeWizard addESBRuntime() {
		new PushButton("Add...").click();
		return new ESBRuntimeWizard();
	}

	public void addESBRuntime(String version, String path) {
		ESBRuntimeWizard wizard = addESBRuntime();
		wizard.setName("esb-" + version);
		wizard.setHomeFolder(path);
		wizard.finish();
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
}
