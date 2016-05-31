package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.PRESENT;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.EAP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.eclipse.wst.server.ui.RuntimePreferencePage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
import org.jboss.tools.runtime.reddeer.wizard.ServerRuntimeWizard;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests deployment of 'wildfly-camel-archetype-spring' project on Fuse on EAP runtime
 * 
 * @author tsedmik
 */
@Server(type = EAP, state = PRESENT)
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
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@BeforeClass
	public static void setupInitial() throws FuseArchetypeNotFoundException {

		// change default runtime JRE -- due to https://issues.jboss.org/browse/JBTIS-719
		ServerBase base = serverRequirement.getConfig().getServerBase(); 
		if (base.getJreName() == null) {
			WorkbenchPreferenceDialog preferences = new WorkbenchPreferenceDialog();
			preferences.open();
			RuntimePreferencePage prefs = new RuntimePreferencePage();
			preferences.select(prefs);
			prefs.editRuntime(prefs.getServerRuntimes().get(0).getName());
			ServerRuntimeWizard runtimeWizard = new ServerRuntimeWizard();
			if (runtimeWizard.getExecutionEnvironment().contains("1.6")) {
				List<String> list = runtimeWizard.getExecutionEnvironments();
				for (String temp : list) {
					if (temp.contains("1.7") || temp.contains("1.8")) {
						runtimeWizard.selectExecutionEnvironment(temp);
						break;
					}
				}
			}
			runtimeWizard.finish();
			preferences.ok();
		}
		new ServersView().getServer(base.getName()).start();

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupManageServers() {

		FuseServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
	}

	/**
	 * <p>
	 * Test tries to deploy a project on Fuse on EAP server.
	 * </p>
	 * <b>Steps</b>
	 * <ol>
	 * <li>create a new project with wildfly-camel-archetype-spring archetype</li>
	 * <li>add a new Fuse on EAP server</li>
	 * <li>add the project to the server</li>
	 * <li>check if the server contains the project in Add and Remove ... dialog window</li>
	 * <li>start the server</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text "(CamelContext: spring-context) started"</li>
	 * <li>check if Fuse Shell view contains text "Deployed "wildfly-spring.war""</li>
	 * <li>open Browser View and try to open URL "http://localhost:8080/wildfly-spring"</li>
	 * <li>remove all deployed modules</li>
	 * <li>open Fuse Shell view and execute command log:display</li>
	 * <li>check if Fuse Shell view contains text "(CamelContext: spring-context) is shutdown"</li>
	 * <li>check if Fuse Shell view contains text "Undeployed "wildfly-spring.war""</li>
	 * </ol>
	 */
	@Test
	public void testDeployment() {

		FuseServerManipulator.addModule(serverRequirement.getConfig().getName(), PROJECT_NAME);
		assertTrue(FuseServerManipulator.hasServerModule(serverRequirement.getConfig().getName(), PROJECT_NAME));
		new WaitUntil(new ConsoleHasText("(CamelContext: spring-context) started"), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("Deployed \"wildfly-spring.war\""), TimePeriod.LONG);
		BrowserView browser = new BrowserView();
		browser.open();
		browser.openPageURL("http://localhost:8080/wildfly-spring");
		assertTrue(browser.getText().contains("Hello null"));
		FuseServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		new WaitUntil(new ConsoleHasText("(CamelContext: spring-context) is shutdown"), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("Undeployed \"wildfly-spring.war\""), TimePeriod.LONG);
		assertFalse(FuseServerManipulator.hasServerModule(serverRequirement.getConfig().getName(), PROJECT_NAME));
		browser.open();
		browser.openPageURL("http://localhost:8080/wildfly-spring");
		assertTrue(browser.getText().contains("404"));
	}
}
