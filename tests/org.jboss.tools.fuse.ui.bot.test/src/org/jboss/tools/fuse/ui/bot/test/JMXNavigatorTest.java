package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.view.ErrorLogView;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests <i>JMX Navigator</i> view that:
 * <ul>
 * <li>shows running processes (local context) correctly</li>
 * <li><i>Suspend</i>, <i>Resume</i> options work</li>
 * </ul>
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class JMXNavigatorTest {

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring";
	private static final String PROJECT_NAME = "camel-spring";
	private static final String PROJECT_CAMEL_CONTEXT = "camel-context.xml";

	private static Logger log = Logger.getLogger(JMXNavigatorTest.class);

	@BeforeClass
	public static void createProject() {

		log.info("Create a new Fuse project (" + PROJECT_ARCHETYPE + ")");
		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
	}

	@Before
	public void runCamelContext() {

		Shell workbenchShell = new WorkbenchShell();
		log.info("Run the Fuse project as Local Camel Context");
		new CamelProject(PROJECT_NAME).runCamelContextWithoutTests(PROJECT_CAMEL_CONTEXT);
		new WaitUntil(new ConsoleHasText("Route: route1 started and consuming"), TimePeriod.getCustom(300));
		AbstractWait.sleep(TimePeriod.NORMAL);
		new ErrorLogView().deleteLog();
		workbenchShell.setFocus();
	}

	@After
	public void terminateCamelContext() {

		new ConsoleView().terminateConsole();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@AfterClass
	public static void cleanUp() {

		new ConsoleView().terminateConsole();
		new CamelProject(PROJECT_NAME).deleteProject();
		log.info("Workspace was cleaned.");
	}

	@Test
	public void processesViewTest() {

		JMXNavigator jmx = new JMXNavigator();
		assertNotNull(jmx.getNode("Local Camel Context"));
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camel-1", "Endpoints", "file", "src/data?noop=true"));
		assertNotNull(jmx.getNode("Local Camel Context", "Camel", "camel-1", "Routes", "route1", "file:src/data?noop=true",
				"choice1", "when1", "log1", "to1"));
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}

	@Test
	public void contextOperationsTest() {

		JMXNavigator jmx = new JMXNavigator();
		new JMXNavigator().getNode("Local Camel Context", "Camel", "camel-1").select();
		log.info("Suspend Camel Context");
		new ContextMenu("Suspend Camel Context").select();
		new WaitUntil(new ConsoleHasText("route1 suspend complete"), TimePeriod.NORMAL);
		jmx.open();
		new JMXNavigator().getNode("Local Camel Context", "Camel", "camel-1").select();
		log.info("Resume Camel Context");
		new ContextMenu("Resume Camel Context").select();
		new WaitUntil(new ConsoleHasText("route1 resumed"), TimePeriod.NORMAL);
		assertTrue(new ErrorLogView().getErrorMessages().size() == 0);
	}
}
