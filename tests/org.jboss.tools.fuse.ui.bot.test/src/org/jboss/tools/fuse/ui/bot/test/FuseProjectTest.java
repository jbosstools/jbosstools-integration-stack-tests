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
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
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
			"camel-cxf-code-first-archetype",
			"camel-cxf-contract-first-archetype",
			"camel-drools-archetype",
			"camel-webservice-archetype",
			"camel-archetype-activemq",
			"camel-archetype-api-component",
			"camel-archetype-blueprint",
			"camel-archetype-component",
			"camel-archetype-cxf-code-first-blueprint",
			"camel-archetype-cxf-contract-first-blueprint",
			"camel-archetype-dataformat",
			"camel-archetype-java",
			"camel-archetype-scr",
			"camel-archetype-spring",
			"camel-archetype-spring-dm",
			"camel-archetype-war",
			"camel-archetype-web",
			"camel-archetype-webconsole",
			"cxf-jaxrs-service",
			"cxf-jaxws-javafirst"
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
	 * <p>Tries to create a Fuse project from <i>camel-cxf-code-first-archetype</i> archetype
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
	public void testCamelCxfCodeTest() {

		try {
			ProjectFactory.createProject("camel-cxf-code-first-archetype", "camel-cxf-code-first-archetype");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-cxf-code-first-archetype"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-cxf-code-first-archetype"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-cxf-contract-first-archetype</i> archetype
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
	public void testCamelCxfContractTest() {

		try {
			ProjectFactory.createProject("camel-cxf-contract-first-archetype", "camel-cxf-contract-first-archetype");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-cxf-contract-first-archetype"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-cxf-contract-first-archetype"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-drools-archetype</i> archetype
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
	public void testCamelDroolsTest() {

		try {
			ProjectFactory.createProject("camel-drools-archetype", "camel-drools-archetype");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-drools-archetype"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-drools-archetype"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
	}

	/**
	 * <p>Tries to create a Fuse project from <i>camel-webservice-archetype</i> archetype
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
	public void testCamelWebServiceTest() {

		try {
			ProjectFactory.createProject("camel-webservice-archetype", "camel-webservice-archetype");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-webservice-archetype"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-webservice-archetype"));
		} catch (FuseArchetypeNotFoundException e) {
			log.warn("Archetype is not available");
		}
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-api-component</i> archetype
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
	public void testCamelAPIComponentTest() {

		try {
			ProjectFactory.createProject("camel-archetype-api-component", "camel-archetype-api-component");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-api-component"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-api-component"));
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-component</i> archetype
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
	public void testCamelComponentTest() {

		try {
			ProjectFactory.createProject("camel-archetype-component", "camel-archetype-component");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-component"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-component"));
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-dataformat</i> archetype
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
	public void testCamelDataFormatTest() {

		try {
			ProjectFactory.createProject("camel-archetype-dataformat", "camel-archetype-dataformat");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-dataformat"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-dataformat"));
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-scr</i> archetype
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
	public void testCamelSCRTest() {

		try {
			ProjectFactory.createProject("camel-archetype-scr", "camel-archetype-scr");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-scr"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-scr"));
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-war</i> archetype
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
	public void testCamelWARTest() {

		try {
			ProjectFactory.createProject("camel-archetype-war", "camel-archetype-war");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-war"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-war"));
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
	 * <p>Tries to create a Fuse project from <i>camel-archetype-webconsole</i> archetype
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
	public void testCamelWebConsoleTest() {

		try {
			ProjectFactory.createProject("camel-archetype-webconsole", "camel-archetype-webconsole");
			assertTrue("Project is not present in Project Explorer", isPresent("camel-archetype-webconsole"));
			assertFalse("Project was created with errors", hasErrors());
			assertTrue("Project cannot be run as Local Camel Context", canBeRun("camel-archetype-webconsole"));
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
}
