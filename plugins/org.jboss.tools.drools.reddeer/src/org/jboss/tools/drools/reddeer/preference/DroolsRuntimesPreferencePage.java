package org.jboss.tools.drools.reddeer.preference;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ProgressInformationShellIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.drools.reddeer.dialog.DroolsRuntimeDialog;

public class DroolsRuntimesPreferencePage extends PreferencePage {
	private static final Logger LOGGER = Logger.getLogger(DroolsRuntimesPreferencePage.class);

	public DroolsRuntimesPreferencePage() {
		super("Drools", "Installed Drools Runtimes");
	}

	public DroolsRuntimeDialog addDroolsRuntime() {
		new PushButton("Add...").click();
		return new DroolsRuntimeDialog();
	}

	public DroolsRuntimeDialog editDroolsRuntime(String name) {
		selectDroolsRuntime(name);
		new PushButton("Edit...").click();
		return new DroolsRuntimeDialog();
	}

	public void removeDroolsRuntime(String name) {
		selectDroolsRuntime(name);
		new PushButton("Remove").click();
	}

	public void setDroolsRuntimeAsDefault(String name) {
		for (TableItem item : new DefaultTable().getItems()) {
			if (item.getText(0).equals(name)) {
				item.setChecked(true);
			}
		}
	}

	public DroolsRuntime getDefaultDroolsRuntime() {
		for (DroolsRuntime r : getDroolsRuntimes()) {
			if (r.isDefault()) {
				return r;
			}
		}

		return null;
	}

	public Collection<DroolsRuntime> getDroolsRuntimes() {
		Collection<DroolsRuntime> result = new ArrayList<DroolsRuntimesPreferencePage.DroolsRuntime>();
		String name, version, location;
		boolean isDefault;
		for (TableItem item : new DefaultTable().getItems()) {
			name = item.getText(0);
			version = item.getText(1);
			location = item.getText(2);
			isDefault = item.isChecked();
			result.add(new DroolsRuntime(name, version, location, isDefault));
		}

		return result;
	}

	public static class DroolsRuntime {
		private final String name;
		private final String version;
		private final String location;
		private final boolean isDefault;

		public DroolsRuntime(String name, String version, String location, boolean isDefault) {
			this.name = name;
			this.version = version;
			this.location = location;
			this.isDefault = isDefault;
		}

		public String getName() {
			return name;
		}
		
		public String getVersion() {
			return version;
		}
		
		public String getLocation() {
			return location;
		}

		public boolean isDefault() {
			return isDefault;
		}
	}

	public boolean okCloseWarning() {
		String shellText = new DefaultShell().getText();
		Button b = new PushButton("Apply and Close");
		LOGGER.info("Close Preferences dialog");
		b.click();

		boolean warning;
		try {
			new WaitUntil(new ShellWithTextIsActive("Warning"), TimePeriod.SHORT);
			new PushButton("OK").click();
			warning = true;
		} catch (Exception ex) {
			LOGGER.info("Default Drools runtime changed warning not shown.");
			warning = false;
		}

		new WaitWhile(new ProgressInformationShellIsActive());
		new WaitWhile(new ShellWithTextIsAvailable(shellText));
		return warning;
	}

	private void selectDroolsRuntime(String name) {
		Table t = new DefaultTable();
		for (int i = 0; i < t.rowCount(); i++) {
			if (t.getItem(i).getText(0).equals(name)) {
				t.select(i);
				break;
			}
		}
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

	public void cancel() {
		String title = new DefaultShell().getText();
		new CancelButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(title));
	}
}
