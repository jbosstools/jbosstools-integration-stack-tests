package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.RUNNING;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.EAP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests deployment of 'wildfly-camel-archetype-spring' project on Fuse on EAP runtime
 * 
 * @author tsedmik
 */
@Server(type = EAP, state = RUNNING)
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class DeploymentEAPTest extends DefaultTest {

	private static final String PROJECT_ARCHETYPE = "wildfly-camel-archetype-spring";
	private static final String PROJECT_NAME = "wildfly-spring";
	
	@InjectRequirement
	private static ServerRequirement serverRequirement;

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException Fuse archetype was not found. Tests cannot be executed!
	 */
	@BeforeClass
	public static void setupInitial() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupManageServers() {

		ServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
	}

	/**
	 * <p>Test tries to deploy a project on Fuse on EAP server.</p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with wildfly-camel-archetype-spring archetype</li>
	 * <li>add a new Fuse on EAP server</li>
	 * <li>add the project to the server</li>
	 * <li>check if the server contains the project in Add and Remove ... dialog window</li>
	 * <li>start the server</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text TODO</li>
	 * <li>remove all deployed modules</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text TODO</li>
	 * </ol>
	 */
	@Test
	public void testDeployment() {

		ServerManipulator.addModule(serverRequirement.getConfig().getName(), PROJECT_NAME);
		assertTrue(ServerManipulator.hasServerModule(serverRequirement.getConfig().getName(), PROJECT_NAME));
		new WaitUntil(new ConsoleHasText("(CamelContext: spring-context) started"));
		new WaitUntil(new ConsoleHasText("Deployed \"wildfly-spring.war\""));
		BrowserView browser = new BrowserView();
		browser.open();
		browser.openPageURL("http://localhost:8080/wildfly-spring");
		assertTrue(browser.getText().contains("Hello null"));
		ServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		new WaitUntil(new ConsoleHasText("(CamelContext: spring-context) is shutdown"));
		new WaitUntil(new ConsoleHasText("Undeployed \"wildfly-spring.war\""));
		assertFalse(ServerManipulator.hasServerModule(serverRequirement.getConfig().getName(), PROJECT_NAME));
		browser.open();
		browser.openPageURL("http://localhost:8080/wildfly-spring");
		assertTrue(browser.getText().contains("404"));
	}
}
