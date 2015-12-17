package org.jboss.tools.teiid.reddeer;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;
import org.jboss.tools.teiid.reddeer.view.GuidesView;
import org.jboss.tools.teiid.reddeer.wizard.GenerateDynamicVdbWizard;
import org.jboss.tools.teiid.reddeer.wizard.GenerateVdbArchiveWizard;

/**
 * This class represents a virtual database.
 * 
 * @author apodhrad
 * 
 */
public class VDB {
	
	private static final String GENERATE_VDB_ARCHIVE = "Generate VDB Archive and Models";
	private static final String GENERATE_DYNAMIC_VDB = "Generate Dynamic VDB";

	private ProjectItem projectItem;
	
	public VDB(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

	/**
	 * Deployes this VDB
	 */
	public void deployVDB() {
		new WorkbenchShell();
		projectItem.select();
		new ContextMenu("Modeling", "Deploy").select();

		try {
			new WaitUntil(new ShellWithTextIsAvailable("Create VDB Data Source"), TimePeriod.NORMAL);
			new DefaultShell("Create VDB Data Source");
			new PushButton("Create Source").click();
		} catch (Exception e) {
		}

		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	/**
	 * Executes this VDB
	 */
	public void executeVDB() {
		new WorkbenchShell();
		projectItem.select();
		new ContextMenu("Modeling", "Execute VDB").select();
		new WaitWhile(new IsInProgress(), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	/**
	 * Executes this VDB via Modelling actions guides
	 * @param viaGuides
	 */
	public void executeVDB(boolean viaGuides){
		if (viaGuides){
			projectItem.select();
			new GuidesView().chooseAction("Model JDBC Source", "Execute VDB");
			new DefaultShell("Execute VDB");
			//VDB should be selected from previous step, if not:
			if (! new DefaultText(0).getText().isEmpty()){
				new SWTWorkbenchBot().button("OK").click();
			} else {
				new PushButton("Cancel").click();
				executeVDB();
			}
			
		} else {
			executeVDB();
		}
	}
	
	public GenerateDynamicVdbWizard generateDynamicVDB(){
		projectItem.select();
		new ContextMenu("Modeling", GENERATE_DYNAMIC_VDB).select();
		return new GenerateDynamicVdbWizard().activate();
	}
	
	public GenerateVdbArchiveWizard generateVdbArchive(){
		projectItem.select();
		new ContextMenu("Modeling", GENERATE_VDB_ARCHIVE).select();
		return new GenerateVdbArchiveWizard().activate();
	}
}
