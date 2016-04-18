package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.RUNNING;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Fuse;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Karaf;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.ServiceMix;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.LogGrapper;
import org.jboss.tools.common.reddeer.preference.ConsolePreferencePage;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.condition.FuseLogContainsText;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests <i>JMX Navigator</i> (in a server's fashion):
 * <ul>
 * <li>show a server process</li>
 * <li><i>Suspend</i>, <i>Resume</i> options work</li>
 * </ul>
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
@Server(type = { Fuse, Karaf, ServiceMix }, state = RUNNING)
public class JMXNavigatorServerTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring-dm";
	private static final String PROJECT_NAME = "camel-spring-dm";

	private static String serverName;
	private static boolean setupIsDone = false;

	@InjectRequirement
	private static ServerRequirement serverReq;

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Before
	public void setup() throws FuseArchetypeNotFoundException {

		if (setupIsDone) {
			return;
		}

		// Maximizing workbench shell
		new WorkbenchShell().maximize();

		// Disable showing Console view after standard output changes
		ConsolePreferencePage consolePref = new ConsolePreferencePage();
		consolePref.open();
		consolePref.toggleShowConsoleErrorWrite(false);
		consolePref.toggleShowConsoleStandardWrite(false);
		consolePref.ok();

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
		serverName = serverReq.getConfig().getName();
		FuseServerManipulator.addModule(serverName, PROJECT_NAME);

		// Deleting Error Log
		new ErrorLogView().deleteLog();

		setupIsDone = true;
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupDefaultClean() {

		new WorkbenchShell();

		// Closing all non workbench shells
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	/**
	 * Cleans up test environment
	 */
	@AfterClass
	public static void setupDefaultFinalClean() {

		new WorkbenchShell();

		// Deleting all projects
		new ProjectExplorer().deleteAllProjects();

		// Stopping and deleting configured servers
		FuseServerManipulator.deleteAllServers();
		FuseServerManipulator.deleteAllServerRuntimes();
	}

	/**
	 * <p>
	 * Test tries to access nodes relevant for JBoss Fuse in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring-dm archetype</li>
	 * <li>add a new Fuse server</li>
	 * <li>start the server</li>
	 * <li>open JMX Navigator View</li>
	 * <li>try to access node "karaf", "Camel", "camelContext-...", "Endpoints", "timer", "foo?period=5000"</li>
	 * <li>try to access node "karaf", "Camel", "camelContext-...", "Routes", "route1", "timer:foo?period=5000",
	 * "setBody1", "log1"</li>
	 * </ol>
	 */
	@Test
	public void testServerInJMXNavigator() {

		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.open();
		assertNotNull("There is no Fuse node in JMX Navigator View!", jmx.getNode("karaf"));
		jmx.connectTo("karaf");
		assertNotNull("The following path is inaccesible: karaf/Camel/camelContext-.../Endpoints/timer/foo?period=5000",
				jmx.getNode("karaf", "Camel", "camelContext", "Endpoints", "timer", "foo?period=5000"));
		assertNotNull(
				"The following path is inaccesible: karaf/Camel/camel-*/Routes/route/timer:foo?period=5000/setBody/log",
				jmx.getNode("karaf", "Camel", "camelContext", "Routes", "_route1", "timer:foo?period=5000",
						"SetBody _setBody1", "Log _log1"));
		assertTrue("There are some errors in Error Log", LogGrapper.getPluginErrors("fuse").size() == 0);
	}

	/**
	 * <p>
	 * Test tries context menu options related to Camel Context deployed on the Fuse server - Suspend/Resume context.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring-dm archetype</li>
	 * <li>add a new Fuse server</li>
	 * <li>add the project to the server</li>
	 * <li>start the server</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "karaf", "Camel", "camelContext-..."</li>
	 * <li>select the context menu option Suspend Camel Context</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text "(CamelContext: camel-1) is suspended" (true)</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "karaf", "Camel", "camelContext-..."</li>
	 * <li>select the context menu option Resume Camel Context</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text "(CamelContext: camel-1) resumed" (true)</li>
	 * </ol>
	 */
	@Test
	public void testServerContextOperationsInJMXNavigator() {

		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.open();
		String camel = jmx.getNode("karaf", "Camel", "camelContext").getText();
		assertNotNull("Camel context was not found in JMX Navigator View!", camel);
		assertTrue("Suspension was not performed", jmx.suspendCamelContext("karaf", "Camel", camel));
		try {
			new WaitUntil(new FuseLogContainsText("(CamelContext: " + camel + ") is suspended"), TimePeriod.NORMAL);
		} catch (WaitTimeoutExpiredException e) {
			fail("Camel context was not suspended!");
		}
		assertTrue("Resume of Camel Context was not performed", jmx.resumeCamelContext("karaf", "Camel", camel));
		try {
			new WaitUntil(new FuseLogContainsText("(CamelContext: " + camel + ") resumed"), TimePeriod.NORMAL);
		} catch (WaitTimeoutExpiredException e) {
			fail("Camel context was not resumed!");
		}
		assertTrue("There are some errors in Error Log", LogGrapper.getPluginErrors("fuse").size() == 0);
	}
}
