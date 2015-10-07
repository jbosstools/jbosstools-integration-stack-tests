package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.tools.fuse.reddeer.condition.FuseLogContainsText;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.preference.ConsolePreferencePage;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.LogGrapper;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
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
@Server(type = {ServerReqType.Fuse, ServerReqType.Karaf, ServerReqType.ServiceMix}, state = ServerReqState.RUNNING)
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
	 * @throws FuseArchetypeNotFoundException Fuse archetype was not found. Tests cannot be executed!
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
	 * <p>Test tries to access nodes relevant for JBoss Fuse in JMX Navigator view.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring-dm archetype</li>
	 * <li>add a new Fuse server</li>
	 * <li>add the project to the server</li>
	 * <li>start the server</li>
	 * <li>open JMX Navigator View</li>
	 * <li>try to access node "karaf", "Camel", "camel-1", "Endpoints", "timer", "foo?period=5000"</li>
	 * <li>try to access node "karaf", "Camel", "camel-1", "Routes", "route1", "timer:foo?period=5000", "setBody1", "log1"</li>
	 * </ol>
	 */
	@Test
	public void testServerInJMXNavigator() {

		JMXNavigator jmx = new JMXNavigator();
		assertNotNull(jmx.getNode("karaf"));
		jmx.connectTo("karaf");
		assertNotNull(jmx.getNode("karaf", "Camel", "camel-1", "Endpoints", "timer", "foo?period=5000"));
		assertNotNull(jmx.getNode("karaf", "Camel", "camel-1", "Routes", "route1", "timer:foo?period=5000", "setBody", "log"));
		assertTrue("There are some errors in Error Log", LogGrapper.getFuseErrors().size() == 0);
	}

	/**
	 * <p>Test tries context menu options related to Camel Context deployed on the Fuse server - Suspend/Resume context.</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring-dm archetype</li>
	 * <li>add a new Fuse server</li>
	 * <li>add the project to the server</li>
	 * <li>start the server</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "karaf", "Camel", "camel-1"</li>
	 * <li>select the context menu option Suspend Camel Context</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text "(CamelContext: camel-1) is suspended" (true)</li>
	 * <li>open JMX Navigator View</li>
	 * <li>select node "karaf", "Camel", "camel-1"</li>
	 * <li>select the context menu option Resume Camel Context</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text "(CamelContext: camel-1) resumed" (true)</li>
	 * </ol>
	 */
	@Test
	public void testServerContextOperationsInJMXNavigator() {

		JMXNavigator jmx = new JMXNavigator();
		new JMXNavigator().getNode("karaf", "Camel", "camel-1").select();
		new ContextMenu("Suspend Camel Context").select();
		new WaitUntil(new FuseLogContainsText("(CamelContext: camel-1) is suspended"), TimePeriod.NORMAL);
		jmx.open();
		new JMXNavigator().getNode("karaf", "Camel", "camel-1").select();
		new ContextMenu("Resume Camel Context").select();
		new WaitUntil(new FuseLogContainsText("(CamelContext: camel-1) resumed"), TimePeriod.NORMAL);
		assertTrue("There are some errors in Error Log", LogGrapper.getFuseErrors().size() == 0);
	}
}
