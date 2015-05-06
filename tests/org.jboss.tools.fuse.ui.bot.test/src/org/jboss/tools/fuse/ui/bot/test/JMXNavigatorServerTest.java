package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.fuse.reddeer.condition.FuseLogContainsText;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.preference.ConsolePreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
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
@Server(type = ServerReqType.Fuse, state = ServerReqState.RUNNING)
public class JMXNavigatorServerTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring-dm";
	private static final String PROJECT_NAME = "camel-spring-dm";

	private static String serverName;
	private static boolean setupIsDone = false;

	@InjectRequirement
	private static ServerRequirement serverReq;

	@Before
	public void setUp() throws FuseArchetypeNotFoundException {

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
		ServerManipulator.addModule(serverName, PROJECT_NAME);

		// Deleting Error Log
		new ErrorLogView().deleteLog();

		setupIsDone = true;
	}

	@After
	public void defaultClean() {

		new WorkbenchShell();

		// Closing all non workbench shells 
		ShellHandler.getInstance().closeAllNonWorbenchShells();
	}

	@AfterClass
	public static void defaultFinalClean() {

		new WorkbenchShell();
		
		// Deleting all projects
		new ProjectExplorer().deleteAllProjects();

		// Stopping and deleting configured servers
		ServerManipulator.deleteAllServers();
		ServerManipulator.deleteAllServerRuntimes();
	}

	@Test
	public void testServerInJMXNavigator() {

		JMXNavigator jmx = new JMXNavigator();
		assertNotNull(jmx.getNode("karaf"));
		jmx.connectTo("karaf");
		assertNotNull(jmx.getNode("karaf", "Camel", "camel-1", "Endpoints", "timer", "foo?period=5000"));
		assertNotNull(jmx.getNode("karaf", "Camel", "camel-1", "Routes", "route1", "timer:foo?period=5000", "setBody", "log"));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

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
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}
}
