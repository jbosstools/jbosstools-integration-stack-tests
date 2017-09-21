package org.jboss.tools.teiid.reddeer.preference;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.eclipse.datatools.connectivity.ui.dialogs.DriverDialog;
import org.eclipse.reddeer.eclipse.datatools.connectivity.ui.preferences.DriverPreferences;
import org.eclipse.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.list.DefaultList;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.DefaultText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.teiid.reddeer.extensions.DriverDefinitionExt;

public class DriverDefinitionPreferencePageExt extends DriverPreferences {

	private static final String GENERIC_JDBC = "Generic JDBC";
	public static final CharSequence OTHER = "Other";

	public DriverDefinitionPreferencePageExt(ReferencedComposite ref) {
		super(ref);
	}

	public void open() {
		try {
			WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
			preferences.open();
			preferences.select(this);
		} catch (Exception e) {
			new DefaultTreeItem("Data Management").collapse();
			new DefaultTreeItem("Data Management", "Connectivity", "Driver Definitions").expand();
			new DefaultTreeItem("Data Management", "Connectivity", "Driver Definitions").select();
		}
	}

	// public void addDriverDefinition(DriverDefinition driverDefinition) {
	public void addDriverDefinition(DriverDefinitionExt driverDefinition) {
		new PushButton("Add...").click();
		new DefaultShell("New Driver Definition");
		new DriverDefinitionWizardExt(driverDefinition).execute();
		new DefaultShell("New Driver Definition");
		new PushButton("OK").click();
	}

	public void ok() {
		String title = new DefaultShell().getText();
		new OkButton().click();
		new WaitWhile(new ShellIsAvailable(title));
	}

	private class DriverDefinitionWizardExt extends DriverDialog {

		// private DriverDefinition driverDefinition;
		private DriverDefinitionExt driverDefinition;

		// public DriverDefinitionWizardExt(DriverDefinition driverDefinition) {
		public DriverDefinitionWizardExt(DriverDefinitionExt driverDefinition) {
			this.driverDefinition = driverDefinition;
		}

		public void execute() {

			DriverTemplate drvTemp = driverDefinition.getDriverTemplate();

			if (drvTemp.getType().contains(GENERIC_JDBC)) { // e.g. HSQL with Generic driver and with Generic CP

				DriverDefinitionPageExt pageExt = new DriverDefinitionPageExt();
				pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());// !!!! if at least database
																						// matches and others are empty,
																						// select it
				pageExt.setName(driverDefinition.getDriverName());
				pageExt.addDriverLibrary(driverDefinition.getDriverLibrary());
				pageExt.setDriverClassGeneric(driverDefinition.getDriverClass());
				return;
			}

			if (drvTemp.getType().contains(OTHER)) { // e.g. Sybase jtds

				DriverDefinitionPageExt pageExt = new DriverDefinitionPageExt();
				pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion(),
						driverDefinition.getVendorTemplate());
				pageExt.setName(driverDefinition.getDriverName());
				pageExt.addDriverLibrary(driverDefinition.getDriverLibrary());
				pageExt.setDriverClassOther(driverDefinition.getDriverClass());
				pageExt.setConnectionUrlOther(driverDefinition.getConnectionUrl());
				pageExt.setDatabaseName(driverDefinition.getDatabaseName());
				new DefaultShell("New Driver Definition").setFocus();
				return;
			}

			else {
				// normal jdbc (e.g. HSQL with HSQL CP, Sybase jconn3)
				DriverDialog page = new DriverDialog();
				page.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());
				page.setName(driverDefinition.getDriverName());
				page.addDriverLibrary(driverDefinition.getDriverLibrary());// firstly clears all suggested jars, but for
																			// Generic the list is empty
			}
		}

	}

	private class DriverDefinitionPageExt extends DriverDialog {

		private static final String DATABASE_NAME = "Database Name";
		private static final String CONNECTION_URL = "Connection URL";
		private static final String GENERAL = "General";
		private static final String DRIVER_CLASS = "Driver Class";

		public void setDatabaseName(String databaseName) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(new DefaultTree(0), GENERAL, DATABASE_NAME).doubleClick();
			new DefaultText().setText(databaseName);
		}

		public void setConnectionUrlOther(String connectionUrl) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(new DefaultTree(0), GENERAL, CONNECTION_URL).doubleClick();
			new DefaultText().setText(connectionUrl);
		}

		public void selectDriverTemplate(String type, String version, String vendorTemplate) {
			selectTab(TAB_NAME_TYPE);
			Tree tree = new DefaultTree();
			// Database
			TreeItem root = tree.getItems().get(0);
			for (TreeItem item : root.getItems()) {
				if (type.equals(item.getCell(0)) && version.equals(item.getCell(2))
						&& vendorTemplate.equals(item.getCell(1))) {
					item.select();
					break;
				}
			}
		}

		public void setDriverClassGeneric(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(new DefaultTree(0), GENERAL, DRIVER_CLASS).doubleClick();// kepler 0, juno 1
			new PushButton("...").click();
			new DefaultShell("Available Classes from Jar List");
			new DefaultText().setText(driverClass);
			new PushButton("OK").click();
		}

		public void setDriverClassOther(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(new DefaultTree(0), GENERAL, DRIVER_CLASS).doubleClick();
			new DefaultText().setText(driverClass);
		}

		@Override
		public void addDriverLibrary(String driverLocation) {
			selectTab(TAB_JAR_LIST);
			addItem(driverLocation);
			addItem(driverLocation);
			removeDriverLibrary(driverLocation);
		}

		private void addItem(final String item) {
			Display.syncExec(new Runnable() {
				@Override
				public void run() {
					new DefaultList().getSWTWidget().add(item);
				}
			});
		}

		@Override
		public void selectDriverTemplate(String type, String version) {
			selectTab(TAB_NAME_TYPE);
			Tree tree = new DefaultTree();
			// Database
			TreeItem root = tree.getItems().get(0);
			for (TreeItem item : root.getItems()) {
				if (type.equals(item.getCell(0)) && version.equals(item.getCell(2))) {
					item.select();
					return;
				}
			}
			for (TreeItem item : root.getItems()) {// if matches at least the type
				if (type.equals(item.getCell(0))) {
					item.select();
					return;
				}
			}
		}

	}
}
