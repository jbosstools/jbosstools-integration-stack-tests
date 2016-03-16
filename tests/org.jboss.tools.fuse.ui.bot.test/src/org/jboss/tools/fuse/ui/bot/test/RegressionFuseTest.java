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
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.condition.FuseLogContainsText;
import org.jboss.tools.fuse.reddeer.debug.BreakpointsView;
import org.jboss.tools.fuse.reddeer.debug.IsRunning;
import org.jboss.tools.fuse.reddeer.debug.ResumeButton;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.preference.FuseServerRuntimePreferencePage;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
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

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupClean() {

		String server = serverRequirement.getConfig().getName();
		if (FuseServerManipulator.isServerStarted(server)) {
			FuseServerManipulator.stopServer(server);
		}
		new ProjectExplorer().deleteAllProjects();
	}

	/**
	 * <p>
	 * New Server Runtime Wizard - Cancel/Finish button error
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1067">https://issues.jboss.org/browse/FUSETOOLS-1067</a>
	 */
	@Test
	public void issue_1067() {

		new FuseServerRuntimePreferencePage().open();

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
		new DefaultTreeItem("JBoss Fuse", "JBoss Fuse " + serverRequirement.getConfig().getServerBase().getVersion()).select();
		AbstractWait.sleep(TimePeriod.SHORT);
		new PushButton("Cancel").click();
		AbstractWait.sleep(TimePeriod.SHORT);
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
	 * <p>
	 * Karaf cannot be started in debug mode
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1132">https://issues.jboss.org/browse/FUSETOOLS-1132</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1132() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-blueprint", "camel-archetype-blueprint");
		new ProjectExplorer().getProject("camel-blueprint")
				.getProjectItem("src/test/java", "com.mycompany.camel.blueprint", "RouteTest.java").delete();
		String server = serverRequirement.getConfig().getName();
		new BreakpointsView().importBreakpoints(
				ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/breakpoint.bkpt"));
		FuseServerManipulator.debugServer(server);
		FuseServerManipulator.addModule(server, "camel-blueprint");
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
			FuseServerManipulator.removeAllModules(server);
		}
	}

	/**
	 * <p>
	 * uninstall of bundles from servers broken
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1152">https://issues.jboss.org/browse/FUSETOOLS-1152</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1152() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring-dm", "camel-archetype-spring-dm");
		String server = serverRequirement.getConfig().getName();
		FuseServerManipulator.startServer(server);
		FuseServerManipulator.addModule(server, "camel-spring-dm");
		AbstractWait.sleep(TimePeriod.NORMAL);
		FuseServerManipulator.removeAllModules(server);
		new WaitUntil(
				new FuseLogContainsText(
						"Application context succesfully closed (OsgiBundleXmlApplicationContext(bundle=camel-archetype-spring-dm"),
				TimePeriod.VERY_LONG);
	}

	/**
	 * <p>
	 * Problem occurred during restart JBoss Fuse
	 * </p>
	 * <b>Link: </b>
	 * <a href="https://issues.jboss.org/browse/FUSETOOLS-1252">https://issues.jboss.org/browse/FUSETOOLS-1252</a>
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Test
	public void issue_1252() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-blueprint", "camel-archetype-blueprint");
		String server = serverRequirement.getConfig().getName();
		FuseServerManipulator.addModule(server, "camel-blueprint");
		FuseServerManipulator.startServer(server);
		FuseServerManipulator.restartInDebug(server);
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
