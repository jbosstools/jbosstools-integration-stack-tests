package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.common.reddeer.view.ErrorLogView;
import org.jboss.tools.common.reddeer.view.MessagesView;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.utils.TracingDragAndDropManager;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.FuseArchetypeNotFoundException;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests covers 'Remote Route Editing' feature of the JBoss Fuse Tooling
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class RouteManipulationTest extends DefaultTest {

	/**
	 * Prepares test environment
	 * 
	 * @throws FuseArchetypeNotFoundException
	 *             Fuse archetype was not found. Tests cannot be executed!
	 */
	@Before
	public void setupCreateAndRunCamelProject() throws FuseArchetypeNotFoundException {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		Shell workbenchShell = new WorkbenchShell();
		new CamelProject("camel-spring").runCamelContextWithoutTests("camel-context.xml");
		new WaitUntil(new ConsoleHasText("Route: _route1 started and consuming"), TimePeriod.getCustom(300));
		AbstractWait.sleep(TimePeriod.NORMAL);
		workbenchShell.setFocus();
		new ErrorLogView().deleteLog();
	}

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupDeleteProjects() {

		new WorkbenchShell();
		new ProjectExplorer().deleteAllProjects();
	}

	/**
	 * <p>
	 * Tests Remote Route Editing of running camel context in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>Run a Project as Local Camel Context without tests</li>
	 * <li>open JMX Navigator view</li>
	 * <li>select the node "Local Camel Context", "Camel", "camelContext..."</li>
	 * <li>select the context menu option Edit Routes</li>
	 * <li>set focus on the recently opened Camel Editor</li>
	 * <li>select component Log _log1</li>
	 * <li>change property Message to XXX</li>
	 * <li>save the editor</li>
	 * <li>check if the Console View contains the text Route: _route1 is stopped, was consuming from:
	 * Endpoint[file://src/data?noop=true]</li>
	 * <li>check if the Console View contains the text Route: _route1 started and consuming from:
	 * Endpoint[file://src/data?noop=true]</li>
	 * <li>check if the Console View contains the text file://src/data] _route1 INFO XXX</li>
	 * <li>activate Camel Editor and switch to Source page</li>
	 * <li>remove otherwise branch</li>
	 * <li>change attribute message to YYY</li>
	 * <li>save the editor</li>
	 * <li>check if the Console View contains the text file://src/data] _route1 INFO YYY</li>
	 * <li>open JMX Navigator view</li>
	 * <li>try to select the node "Local Camel Context", "Camel", "camelContext", "Routes", "_route1",
	 * "file:src/data?noop=true", "Choice", "When /person/city = 'London'", "Log _log1", "file:target/messages/uk"
	 * (successful)</li>
	 * <li>try to select the node "Local Camel Context", "Camel", "camelContext", "Routes", "_route1",
	 * "file:src/data?noop=true", "Choice", "Otherwise"" (unsuccessful)</li>
	 * </ol>
	 */
	@Test
	public void testRemoteRouteEditing() {

		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.getNode("Local Camel Context", "Camel");
		AbstractWait.sleep(TimePeriod.NORMAL);
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camelContext", "Routes", "_route1",
				"file:src/data?noop=true", "Choice", "Otherwise"));
		jmx.getNode("Local Camel Context", "Camel", "camelContext").select();
		new ContextMenu("Edit Routes").select();
		CamelEditor editor = new CamelEditor(new DefaultEditor(new RegexMatcher("Remote CamelContext:.*")).getTitle());
		assertTrue(editor.isComponentAvailable("Log _log1"));
		editor.selectEditPart("Route _route1");
		AbstractWait.sleep(TimePeriod.SHORT);
		editor.selectEditPart("Log _log1");
		editor.setProperty("Message *", "XXX");
		editor.save();
		new WaitUntil(new ConsoleHasText(
				"Route: _route1 is stopped, was consuming from: Endpoint[file://src/data?noop=true]"));
		new WaitUntil(
				new ConsoleHasText("Route: _route1 started and consuming from: Endpoint[file://src/data?noop=true]"));
		new WaitUntil(new ConsoleHasText("file://src/data] _route1                        INFO  XXX"));
		editor.activate();
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-route-edit.xml");
		CamelEditor.switchTab("Design");
		new WaitUntil(new ConsoleHasText("file://src/data] _route1                        INFO  YYY"));
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camelContext", "Routes", "_route1",
				"file:src/data?noop=true", "Choice", "When /person/city = 'London'", "Log _log1",
				"file:target/messages/uk"));
		assertNull(jmx.getNode("Local Camel Context", "Camel", "camelContext", "Routes", "_route1",
				"file:src/data?noop=true", "Choice", "Otherwise"));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	/**
	 * <p>
	 * Tests Tracing of running camel context in JMX Navigator view.
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>create a new project with camel-archetype-spring archetype</li>
	 * <li>Run a Project as Local Camel Context without tests</li>
	 * <li>open JMX Navigator view</li>
	 * <li>select the node "Local Camel Context", "Camel", "camel-1"</li>
	 * <li>select the context menu option Start Tracing</li>
	 * <li>check if the context menu option was changed into Stop Tracing Context</li>
	 * <li>in Project Explorer open "camel-spring", "src", "data"</li>
	 * <li>in JMX Navigator open "Local Camel Context", "Camel", "camel-1", "Endpoints", "file"</li>
	 * <li>perform drag&drop message1.xml from Project Explorer to src/data?noop=true in JMX Navigator</li>
	 * <li>perform drag&drop message2.xml from Project Explorer to src/data?noop=true in JMX Navigator</li>
	 * <li>open Message View</li>
	 * <li>in JMX Navigator open "Local Camel Context", "Camel", "camel-1"</li>
	 * <li>check if the messages in the Message View corresponds with sent messages</li>
	 * </ol>
	 */
	@Test
	public void testTracing() {

		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.getNode("Local Camel Context", "Camel");
		AbstractWait.sleep(TimePeriod.NORMAL);
		jmx.getNode("Local Camel Context", "Camel", "camelContext").select();
		new ContextMenu("Start Tracing").select();
		AbstractWait.sleep(TimePeriod.SHORT);
		jmx.getNode("Local Camel Context", "Camel", "camelContext").select();
		new ContextMenu("Stop Tracing Context");

		String[] from = { "camel-spring", "src", "data", "message1.xml" };
		String[] from2 = { "camel-spring", "src", "data", "message2.xml" };
		String[] to = { "Local Camel Context", "Camel", "camelContext", "Endpoints", "file", "src/data?noop=true" };
		MessagesView msg = new MessagesView();
		msg.open();
		jmx.refreshNode("Local Camel Context", "Camel", "camelContext");
		jmx.getNode("Local Camel Context", "Camel", "camelContext", "Endpoints", "file", "target/messages/others")
				.select();
		new TracingDragAndDropManager(from, to).performDragAndDrop();
		new WaitUntil(
				new ConsoleHasText("thread #2 - file://src/data] _route1                        INFO  UK message"),
				TimePeriod.getCustom(60));
		jmx.getNode("Local Camel Context", "Camel", "camelContext", "Endpoints", "file", "target/messages/others")
				.select();
		new TracingDragAndDropManager(from2, to).performDragAndDrop();

		msg = new MessagesView();
		msg.open();
		jmx.getNode("Local Camel Context", "Camel", "camelContext").select();
		assertEquals(8, msg.getAllMessages().size());
		assertEquals("_choice1", msg.getMessage(2).getTraceNode());
		assertEquals("_log1", msg.getMessage(3).getTraceNode());
		assertEquals("_to1", msg.getMessage(4).getTraceNode());
		assertEquals("_choice1", msg.getMessage(6).getTraceNode());
		assertEquals("_log2", msg.getMessage(7).getTraceNode());
		assertEquals("_to2", msg.getMessage(8).getTraceNode());
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}
}
