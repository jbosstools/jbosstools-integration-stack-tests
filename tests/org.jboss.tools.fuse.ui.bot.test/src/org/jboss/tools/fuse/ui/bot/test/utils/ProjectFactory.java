package org.jboss.tools.fuse.ui.bot.test.utils;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
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
	public static void createProject(String name, String archetype) {

		FuseProjectWizard projectWizard = new FuseProjectWizard();
		projectWizard.open();
		projectWizard.setProjectName(name);
		projectWizard.next();
		projectWizard.setFilter(archetype);
		projectWizard.selectFirstArchetype();
		projectWizard.finish();

		try {
			new WaitUntil(new ShellWithTextIsAvailable("Open Associated Perspective?"), TimePeriod.NORMAL);
			new DefaultShell("Open Associated Perspective?");
			new PushButton("No").click();;
		} catch (Exception ex) {}

		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(300));
		log.info("The Fuse project from archetype: " + archetype + " was created.");
	}
}
