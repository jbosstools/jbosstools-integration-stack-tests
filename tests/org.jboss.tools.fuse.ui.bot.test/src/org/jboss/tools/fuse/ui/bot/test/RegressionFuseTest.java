package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.condition.FuseLogContainsText;
import org.jboss.tools.fuse.reddeer.debug.BreakpointsView;
import org.jboss.tools.fuse.reddeer.debug.IsRunning;
import org.jboss.tools.fuse.reddeer.debug.ResumeButton;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.preference.ServerRuntimePreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class contains test cases verifying resolved issues
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
@Server(type = ServerReqType.Fuse, state = ServerReqState.PRESENT)
public class RegressionFuseTest extends DefaultTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@After
	public void clean() {

		String server = serverRequirement.getConfig().getName();
		if (ServerManipulator.isServerStarted(server)) {
			ServerManipulator.stopServer(server);
		}
		new ProjectExplorer().deleteAllProjects();
	}

	/**
	 * New Server Runtime Wizard - Cancel/Finish button error
	 * https://issues.jboss.org/browse/FUSETOOLS-1067
	 */
	@Test
	public void issue_1067() {

		new ServerRuntimePreferencePage().open();

		new PushButton("Add...").click();
		new DefaultShell("New Server Runtime Environment").setFocus();
		new DefaultTreeItem("JBoss Fuse").expand();

		// tests the _Finish_ button
		for (TreeItem item : new DefaultTreeItem("JBoss Fuse").getItems()) {
			if (!item.getText().startsWith("JBoss"))
				continue;
			AbstractWait.sleep(TimePeriod.SHORT);
			item.select();
			try {

				assertFalse(new PushButton("Finish").isEnabled());
			} catch (AssertionError ex) {

				new DefaultTreeItem("JBoss Fuse").select();
				AbstractWait.sleep(TimePeriod.SHORT);
				new PushButton("Cancel").click();
				AbstractWait.sleep(TimePeriod.NORMAL);
				new DefaultShell().close();
				throw ex;
			}
		}

		// tests the _Cancel_ button
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTreeItem("JBoss Fuse", serverRequirement.getConfig().getServerBase().getName()).select();
		new PushButton("Cancel").click();
		try {

			assertTrue(new DefaultShell().getText().equals("Preferences"));
		} catch (AssertionError ex) {

			new DefaultShell().close();
			new DefaultTreeItem("JBoss Fuse").select();
			AbstractWait.sleep(TimePeriod.SHORT);
			new PushButton("Cancel").click();
			AbstractWait.sleep(TimePeriod.NORMAL);
			new DefaultShell().close();
			throw ex;
		}
	}

	/**
	 * Karaf cannot be started in debug mode
	 * https://issues.jboss.org/browse/FUSETOOLS-1132
	 * @throws FuseArchetypeNotFoundException 
	 */
	@Test
	public void issue_1132() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-blueprint", "camel-archetype-blueprint");
		new ProjectExplorer().getProject("camel-blueprint").getProjectItem("src/test/java", "com.mycompany.camel.blueprint", "RouteTest.java").delete();
		String server = serverRequirement.getConfig().getName();
		new BreakpointsView().importBreakpoints(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/breakpoint.bkpt"));
		ServerManipulator.debugServer(server);
		ServerManipulator.addModule(server, "camel-blueprint");
		try {	
			new WaitUntil(new ShellWithTextIsAvailable("Confirm Perspective Switch"), TimePeriod.LONG);
			new DefaultShell("Confirm Perspective Switch");
			new CheckBox(0).toggle(true);
			new PushButton("No").click();
			AbstractWait.sleep(TimePeriod.NORMAL);
		} catch (Exception e) {
			fail();
		} finally {
			new WaitWhile(new IsRunning());
			new BreakpointsView().removeAllBreakpoints();
			new WorkbenchShell().setFocus();
			ResumeButton resume = new ResumeButton();
			if (resume.isEnabled()) {
				resume.select();
			}
			ServerManipulator.removeAllModules(server);
		}
	}

	/**
	 * uninstall of bundles from servers broken
	 * https://issues.jboss.org/browse/FUSETOOLS-1152
	 * @throws FuseArchetypeNotFoundException 
	 */
	@Test
	public void issue_1152() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring-dm", "camel-archetype-spring-dm");
		String server = serverRequirement.getConfig().getName();
		ServerManipulator.startServer(server);
		ServerManipulator.addModule(server, "camel-spring-dm");
		AbstractWait.sleep(TimePeriod.NORMAL);
		ServerManipulator.removeAllModules(server);
		new WaitUntil(new FuseLogContainsText("Application context succesfully closed (OsgiBundleXmlApplicationContext(bundle=camel-spring-dm"), TimePeriod.VERY_LONG);
	}

	/**
	 * Problem occurred during restart JBoss Fuse
	 * https://issues.jboss.org/browse/FUSETOOLS-1252
	 * @throws FuseArchetypeNotFoundException 
	 */
	@Test
	public void issue_1252() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-blueprint", "camel-archetype-blueprint");
		String server = serverRequirement.getConfig().getName();
		ServerManipulator.addModule(server, "camel-blueprint");
		ServerManipulator.startServer(server);
		ServerManipulator.restartInDebug(server);
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Problem Occurred"));
			new DefaultShell("Problem Occurred");
			new PushButton("OK");
		} catch (Exception e) {
			// OK no shell "Problem Occurred" was found
			return;
		}
		fail();
	}
}
