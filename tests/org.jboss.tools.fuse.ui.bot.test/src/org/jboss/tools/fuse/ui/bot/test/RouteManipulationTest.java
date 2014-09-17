package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.editor.CamelEditor;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.utils.TracingDragAndDropManager;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.reddeer.view.MessagesView;
import org.jboss.tools.fuse.ui.bot.test.utils.EditorManipulator;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class RouteManipulationTest {

	@BeforeClass
	public static void setup() {

		new WorkbenchShell().maximize();
	}

	@Before
	public void createAndRunCamelProject() {

		ProjectFactory.createProject("camel-spring", "camel-archetype-spring");
		Shell workbenchShell = new WorkbenchShell();
		new CamelProject("camel-spring").runCamelContextWithoutTests("camel-context.xml");
		new WaitUntil(new ConsoleHasText("Route: route1 started and consuming"), TimePeriod.getCustom(300));
		AbstractWait.sleep(TimePeriod.NORMAL);
		workbenchShell.setFocus();
	}

	@After
	public void deleteProjects() {

		new ConsoleView().terminateConsole();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultToolItem(new WorkbenchShell(), 0, new WithTooltipTextMatcher(new RegexMatcher("Save All.*")))
				.click();
		new ProjectExplorer().deleteAllProjects();
	}

	@Test
	public void testRemoteRouteEditing() {

		JMXNavigator jmx = new JMXNavigator();
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camel-1", "Routes", "route1", "file:src/data?noop=true", "choice1", "otherwise1"));
		jmx.getNode("Local Camel Context", "Camel", "camel-1").select();
		new ContextMenu("Edit Routes").select();
		CamelEditor editor = new CamelEditor("CamelContext: camel-1");
		editor.isComponentAvailable("log1");
		editor.setProperty("Message", "XXX");
		editor.save();
		new WaitUntil(new ConsoleHasText("Route: route1 is stopped, was consuming from: Endpoint[file://src/data?noop=true]"));
		new WaitUntil(new ConsoleHasText("Route: route1 started and consuming from: Endpoint[file://src/data?noop=true]"));
		new WaitUntil(new ConsoleHasText("file://src/data] route1                         INFO  XXX"));
		editor.activate();
		CamelEditor.switchTab("Source");
		EditorManipulator.copyFileContentToCamelXMLEditor("resources/camel-context-route-edit.xml");
		CamelEditor.switchTab("Design");
		editor.save();
		new WaitUntil(new ConsoleHasText("file://src/data] route1                         INFO  YYY"));
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camel-1", "Routes", "route1", "file:src/data?noop=true", "choice1", "choice1", "log1", "to1"));
		assertNull(jmx.getNode("Local Camel Context", "Camel", "camel-1", "Routes", "route1", "file:src/data?noop=true", "choice1", "otherwise"));
	}

	@Test
	public void testTracing() {

		JMXNavigator jmx = new JMXNavigator();
		jmx.getNode("Local Camel Context", "Camel", "camel-1").select();
		new ContextMenu("Start Tracing").select();
		jmx.getNode("Local Camel Context", "Camel", "camel-1").select();
		new ContextMenu("Stop Tracing Context");

		String[] from = {"camel-spring", "src", "data", "message1.xml"};
		String[] from2 = {"camel-spring", "src", "data", "message2.xml"};
		String[] to = {"Local Camel Context", "Camel", "camel-1", "Endpoints", "file", "src/data?noop=true"};
		new TracingDragAndDropManager(from, to).performDragAndDrop();
		new WaitUntil(new ConsoleHasText("INFO  Other message\n[1) thread #2 - file://src/data] route1                         INFO  UK message"));
		new TracingDragAndDropManager(from2, to).performDragAndDrop();

		MessagesView msg = new MessagesView();
		msg.open();
		jmx.getNode("Local Camel Context", "Camel", "camel-1").select();
		assertEquals(8, msg.getAllMessages().size());
		assertEquals("choice1", msg.getMessage(2).getTraceNode());
		assertEquals("log2", msg.getMessage(3).getTraceNode());
		assertEquals("to2", msg.getMessage(4).getTraceNode());
		assertEquals("choice1", msg.getMessage(6).getTraceNode());
		assertEquals("log1", msg.getMessage(7).getTraceNode());
		assertEquals("to1", msg.getMessage(8).getTraceNode());
	}
}
