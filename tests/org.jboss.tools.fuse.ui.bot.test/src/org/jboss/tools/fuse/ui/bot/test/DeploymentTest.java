package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
import org.jboss.tools.runtime.reddeer.view.TerminalView;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests a Fuse project deployment
 * 
 * @author tsedmik
 */
@Server(type = { ServerReqType.Fuse, ServerReqType.Karaf, ServerReqType.ServiceMix }, state = ServerReqState.PRESENT)
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

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@BeforeClass
	public static void setupInitial() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
		new ProjectExplorer().getProject(PROJECT_NAME)
				.getProjectItem("src/test/java", "com.mycompany.camel.blueprint", "RouteTest.java").delete();
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupManageServers() {

		new WorkbenchShell().setFocus();
		FuseServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		FuseServerManipulator.stopServer(serverRequirement.getConfig().getName());
		FuseServerManipulator.removeServer(serverRequirement.getConfig().getName());
	}

	/**
	 * <p>
	 * Test tries to deploy a project on Fuse server.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-blueprint archetype</li>
	 * <li>add a new Fuse server</li>
	 * <li>add the project to the server</li>
	 * <li>check if the server contains the project in Add and Remove ... dialog window</li>
	 * <li>start the server</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text Route: timerToLog started and consuming from:
	 * Endpoint[timer://foo?period=5000 (project is deployed)</li>
	 * <li>remove all deployed modules</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text (CamelContext: blueprintContext) is shutdown (project is undeployed)
	 * </li>
	 * </ol>
	 */
	@Test
	public void testServerDeployment() {
		FuseServerManipulator.addModule(serverRequirement.getConfig().getName(), PROJECT_NAME);
		assertTrue(FuseServerManipulator.hasServerModule(serverRequirement.getConfig().getName(), PROJECT_NAME));
		FuseServerManipulator.startServer(serverRequirement.getConfig().getName());
		FuseServerManipulator.publish(serverRequirement.getConfig().getName());
		assertTrue(new TerminalView().containsLog(PROJECT_IS_DEPLOYED));
		FuseServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		AbstractWait.sleep(TimePeriod.getCustom(30));
		assertTrue(new TerminalView().containsLog(PROJECT_IS_UNDEPLOYED));
	}
}