package org.jboss.tools.teiid.reddeer.preference;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.jboss.reddeer.eclipse.datatools.ui.DriverDefinition;
import org.jboss.reddeer.eclipse.datatools.ui.DriverTemplate;
import org.jboss.reddeer.eclipse.datatools.ui.preference.DriverDefinitionPreferencePage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionPage;
import org.jboss.reddeer.eclipse.datatools.ui.wizard.DriverDefinitionWizard;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.teiid.reddeer.extensions.DriverDefinitionExt;

public class DriverDefinitionPreferencePageExt extends DriverDefinitionPreferencePage {

	private static final String GENERIC_JDBC = "Generic JDBC";
	public static final CharSequence OTHER = "Other";
	
	@Override
	public void open() {
		/*if (isRunningOnMacOs()) {
			new SWTWorkbenchBot().shells()[0].pressShortcut(SWT.COMMAND, ',');
		}*/
		super.open();
	}

	//public void addDriverDefinition(DriverDefinition driverDefinition) {
	public void addDriverDefinition(DriverDefinitionExt driverDefinition) {
		new PushButton("Add...").click();
		new DriverDefinitionWizardExt(driverDefinition).execute();
		new PushButton("OK").click();
	}

	private static boolean isRunningOnMacOs() {
		return Platform.getOS().equalsIgnoreCase("macosx");
	}

	private class DriverDefinitionWizardExt extends DriverDefinitionWizard {

		//private DriverDefinition driverDefinition;
		private DriverDefinitionExt driverDefinition;

		//public DriverDefinitionWizardExt(DriverDefinition driverDefinition) {
		public DriverDefinitionWizardExt(DriverDefinitionExt driverDefinition) {
			this.driverDefinition = driverDefinition;
		}

		public void execute() {
			DriverTemplate drvTemp = driverDefinition.getDriverTemplate();
			
			/*if (! drvTemp.getType().contains(GENERIC_JDBC)){
				DriverDefinitionPage page = getFirstPage();
				page.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());
				page.setName(driverDefinition.getDriverName());
				page.addDriverLibrary(driverDefinition.getDriverLibrary());//firstly clears all suggested jars, but for Generic the list is empty
			} 
			else {//or Other driver
				DriverDefinitionPageExt pageExt = new DriverDefinitionPageExt(getFirstPage().getWizardDialog(), 0);
				if (drvTemp.getType().equals("Other")){
					//pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion(), driverDefinition.getVendorTemplate());
					
				} else {
					pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());
				}
				
				
				pageExt.setName(driverDefinition.getDriverName());
				pageExt.addDriverLibrary(driverDefinition.getDriverLibrary());//firstly clears all suggested jars, but for Generic the list is empty
				pageExt.setDriverClass(driverDefinition.getDriverClass());
			}*/
			
			if (drvTemp.getType().contains(GENERIC_JDBC)){//e.g. HSQL with Generic driver and with Generic CP 
				DriverDefinitionPageExt pageExt = new DriverDefinitionPageExt(getFirstPage().getWizardDialog(), 0);
				pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());
				pageExt.setName(driverDefinition.getDriverName());
				pageExt.addDriverLibrary(driverDefinition.getDriverLibrary());
				pageExt.setDriverClassGeneric(driverDefinition.getDriverClass());
				return;
			} 
			
			if (drvTemp.getType().contains(OTHER)){//e.g. Sybase jtds
				DriverDefinitionPageExt pageExt = new DriverDefinitionPageExt(getFirstPage().getWizardDialog(), 0);
				pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion(), driverDefinition.getVendorTemplate());
				pageExt.setName(driverDefinition.getDriverName());
				pageExt.addDriverLibrary(driverDefinition.getDriverLibrary());
				pageExt.setDriverClassOther(driverDefinition.getDriverClass());
				pageExt.setConnectionUrlOther(driverDefinition.getConnectionUrl());
				pageExt.setDatabaseName(driverDefinition.getDatabaseName());
				new DefaultShell("New Driver Definition").setFocus();
				return;
			} 
			
			else {
				//normal jdbc (e.g. HSQL with HSQL CP, Sybase jconn3)
				DriverDefinitionPage page = getFirstPage();
				page.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());
				page.setName(driverDefinition.getDriverName());
				page.addDriverLibrary(driverDefinition.getDriverLibrary());//firstly clears all suggested jars, but for Generic the list is empty
			}
		}
		
	}
	
	private class DriverDefinitionPageExt extends DriverDefinitionPage {

		private static final String DATABASE_NAME = "Database Name";
		private static final String CONNECTION_URL = "Connection URL";
		private static final String GENERAL = "General";
		private static final String DRIVER_CLASS = "Driver Class";
		
		public DriverDefinitionPageExt() {
			super(null, -1);
		}

		public void setDatabaseName(String databaseName) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(0, GENERAL, DATABASE_NAME).doubleClick();
			new DefaultText().setText(databaseName);
		}

		public void setConnectionUrlOther(String connectionUrl) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(0, GENERAL, CONNECTION_URL).doubleClick();
			new DefaultText().setText(connectionUrl);
		}

		public void selectDriverTemplate(String type, String version,
				String vendorTemplate) {
			selectTab(TAB_NAME_TYPE);
			Tree tree = new DefaultTree();
			// Database
			TreeItem root = tree.getItems().get(0);
			for (TreeItem item : root.getItems()) {
				if (type.equals(item.getCell(0)) && version.equals(item.getCell(2)) && vendorTemplate.equals(item.getCell(1))) {
					item.select();
					break;
				}
			}
		}

		public DriverDefinitionPageExt(WizardDialog wizardDialog, int pageIndex) {
			super(wizardDialog, pageIndex);
		}

		public void setDriverClassGeneric(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(0, GENERAL, DRIVER_CLASS).doubleClick();//kepler 0, juno 1
			new PushButton("...").click();
			new DefaultText().setText(driverClass);
			new PushButton("OK").click();
		}
		
		public void setDriverClassOther(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(0, GENERAL, DRIVER_CLASS).doubleClick();
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
			syncExec(new VoidResult() {

				@Override
				public void run() {
					new SWTWorkbenchBot().list().widget.add(item);
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
					break;
				}
			}
		}

	}
}
