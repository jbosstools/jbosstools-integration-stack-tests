package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.tools.fuse.reddeer.ProjectType.SPRING;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.common.reddeer.LogGrapper;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
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

	private static final String PROJECT_NAME = "camel-spring";
	private static final String PROJECT_CAMEL_CONTEXT = "camel-context.xml";

	private static Logger log = Logger.getLogger(JMXNavigatorTest.class);

	/**
	 * Prepares test environment
	 */
	@BeforeClass
	public static void setupCreateProject() {

		log.info("Create a new Fuse project from 'Content Based Router' template");
		ProjectFactory.createProject(PROJECT_NAME, "Content Based Router", SPRING);
	}

	/**
	 * Prepares test environment
	 */
	@Before
	public void setupRunCamelContext() {

		log.info("Run the Fuse project as Local Camel Context");
		new CamelProject(PROJECT_NAME).runCamelContextWithoutTests(PROJECT_CAMEL_CONTEXT);
	}

	/**
	 * <p>
	 * Test tries to access nodes relevant for Local Camel Context in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project from 'Content Based Router' template</li>
	 * <li>open Project Explorer View</li>
	 * <li>run the Fuse project as Local Camel Context</li>
	 * <li>open JMX Navigator View</li>
	 * <li>try to access node "Local Camel Context", "Camel", "camelContext", "Endpoints", "file", "work/cbr/input"</li>
	 * <li>try to access node "Local Camel Context", "Camel", "camelContext", "Routes", "cbr-route",
	 * "file:work/cbr/input", "Log _log1", "Choice", "Otherwise", "Log _log4", "file:work/cbr/output/others"</li>
	 * </ol>
	 */
	@Test
	public void testProcessesView() {

		FuseJMXNavigator jmx = new FuseJMXNavigator();
		assertNotNull(
				"The following path is inaccesible: Local Camel Context/Camel/camelContext-.../Endpoints/file/work/cbr/input",
				jmx.getNode("Local Camel Context", "Camel", "camelContext", "Endpoints", "file", "work/cbr/input"));
		assertNotNull(
				"The following path is inaccesible: Local Camel Context/Camel/camelContext-.../Routes/cbr-route/file:work/cbr/input/Log _log1/Choice/Otherwise/Log _log4/file:work/cbr/output/others",
				jmx.getNode("Local Camel Context", "Camel", "camelContext", "Routes", "cbr-route",
						"file:work/cbr/input", "Log _log1", "Choice", "Otherwise", "Log _log4",
						"file:work/cbr/output/others"));
		assertTrue("There are some errors in Error Log", LogGrapper.getPluginErrors("fuse").size() == 0);
	}

	/**
	 * <p>
	 * Test tries context menu options related to Camel Context runs as Local Camel Context - Suspend/Resume context.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project from 'Content Based Router' template</li>
	 * <li>open Project Explorer View</li>
	 * <li>run the Fuse project as Local Camel Context</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "Local Camel Context", "Camel", "camelContext-..."</li>
	 * <li>select the context menu option Suspend Camel Context</li>
	 * <li>check if the Console View contains the text "Route: cbr-route suspend complete" (true)</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "Local Camel Context", "Camel", "camelContext-..."</li>
	 * <li>select the context menu option Resume Camel Context</li>
	 * <li>check if the Console View contains the text "Route: cbr-route resumed" (true)</li>
	 * </ol>
	 */
	@Test
	public void testContextOperations() {

		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.open();
		assertTrue("Suspension was not performed",
				jmx.suspendCamelContext("Local Camel Context", "Camel", "camelContext"));
		try {
			new WaitUntil(new ConsoleHasText("Route: cbr-route suspend complete"), TimePeriod.NORMAL);
		} catch (WaitTimeoutExpiredException e) {
			fail("Camel context was not suspended!");
		}
		assertTrue("Resume of Camel Context was not performed",
				jmx.resumeCamelContext("Local Camel Context", "Camel", "camelContext"));
		try {
			new WaitUntil(new ConsoleHasText("Route: cbr-route resumed"), TimePeriod.NORMAL);
		} catch (WaitTimeoutExpiredException e) {
			fail("Camel context was not resumed!");
		}
		assertTrue("There are some errors in Error Log", LogGrapper.getPluginErrors("fuse").size() == 0);
	}
}
