package org.jboss.tools.fuse.ui.bot.test.utils;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.eclipse.utils.DeleteUtils;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.fuse.reddeer.ProjectTemplate;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.wizard.NewFuseIntegrationProjectWizard;

/**
 * Can create new Fuse projects or import existing
 * 
 * @author tsedmik
 */
public class ProjectFactory {
	
	private String name;
	private String version;
	private String template;
	private ProjectType type;
	
	private ProjectFactory(String name) {
		this.name = name;
	}
	
	public ProjectFactory version(String version) {
		this.version = version;
		return this;
	}

	public ProjectFactory template(ProjectTemplate template) {
		this.template = template.getName();
		return this;
	}
	
	public ProjectFactory template(String template) {
		this.template = template;
		return this;
	}
	
	public ProjectFactory type(ProjectType type) {
		this.type = type;
		return this;
	}
	
	public void create() {
		NewFuseIntegrationProjectWizard wiz = new NewFuseIntegrationProjectWizard();
		wiz.open();
		wiz.setProjectName(name);
		wiz.next();
		if (version != null) {
			wiz.selectCamelVersion(version);
		}
		wiz.next();
		if (template != null) {
			wiz.selectTemplate(template);
		}
		wiz.setProjectType(type);
		wiz.finish();
		openAssociatedPerspective();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(300));
	}
	
	public static ProjectFactory newProject(String name) {
		return new ProjectFactory(name);
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
	 * @param path
	 *            Path to the project
	 * @param name
	 *            Name of the project
	 * @param maven
	 *            true - if the imported project is Maven project
	 * @param fuse
	 *            true - if the imported project is Fuse project
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

	private static void openAssociatedPerspective() {
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Open Associated Perspective?"), TimePeriod.NORMAL);
			new DefaultShell("Open Associated Perspective?");
			new PushButton("No").click();
		} catch (Exception ex) {
		}
	}
}
