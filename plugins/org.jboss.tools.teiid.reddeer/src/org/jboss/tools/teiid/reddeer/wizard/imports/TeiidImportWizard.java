package org.jboss.tools.teiid.reddeer.wizard.imports;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Abstract wizard for importing relational models from various sources
 * 
 * @author apodhrad
 * 
 */
public abstract class TeiidImportWizard extends ImportWizardDialog {

	public TeiidImportWizard(String importer) {
		super("Teiid Designer", importer);
	}

	@Override
	public void finish() {
		super.finish();
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
			new ShellMenu("File", "Save All").select();
			log.info("All files saved.");
		} catch (Exception e) {
			log.info("There is nothing to save.");
		}
	}
	
	/**
	 * use nextPage()
	 */
	@Deprecated
	@Override
	public void next(){
		super.next();
	}
	
	public abstract TeiidImportWizard nextPage();
	
}
