package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.tools.fuse.reddeer.ProjectType.BLUEPRINT;
import static org.jboss.tools.fuse.reddeer.ProjectType.JAVA;
import static org.jboss.tools.fuse.reddeer.ProjectType.SPRING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.FileUtils;
import org.jboss.tools.common.reddeer.preference.MavenUserSettingsPreferencePage;
import org.jboss.tools.fuse.reddeer.ProjectType;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

/**
 * Tries to create and run projects from all available archetypes as Local Camel
 * Context
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class FuseProjectTest extends DefaultTest {

	protected Logger log = Logger.getLogger(FuseProjectTest.class);
	private String template;
	private static String maven;

	/**
	 * Sets parameters for parameterized test
	 * 
	 * @return List of all available Fuse project archetypes
	 */
	@Parameters
	public static Collection<String> setupData() {
		List<String> temp = ProjectFactory.getAllAvailableTemplates();
		temp.add("empty:blueprint");
		temp.add("empty:spring");
		temp.add("empty:java");
		return temp;
	}

	/**
	 * Utilizes passing parameters using the constructor
	 * 
	 * @param template
	 *            a Fuse project archetype
	 */
	public FuseProjectTest(String template) {
		this.template = template;
	}

	/**
	 * Changes local repository to an empty one
	 */
	@BeforeClass
	public static void setupMaven() {

		MavenUserSettingsPreferencePage pref = new MavenUserSettingsPreferencePage();
		pref.open();
		maven = pref.getUserSettings();
		pref.setUserSettings(System.getProperty("maven.settings"));
		pref.updateSettings();
		pref.reindex();
		pref.ok();
	}

	/**
	 * Deletes a local Maven repository
	 */
	@Before
	public void setupDeleteMavenRepo() {

		FileUtils.deleteDir(new File(System.getProperty("maven.repository")));
	}

	/**
	 * Revertes change of the local repository
	 */
	@AfterClass
	public static void setupMavenBack() {

		MavenUserSettingsPreferencePage pref = new MavenUserSettingsPreferencePage();
		pref.open();
		pref.setUserSettings(maven);
		pref.updateSettings();
		pref.reindex();
		pref.ok();

		FileUtils.deleteDir(new File(System.getProperty("maven.repository")));
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupClean() {

		defaultClean();
		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
		new WorkbenchShell();
	}

	private boolean hasErrors() {

		new ProblemsView().open();
		for (TreeItem item : new DefaultTree().getItems()) {
			if (item.getText().toLowerCase().contains("error"))
				return true;
		}
		return false;
	}

	private boolean isPresent(String name) {

		return new ProjectExplorer().containsProject(name);
	}

	private boolean canBeRun(String name) {

		try {
			log.info("Trying to run the project as Local Camel Context");
			new CamelProject(name).runCamelContext();
			ConsoleView console = new ConsoleView();
			if (console.getConsoleText().contains("BUILD FAILURE")
					|| console.getConsoleText().toLowerCase().contains("[ERROR]") || console.consoleIsTerminated()) {
				log.warn("There is a problem with building '" + name + "' project");
				return false;
			}
		} catch (EclipseLayerException e) {
			log.warn("There is no Camel Context file in '" + name + "' project");
		} catch (WaitTimeoutExpiredException e) {
			log.warn("There is a problem with building '" + name + "' project");
			return false;
		}

		return true;
	}

	/**
	 * <p>
	 * Tries to create a Fuse project from <i>${FUSE-TEMPLATE}</i> template and
	 * tries to run the project as Local Camel Context.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project -->
	 * Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testArchetype() {

		String[] templateComposite = template.split(":");
		ProjectType type = BLUEPRINT;
		if (templateComposite[1].startsWith("spring"))
			type = SPRING;
		if (templateComposite[1].startsWith("java"))
			type = JAVA;
		if (templateComposite[0].equals("empty")) {
			ProjectFactory.newProject("test").type(type).create();
		} else {
			ProjectFactory.newProject("test").template(templateComposite[0]).type(type).create();
		}
		assertTrue("Project '" + template + "' is not present in Project Explorer", isPresent("test"));
		assertFalse("Project '" + template + "' was created with errors", hasErrors());
		if ((type != JAVA) && (!template.startsWith("empty")))
			assertTrue("Project '" + template + "' cannot be run as Local Camel Context", canBeRun("test"));
	}
}
