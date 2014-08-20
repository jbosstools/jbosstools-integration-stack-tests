package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.FuseShell;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.impl.ServerFuse;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests a Fuse project deployment
 * 
 * @author tsedmik
 */
@Server(type = ServerReqType.Fuse, state = ServerReqState.PRESENT)
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class DeploymentTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-blueprint";
	private static final String PROJECT_NAME = "camel-blueprint";
	private static final String PROJECT2_ARCHETYPE = "camel-archetype-spring-dm";
	private static final String PROJECT2_NAME = "camel-spring-dm";

	@InjectRequirement
	private static ServerRequirement serverRequirement;

	@BeforeClass
	public static void setUp() {

		new WorkbenchShell().setFocus();
		new WorkbenchShell().maximize();
		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
		ProjectFactory.createProject(PROJECT2_NAME, PROJECT2_ARCHETYPE);
	}

	@AfterClass
	public static void cleanUp() {

		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
		ServerManipulator.clean(serverRequirement.getConfig().getName());
	}

	@After
	public void manageServers() {

		ServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		ServerManipulator.stopServer(serverRequirement.getConfig().getName());
		ServerManipulator.removeAllModules("TEST");
		ServerManipulator.stopServer("TEST");
		ServerManipulator.removeServer("TEST");
	}

	@Test
	public void testServerCreationDeployment() {

		ServerFuse fuse = (ServerFuse) serverRequirement.getConfig().getServerBase();
		ServerManipulator.addServer(fuse.getServerType(), fuse.getHost(), "TEST", fuse.getPort(), fuse.getUsername(),
				fuse.getPassword(), PROJECT_NAME);
		assertTrue(ServerManipulator.hasServerModule("TEST", PROJECT_NAME));
		ServerManipulator.startServer("TEST");
		assertTrue(new FuseShell().execute("log:display").contains(
				"Route: timerToLog started and consuming from: Endpoint[timer://foo?period=5000"));
		ServerManipulator.removeAllModules("TEST");
		AbstractWait.sleep(TimePeriod.getCustom(20));
		assertTrue(new FuseShell().execute("log:display").contains("(CamelContext: blueprintContext) is shutdown"));
	}

	@Test
	public void testImmeadiatelyPublishing() {

		String server = serverRequirement.getConfig().getName();

		ServerManipulator.startServer(server);
		ServerManipulator.setImmeadiatelyPublishing(server, false);
		ServerManipulator.addModule(server, PROJECT2_NAME);
		assertTrue(ServerManipulator.hasServerModule(server, PROJECT2_NAME));
		assertFalse(new FuseShell()
				.execute("log:display")
				.contains(
						"Publishing application context as OSGi service with properties {org.springframework.context.service.name=camel-spring-dm"));
		ServerManipulator.publish(server);
		AbstractWait.sleep(TimePeriod.NORMAL);
		assertTrue(new FuseShell()
				.execute("log:display")
				.contains(
						"Publishing application context as OSGi service with properties {org.springframework.context.service.name=camel-spring-dm"));
	}
}
