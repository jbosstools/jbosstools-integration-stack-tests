package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.eclipse.reddeer.common.exception.WaitTimeoutExpiredException;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.selectionwizard.ImportMenuWizard;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Abstract wizard for importing relational models from various sources
 * 
 * @author apodhrad
 * 
 */
public abstract class TeiidImportWizard extends ImportMenuWizard {

	public TeiidImportWizard(String dialogTitle, String importer) {
		super(dialogTitle, "Teiid Designer", importer);
	}

	@Override
	public void finish() {
		// super.finish(); fail MAC jenkins
		new PushButton("Finish").click(); 
		// wait for 'Progress Information'
		log.info("Progress waiting started ...");
		try {
			new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		} catch (WaitTimeoutExpiredException ex) {
			new PushButton("Cancel").click();
			throw ex;
		}
		log.info("Progress waiting stopped.");
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		log.info("No running job");
		try {
			new ShellMenuItem(new WorkbenchShell(), "File", "Save All").select();
			log.info("All files saved.");
		} catch (Exception e) {
			log.info("There is nothing to save.");
		}
	}

	public abstract TeiidImportWizard nextPage();
	
}
