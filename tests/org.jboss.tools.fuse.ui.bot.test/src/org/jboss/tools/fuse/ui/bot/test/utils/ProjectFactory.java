package org.jboss.tools.fuse.ui.bot.test.utils;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsDescriptionMatcher;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.fuse.reddeer.wizard.FuseProjectWizard;

/**
 * Can create new Fuse projects or import existing
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
		} catch (SWTLayerException|CoreLayerException e) {
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
	 * Retrieve list of all available archetypes
	 * 
	 * @return List of artifact ids of available archetypes
	 */
	public static List<String> getArchetypes() {

		FuseProjectWizard projectWizard = new FuseProjectWizard();
		projectWizard.open();
		projectWizard.setProjectName("test");
		projectWizard.next();
		List<String> result = projectWizard.getArchetypes();
		projectWizard.cancel();
		return result;
	}

	/**
	 * Removes all projects from file system.
	 */
	public static void deleteAllProjects() {

		ProjectExplorer explorer = new ProjectExplorer();
		explorer.activate();
		if (explorer.getProjects().size() > 0) {
			explorer.selectAllProjects();
			new ContextMenu("Refresh").select();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			AbstractWait.sleep(TimePeriod.SHORT);
			new WorkbenchShell();
			explorer.activate();
			explorer.selectAllProjects();
			new ContextMenu("Delete").select();
			Shell s = new DefaultShell("Delete Resources");
			new CheckBox().toggle(true);
			new PushButton("OK").click();
			DeleteUtils.handleDeletion(s, TimePeriod.LONG);
		}
	}

	/**
	 * Import existing project into workspace
	 * 
	 * @param path Path to the project
	 * @param name Name of the project
	 * @param maven true - if the imported project is Maven project
	 * @param fuse true - if the imported project is Fuse project
	 */
	public static void importExistingProject(String path, String name, boolean maven, boolean fuse) {

		ExternalProjectImportWizardDialog dialog = new ExternalProjectImportWizardDialog();
		dialog.open();
		WizardProjectsImportPage page = new WizardProjectsImportPage();
		page.copyProjectsIntoWorkspace(true);
		page.setRootDirectory(path);
		page.selectProjects(name);
		dialog.finish();

		if (maven) {
			new ProjectExplorer().selectProjects(name);
			new ContextMenu("Configure", "Convert to Maven Project").select();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		}

		if (fuse) {
			new ProjectExplorer().selectProjects(name);
			new ContextMenu("Enable Fuse Camel Nature").select();
			AbstractWait.sleep(TimePeriod.getCustom(5));
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
