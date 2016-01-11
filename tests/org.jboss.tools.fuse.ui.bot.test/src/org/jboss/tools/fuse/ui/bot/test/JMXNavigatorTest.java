package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.LogGrapper;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests <i>JMX Navigator</i> view that:
 * <ul>
 * <li>shows running processes (local context) correctly</li>
 * <li><i>Suspend</i>, <i>Resume</i> options work</li>
 * </ul>
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class JMXNavigatorTest extends DefaultTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring";
	private static final String PROJECT_NAME = "camel-spring";
	private static final String PROJECT_CAMEL_CONTEXT = "camel-context.xml";

	private static Logger log = Logger.getLogger(JMXNavigatorTest.class);

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@BeforeClass
	public static void setupCreateProject() throws FuseArchetypeNotFoundException {

		log.info("Create a new Fuse project (" + PROJECT_ARCHETYPE + ")");
		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
	}

	/**
	 * Prepares test environment
	 */
	@Before
	public void setupRunCamelContext() {

		log.info("Run the Fuse project as Local Camel Context");
		new CamelProject(PROJECT_NAME).runCamelContextWithoutTests(PROJECT_CAMEL_CONTEXT);
		new WaitUntil(new ConsoleHasText("Route: route1 started and consuming"), TimePeriod.getCustom(300));
		AbstractWait.sleep(TimePeriod.NORMAL);
	}

	/**
	 * <p>
	 * Test tries to access nodes relevant for Local Camel Context in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer View</li>
	 * <li>run the Fuse project as Local Camel Context</li>
	 * <li>open JMX Navigator View</li>
	 * <li>try to access node "Local Camel Context", "Camel", "camel-1", "Endpoints", "file", "src/data?noop=true"</li>
	 * <li>try to access node "Local Camel Context", "Camel", "camel-1", "Routes", "route1", "file:src/data?noop=true",
	 * "choice1", "when1", "log1", "to1"</li>
	 * </ol>
	 */
	@Test
	public void testProcessesView() {

		JMXNavigator jmx = new JMXNavigator();
		assertNotNull(
				jmx.getNode("Local Camel Context", "Camel", "camel-1", "Endpoints", "file", "src/data?noop=true"));
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camel-1", "Routes", "route1",
				"file:src/data?noop=true", "choice", "when", "log", "to"));
		assertTrue("There are some errors in Error Log", LogGrapper.getFuseErrors().size() == 0);
	}

	/**
	 * <p>
	 * Test tries context menu options related to Camel Context runs as Local Camel Context - Suspend/Resume context.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>open Project Explorer View</li>
	 * <li>run the Fuse project as Local Camel Context</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "Local Camel Context", "Camel", "camel-1"</li>
	 * <li>select the context menu option Suspend Camel Context</li>
	 * <li>check if the Console View contains the text "route1 suspend complete" (true)</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "Local Camel Context", "Camel", "camel-1"</li>
	 * <li>select the context menu option Resume Camel Context</li>
	 * <li>check if the Console View contains the text "route1 resumed" (true)</li>
	 * </ol>
	 */
	@Test
	public void testContextOperations() {

		JMXNavigator jmx = new JMXNavigator();
		jmx.open();
		jmx.getNode("Local Camel Context", "Camel", "camel-1").select();
		log.info("Suspend Camel Context");
		new ContextMenu("Suspend Camel Context").select();
		new WaitUntil(new ConsoleHasText("route1 suspend complete"), TimePeriod.NORMAL);
		jmx.open();
		jmx.getNode("Local Camel Context", "Camel", "camel-1").select();
		log.info("Resume Camel Context");
		new ContextMenu("Resume Camel Context").select();
		new WaitUntil(new ConsoleHasText("route1 resumed"), TimePeriod.NORMAL);
		assertTrue("There are some errors in Error Log", LogGrapper.getFuseErrors().size() == 0);
	}
}
