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
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class DriverDefinitionPreferencePageExt extends DriverDefinitionPreferencePage {

	private static final String GENERIC_JDBC = "Generic JDBC";
	
	@Override
	public void open() {
		/*if (isRunningOnMacOs()) {
			new SWTWorkbenchBot().shells()[0].pressShortcut(SWT.COMMAND, ',');
		}*/
		super.open();
	}

	public void addDriverDefinition(DriverDefinition driverDefinition) {
		new PushButton("Add...").click();
		new DriverDefinitionWizardExt(driverDefinition).execute();
		new PushButton("OK").click();
	}

	private static boolean isRunningOnMacOs() {
		return Platform.getOS().equalsIgnoreCase("macosx");
	}

	private class DriverDefinitionWizardExt extends DriverDefinitionWizard {

		private DriverDefinition driverDefinition;

		public DriverDefinitionWizardExt(DriverDefinition driverDefinition) {
			this.driverDefinition = driverDefinition;
		}

		public void execute() {
			DriverTemplate drvTemp = driverDefinition.getDriverTemplate();
			
			if (! drvTemp.getType().contains(GENERIC_JDBC)){
				DriverDefinitionPage page = getFirstPage();
				page.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());//problem if there is no version of generic driver ?
				page.setName(driverDefinition.getDriverName());
				page.addDriverLibrary(driverDefinition.getDriverLibrary());//firstly clears all suggested jars, but for Generic the list is empty
			} else {
				DriverDefinitionPageExt pageExt = new DriverDefinitionPageExt(getFirstPage().getWizardDialog(), 0);
				pageExt.selectDriverTemplate(drvTemp.getType(), drvTemp.getVersion());//problem if there is no version of generic driver
				pageExt.setName(driverDefinition.getDriverName());
				pageExt.addDriverLibrary(driverDefinition.getDriverLibrary());//firstly clears all suggested jars, but for Generic the list is empty
				pageExt.setDriverClass(driverDefinition.getDriverClass());
			}
		}
	}
	
	
	private class DriverDefinitionPageExt extends DriverDefinitionPage {

		public DriverDefinitionPageExt() {
			super(null, -1);
		}

		public DriverDefinitionPageExt(WizardDialog wizardDialog, int pageIndex) {
			super(wizardDialog, pageIndex);
		}

		@Override
		public void setDriverClass(String driverClass) {
			selectTab(TAB_PROPERTIES);
			new DefaultTreeItem(0, "General", "Driver Class").doubleClick();//kepler 0, juno 1
			new PushButton("...").click();
			new DefaultText().setText(driverClass);
			new PushButton("OK").click();
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

	}
}
