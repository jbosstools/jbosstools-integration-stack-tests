package org.jboss.tools.runtime.reddeer.preference;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
import org.jboss.tools.runtime.reddeer.RuntimeEntry;
import org.jboss.tools.runtime.reddeer.wizard.DownloadRuntimesWizard;

/**
 * Represents preference page <i>JBoss Runtime Detection</i>.
 * 
 * @author tsedmik
 */
public class JBossRuntimeDetection extends WorkbenchPreferencePage {

	public JBossRuntimeDetection() {
		super("JBoss Tools", "JBoss Runtime Detection");
	}

	public DownloadRuntimesWizard downloadRuntime() {
		new PushButton("Download...").click();
		new WaitUntil(new ShellWithTextIsAvailable("Download Runtimes"));
		new DefaultShell("Download Runtimes");
		return new DownloadRuntimesWizard();
	}

	public int getRuntimesCount() {
		return new DefaultTable(0).rowCount();
	}

	public void removeAllRuntimes() {
		for (TableItem item : new DefaultTable(0).getItems()) {
			item.select();
			new PushButton("Remove").click();
		}
	}

	public void editFirstPath(String path) {
		new DefaultTable(0).getItem(0).select();
		new PushButton("Edit...").click();
		new DefaultShell("Edit runtime detection path");
		new DefaultText(0).setText(path);
		new PushButton("OK").click();
	}

	public List<RuntimeEntry> searchRuntimes() {
		new PushButton("Search...").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new DefaultShell("Searching for runtimes...");
		List<RuntimeEntry> entries = new ArrayList<RuntimeEntry>();
		for (TreeItem item : new DefaultTree(0).getItems()) {
			RuntimeEntry entry = new RuntimeEntry();
			entry.setName(item.getCell(0));
			entry.setType(item.getCell(1));
			entry.setVersion(item.getCell(2));
			entry.setLocation(item.getCell(3));
			entries.add(entry);
		}
		new PushButton("Cancel").click();
		return entries;
	}

	public void open() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		dialog.select(this);
	}
}
