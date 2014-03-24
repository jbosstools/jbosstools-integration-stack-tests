package org.jboss.tools.fuse.ui.bot.test.utils;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.wizard.FuseProjectWizard;

/**
 * Can create new Fuse projects
 * 
 * @author tsedmik
 */
public class ProjectFactory {
	
	private static Logger log = Logger.getLogger(ProjectFactory.class);
	
	/**
	 * Creates a new Fuse project from given archetype 
	 * 
	 * @param archetype <i>Artifact ID</i> in the <i>New Fuse Project</i> Wizard
	 */
	public static void createProject(String archetype) {
		
		FuseProjectWizard projectWizard = new FuseProjectWizard();
		projectWizard.open();
		projectWizard.next();
		projectWizard.setFilter(archetype);
		projectWizard.selectFirstArchetype();
		projectWizard.finish();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);	
		log.info("The Fuse project from archetype: " + archetype + " was created.");
		
	}

}
