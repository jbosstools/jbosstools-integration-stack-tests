package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.RUNNING;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Fuse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.LogGrapper;
import org.jboss.tools.common.reddeer.ResourceHelper;
import org.jboss.tools.common.reddeer.preference.ConsolePreferenceUtil;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.common.reddeer.view.MessagesView;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.requirement.FuseRequirement;
import org.jboss.tools.fuse.reddeer.requirement.FuseRequirement.Fuse;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
import org.jboss.tools.runtime.reddeer.utils.FuseShellSSH;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests covers 'Remote Route Editing' and 'Tracing' features for a project which is deployed on JBoss Fuse Runtime
 * 
 * @author tsedmik
 */
@Fuse(server = @Server(type = Fuse, state = RUNNING))
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class RouteManipulationServerTest {

	@InjectRequirement
	private static FuseRequirement serverRequirement;

	/**
	 * Prepares test environment
	 */
	@BeforeClass
	public static void defaultClassSetup() {
		new WorkbenchShell().maximize();
		ConsolePreferenceUtil.setConsoleOpenOnError(false);
		ConsolePreferenceUtil.setConsoleOpenOnOutput(false);
		new ErrorLogView().selectActivateOnNewEvents(false);
		ProjectFactory.importExistingProject(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				"resources/projects/test-route-manipulation"), "test-route-manipulation", false);
		CamelProject project = new CamelProject("test-route-manipulation");
		project.update();
	}

	@Before
	public void setupImportProject() {
		FuseServerManipulator.addModule(serverRequirement.getConfig().getName(), "test-route-manipulation");
	}

	@After
	public void setupCleanup() {
		try {
			new DefaultEditor(new RegexMatcher("<connected>Remote CamelContext:.*")).close();
		} catch (Exception e) {
			// editor is not opened --> ok
		}
		FuseServerManipulator.publish(serverRequirement.getConfig().getName());
	}

	@AfterClass
	public static void defaultFinalClean() {
		FuseServerManipulator.removeAllModules(serverRequirement.getConfig().getName());
		ConsoleView console = new ConsoleView();
		console.open();
		try {
			console.terminateConsole();
			new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		} catch (CoreLayerException ex) {
		}
		FuseServerManipulator.deleteAllServers();
		FuseServerManipulator.deleteAllServerRuntimes();
		ProjectFactory.deleteAllProjects();
	}

	/**
	 * <p>
	 * Tests Remote Route Editing of deployed camel context on JBoss Fuse in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>import the project 'test-route-manipulation'</li>
	 * <li>deploy the project the JBoss Fuse runtime</li>
	 * <li>open JMX Navigator view</li>
	 * <li>select the node "JBoss Fuse", "Camel", "_context1"</li>
	 * <li>select the context menu option Edit Routes</li>
	 * <li>set focus on the recently opened Camel Editor</li>
	 * <li>select component Log _log1</li>
	 * <li>change property Message to AAA-BBB-CCC</li>
	 * <li>save the editor</li>
	 * <li>check if the Console View contains the text "AAA-BBB-CCC"</li>
	 * <li>activate Camel Editor and switch to Source page</li>
	 * <li>remove otherwise branch</li>
	 * <li>save the editor</li>
	 * <li>open JMX Navigator view</li>
	 * <li>try to select the node "JBoss Fuse", "Camel", "_context1", "Routes", "_route1", "timer:EverySecondTimer",
	 * "Choice", "Otherwise", "Log _log2" (successful)</li>
	 * <li>activate Camel Editor and switch to Source page</li>
	 * <li>remove otherwise branch</li>
	 * <li>open JMX Navigator view</li>
	 * <li>try to select the node "JBoss Fuse", "Camel", "_context1", "Routes", "_route1", "timer:EverySecondTimer",
	 * "Choice", "Otherwise" (unsuccessful)</li>
	 * </ol>
	 */
	@Test
	public void testRemoteRouteEditing() {
		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.refreshLocalProcesses();
		jmx.getNode("JBoss Fuse", "Camel");
		jmx.getNode("JBoss Fuse", "Camel", "_context1").select();
		new ContextMenu("Edit Routes").select();
		CamelEditor editor = new CamelEditor(new DefaultEditor(new RegexMatcher("<connected>Remote CamelContext:.*")).getTitle());
		assertTrue(editor.isComponentAvailable("Log _log1"));
		editor.selectEditPart("Route _route1");
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.selectEditPart("Log _log1");
		editor.setProperty("Message *", "AAA-BBB-CCC");
		editor.save();
		assertTrue(new FuseShellSSH().containsLog("AAA-BBB-CCC"));
		assertNotNull(jmx.getNode("JBoss Fuse", "Camel", "_context1", "Routes", "_route1", "timer:EverySecondTimer", "SetBody _setBody1", "Choice", "Otherwise", "Log _log2"));
		editor.activate();
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/blueprint-route-edit.xml");
		CamelEditor.switchTab("Design");
		jmx.refreshLocalProcesses();
		assertNull(jmx.getNode("JBoss Fuse", "Camel", "_context1", "Routes", "_route1", "timer:EverySecondTimer", "SetBody _setBody1", "Choice", "Otherwise"));
		assertTrue(LogGrapper.getPluginErrors("fuse").size() == 0);
	}

	/**
	 * <p>
	 * Tests Tracing of deployed camel context on JBoss Fuse in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>import the project 'test-route-manipulation'</li>
	 * <li>deploy the project the JBoss Fuse runtime</li>
	 * <li>open JMX Navigator view</li>
	 * <li>select the node "JBoss Fuse", "Camel", "_context1"</li>
	 * <li>select the context menu option Start Tracing</li>
	 * <li>check if the context menu option was changed into Stop Tracing Context</li>
	 * <li>open Message View</li>
	 * <li>in JMX Navigator open "Local Camel Context", "Camel", "_context1"</li>
	 * <li>check if the messages in the Message View corresponds with sent messages</li>
	 * </ol>
	 */
	@Test
	public void testTracing() {
		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.refreshLocalProcesses();
		jmx.getNode("JBoss Fuse", "Camel");
		AbstractWait.sleep(TimePeriod.NORMAL);
		jmx.getNode("JBoss Fuse", "Camel", "_context1").select();
		new ContextMenu("Start Tracing").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		jmx.getNode("JBoss Fuse", "Camel", "_context1").select();
		new ContextMenu("Stop Tracing Context");
		MessagesView msg = new MessagesView();
		msg.open();
		jmx.getNode("JBoss Fuse", "Camel", "_context1").select();
		assertTrue(msg.getAllMessages().size() > 4);
		jmx.getNode("JBoss Fuse", "Camel", "_context1").select();
		new ContextMenu("Stop Tracing Context").select();
		assertTrue(LogGrapper.getPluginErrors("fuse").size() == 0);
	}
}
