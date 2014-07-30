package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.preference.DeployFolderPreferencePage;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.Fabric8Explorer;
import org.jboss.tools.fuse.reddeer.view.FuseShell;
import org.jboss.tools.fuse.reddeer.view.JMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests deployment project via:
 * <ul>
 * <li>Deploy Folder mechanism</li>
 * <li>JMX</li>
 * <li>Fabric Profile</li>
 * </ul>
 * 
 * @author tsedmik
 */
@Server(type = ServerReqType.Fuse, state = ServerReqState.RUNNING)
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
public class DeploymentTest {

	private static String os = System.getProperty("os.name").toLowerCase();

	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring";
	private static final String PROJECT_NAME = "camel-spring";
	private static final String PROJECT_JAR_ARCHIVE = "camel-spring-1.0.0-SNAPSHOT.jar";
	private static final String PROJECT_FABS = "mvn:com.mycompany/camel-spring/1.0.0-SNAPSHOT";

	private static final String PROJECT_ARCHETYPE2 = "camel-archetype-spring-dm";
	private static final String PROJECT_NAME2 = "camel-spring-dm";

	private static final String CONTEXT_DEPLOY_TO = "Deploy to...";
	private static final String DEPLOY_FOLDER_NAME = "Fuse Deploy Folder";

	private String JMX_JBOSS_FUSE = "karaf";
	private static final String JMX_CAMEL = "Camel";
	private static final String JMX_CAMEL2 = "camel-";

	private static boolean setUpIsDone = false;

	@InjectRequirement
	private static ServerRequirement serverRequirement;

	@Before
	public void setUp() {

		if (setUpIsDone) {
			return;
		}

		ProjectFactory.createProject(PROJECT_NAME, PROJECT_ARCHETYPE);
		ProjectFactory.createProject(PROJECT_NAME2, PROJECT_ARCHETYPE2);
		new WorkbenchShell().setFocus();
		new WorkbenchShell().maximize();

		setUpIsDone = true;
	}

	@After
	public void closeContext() {

		TreeItem item = getJMXNode(JMX_JBOSS_FUSE, JMX_CAMEL, JMX_CAMEL2);
		if (item != null) {
			item.select();
			new ContextMenu("Close Camel Context").select();
		}
	}

	@AfterClass
	public static void cleanUp() {

		ServerManipulator.stopServer(serverRequirement.getConfig().getName());
		ServerManipulator.removeServer(serverRequirement.getConfig().getName());
		ServerManipulator.removeServerRuntime(serverRequirement.getConfig().getName());
		new CamelProject(PROJECT_NAME).deleteProject();
		new CamelProject(PROJECT_NAME2).deleteProject();
	}

	@Test
	public void deployWithDeployFolderTest() {

		setupDeployFolder(DEPLOY_FOLDER_NAME, serverRequirement.getConfig().getServerBase().getHome() + "/deploy/",
				"Some description");
		deployProject(PROJECT_NAME, DEPLOY_FOLDER_NAME);

		assertTrue(new ConsoleView().getConsoleText().contains("BUILD SUCCESS"));
		File jarArchive = new File(serverRequirement.getConfig().getServerBase().getHome() + "/deploy/"
				+ PROJECT_JAR_ARCHIVE);
		assertTrue(jarArchive.exists());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deployWithJMXTest() {

		new JMXNavigator().connectTo(JMX_JBOSS_FUSE);
		new ProjectExplorer().getProject(PROJECT_NAME2).select();
		Matcher<String> deployTo = new WithTextMatcher(CONTEXT_DEPLOY_TO);
		Matcher<String> fuse = new RegexMatcher("JBoss Fuse.*");
		new ContextMenu(deployTo, fuse).select();
		new WaitUntil(new ConsoleHasText("BUILD SUCCESS"), TimePeriod.getCustom(300));

		assertNotNull(getJMXNode(JMX_JBOSS_FUSE, JMX_CAMEL, JMX_CAMEL2));
	}

	@Test
	public void deployWithFabric() {

		new FuseShell().createFabric();

		Fabric8Explorer fab = new Fabric8Explorer();
		fab.open();
		fab.addFabricDetails(null, null, "admin", "admin", "admin");
		fab.refresh();
		fab.connectToFabric(null);
		fab.createProfile("test", "1.0", "default");
		fab.deployProjectToProfile(PROJECT_NAME, "test");

		assertEquals(PROJECT_FABS, fab.getProfileFABs("Fabrics", "Local Fabric", "Versions", "1.0", "default", "test"));

		fab.open();
		fab.createContainer("testContainer", "1.0", "test");
		assertTrue(new FuseShell().containsLog("testContainer has been successfully created"));

		fab.open();
		fab.removeFabric(null);
	}

	/**
	 * Create a new record in the <i>Fuse Tooling --> Deploy Folders</i>
	 * Preference page.
	 * 
	 * @param name
	 *            name of the deploy folder record
	 * @param path
	 *            path to the JBoss Fuse server deploy folder
	 * @param description
	 *            description of the deploy folder record
	 */
	private void setupDeployFolder(String name, String path, String description) {

		DeployFolderPreferencePage deployPrefPage = new DeployFolderPreferencePage();
		deployPrefPage.open();
		deployPrefPage.addDeployFolder(name, path, description);
		deployPrefPage.ok();
	}

	/**
	 * Deploy a project on given JBoss Fuse server deploy folder.
	 * 
	 * @param name
	 *            The project's name in <i>Project Explorer</i> view.
	 * @param deployFolder
	 *            The name of deploy folder defined in <i>Fuse Tooling -->
	 *            Deploy Folders</i> Preference page.
	 */
	private void deployProject(String name, String deployFolder) {

		new ProjectExplorer().getProject(name).select();
		new ContextMenu(CONTEXT_DEPLOY_TO, deployFolder).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("BUILD"), TimePeriod.getCustom(300));
	}

	private TreeItem getJMXNode(String... path) {

		JMXNavigator jmx = new JMXNavigator();
		jmx.open();
		jmx.connectTo(JMX_JBOSS_FUSE);
		AbstractWait.sleep(TimePeriod.getCustom(2));
		return jmx.getNode(path);
	}
}
