package org.jboss.tools.fuse.ui.bot.test.utils;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.wait.AbstractWait;
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
	 * @throws FuseArchetypeNotFoundException 
	 */
	public static void createProject(String name, String archetype) throws FuseArchetypeNotFoundException {

		FuseProjectWizard projectWizard = new FuseProjectWizard();
		projectWizard.open();
		projectWizard.setProjectName(name);
		projectWizard.next();
		projectWizard.setFilter(archetype);
		try {
			projectWizard.selectFirstArchetype();
		} catch (SWTLayerException e) {
			throw new FuseArchetypeNotFoundException();
		}
		projectWizard.finish();

		try {
			new WaitUntil(new ShellWithTextIsAvailable("Open Associated Perspective?"), TimePeriod.NORMAL);
			new DefaultShell("Open Associated Perspective?");
			new PushButton("No").click();
		} catch (Exception ex) {
		}

		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(300));
		log.info("The Fuse project from archetype: " + archetype + " was created.");

		log.info("Try to fix errors 'not covered by lifecycle'");
		ProblemsView problems = new ProblemsView();
		problems.open();
		List<Problem> errors = problems.getProblems(ProblemType.ERROR, new ProblemsDescriptionMatcher(new RegexMatcher("Plugin execution not covered by lifecycle.*")));
		while (!errors.isEmpty()) {

			fixPluginError();
			problems.activate();
			AbstractWait.sleep(TimePeriod.getCustom(3));
			errors = problems.getProblems(ProblemType.ERROR, new ProblemsDescriptionMatcher(new RegexMatcher("Plugin execution not covered by lifecycle.*")));
		}
	}

	/**
	 * Fixes the first "Plugin execution not covered by lifecycle" error
	 * 
	 * TODO(tsedmik) rework this method with RedDeer 0.8.0 which will contain better manipulation with "Quick Fix"
	 */
	private static void fixPluginError() {

		new ProblemsView();
		List<TreeItem> items = new DefaultTree().getItems();
		for (TreeItem item : items) {
			if (item.getText().toLowerCase().contains("error")) {
				items = item.getItems();
				break;
			}
		}
		for (TreeItem item : items) {
			if (item.getCell(0).contains("Plugin execution not covered by lifecycle")) {
				item.select();
				break;
			}
		}
		new ContextMenu("Quick Fix").select();
		new WaitUntil(new ShellWithTextIsAvailable("Quick Fix"));
		new DefaultShell("Quick Fix");
		new DefaultTable().select(1);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Quick Fix"));
		new WorkbenchShell();
	}
}
