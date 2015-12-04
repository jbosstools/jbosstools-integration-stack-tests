package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tries to create and run projects from all available archetypes as Local Camel Context
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class FuseProjectTest extends DefaultTest {

	protected Logger log = Logger.getLogger(FuseProjectTest.class);
	private static final List<String> testArchetypes = Arrays.asList(
			"camel-archetype-activemq",
			"camel-archetype-blueprint",
			"camel-archetype-cxf-code-first-blueprint",
			"camel-archetype-cxf-contract-first-blueprint",
			"camel-archetype-java",
			"camel-archetype-spring",
			"camel-archetype-spring-dm",
			"camel-archetype-web",
			"cxf-jaxrs-service",
			"cxf-jaxws-javafirst",
			"wildfly-camel-archetype-spring"
			);

	/**
	 * Checks whether the test methods (archetypes) are complete. If they are not, abort testing.
	 */
	@BeforeClass
	public static void setupCheckArchetypes() {

		org.junit.Assume.assumeTrue(testArchetypes.containsAll(ProjectFactory.getArchetypes()));
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
			if (item.getText().toLowerCase().contains("error")) return true;
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
					|| console.getConsoleText().toLowerCase().contains("error") || console.consoleIsTerminated()) {
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-activemq</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelActiveMQTest() {

		try {
			ProjectFactory.createProject("camel-archetype-activemq", "camel-archetype-activemq");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-activemq"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-activemq"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-blueprint</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelBlueprintTest() {

		try {
			ProjectFactory.createProject("camel-archetype-blueprint", "camel-archetype-blueprint");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-blueprint"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-blueprint"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-cxf-code-first-blueprint</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelCxfCodeFirstBlueprint() {

		try {
			ProjectFactory.createProject("camel-archetype-cxf-code-first-blueprint", "camel-archetype-cxf-code-first-blueprint");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-cxf-code-first-blueprint"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-cxf-code-first-blueprint"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-cxf-contract-first-blueprint</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelCxfContractFirstBlueprint() {

		try {
			ProjectFactory.createProject("camel-archetype-cxf-contract-first-blueprint", "camel-archetype-cxf-contract-first-blueprint");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-cxf-contract-first-blueprint"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-cxf-contract-first-blueprint"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-java</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelJavaTest() {

		try {
			ProjectFactory.createProject("camel-archetype-java", "camel-archetype-java");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-java"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-java"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-spring</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelSpringTest() {

		try {
			ProjectFactory.createProject("camel-archetype-spring", "camel-archetype-spring");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-spring"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-spring"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-spring-dm</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelSpringDMTest() {

		try {
			ProjectFactory.createProject("camel-archetype-spring-dm", "camel-archetype-spring-dm");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-spring-dm"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-spring-dm"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-archetype-web</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelWebTest() {

		try {
			ProjectFactory.createProject("camel-archetype-web", "camel-archetype-web");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-web"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-web"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}


	/**
	 * <p>Tries to create a Fuse project from <i>cxf-jaxrs-service</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelCxfJaxRSTest() {

		try {
			ProjectFactory.createProject("cxf-jaxrs-service", "cxf-jaxrs-service");
			assertTrue("Project is not present in Project Explorer", isPresent("cxf-jaxrs-service"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("cxf-jaxrs-service"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>cxf-jaxws-javafirst</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testCamelCxfJaxWSTest() {

		try {
			ProjectFactory.createProject("cxf-jaxws-javafirst", "cxf-jaxws-javafirst");
			assertTrue("Project is not present in Project Explorer", isPresent("cxf-jaxws-javafirst"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("cxf-jaxws-javafirst"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>wildfly-camel-archetype-spring</i> archetype
	 * and tries to run the project as Local Camel Context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project</li>
	 * <li>open Problems view</li>
	 * <li>check if there are some errors</li>
	 * <li>(optional) try to update project - Maven --> Update Maven Project --> Force Update</li>
	 * <li>check if there are some errors</li>
	 * <li>try to run the project as Local Camel Context</li>
	 * <li>check the console output, if there are some build errors</li>
	 * </ol>
	 */
	@Test
	public void testWildFlyTest() {

		try {
			ProjectFactory.createProject("wildfly-camel-archetype-spring", "wildfly-camel-archetype-spring");
			assertTrue("Project is not present in Project Explorer", isPresent("wildfly-camel-archetype-spring"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("wildfly-camel-archetype-spring"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}
}
