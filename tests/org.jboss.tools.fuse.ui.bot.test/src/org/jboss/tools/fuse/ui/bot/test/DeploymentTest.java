package org.jboss.tools.fuse.ui.bot.test;

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
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
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
public class DeploymentTest extends DefaultTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-blueprint";
	private static final String PROJECT_NAME = "camel-blueprint";
	private static final String PROJECT_IS_DEPLOYED = "Route: timerToLog started and consuming from: Endpoint[timer://foo?period=5000";
	private static final String PROJECT_IS_UNDEPLOYED = "(CamelContext: camel-1) is shutdown";
	
	@InjectRequirement
	private static ServerRequirement serverRequirement;
	
	@BeforeClass
	public static void setUp() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("src/test/java", "com.mycompany.camel.blueprint", "RouteTest.java").delete();
	}

	@After
	public void manageServers() {

		new WorkbenchShell().setFocus();
		ServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		ServerManipulator.stopServer(serverRequirement.getConfig().getName());
		ServerManipulator.removeServer(serverRequirement.getConfig().getName());
	}

	@Test
	public void testServerDeployment() {
		ServerManipulator.addModule(serverRequirement.getConfig().getName(), PROJECT_NAME);
		assertTrue(ServerManipulator.hasServerModule(serverRequirement.getConfig().getName(), PROJECT_NAME));
		ServerManipulator.startServer(serverRequirement.getConfig().getName());
		ServerManipulator.publish(serverRequirement.getConfig().getName());
		assertTrue(new FuseShell().containsLog(PROJECT_IS_DEPLOYED));
		ServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		AbstractWait.sleep(TimePeriod.getCustom(10));
		assertTrue(new FuseShell().containsLog(PROJECT_IS_UNDEPLOYED));
	}
}