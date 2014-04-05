package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.hamcrest.Matcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.matcher.WithRegexMatcher;
import org.jboss.reddeer.swt.matcher.WithTextMatcher;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.fuse.reddeer.preference.DeployFolderPreferencePage;
import org.jboss.tools.fuse.reddeer.projectexplorer.CamelProject;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.view.FuseJMXNavigator;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.fuse.ui.bot.test.utils.ProjectFactory;
import org.jboss.tools.fuse.ui.bot.test.utils.ServerConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

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
@Server
public class DeploymentTest extends RedDeerTest {
	
	private static final String PROJECT_ARCHETYPE = "camel-archetype-spring";
	private static final String PROJECT_NAME = "camel-spring";
	private static final String PROJECT_JAR_ARCHIVE = "camel-spring-1.0.0-SNAPSHOT.jar";

	private static final String PROJECT_ARCHETYPE2 = "camel-archetype-spring-dm";
	private static final String PROJECT_NAME2 = "camel-spring-dm";

	private static final String CONTEXT_DEPLOY_TO = "Deploy to...";
	private static final String DEPLOY_FOLDER_NAME = "Fuse Deploy Folder";
	
	private static final String JMX_JBOSS_FUSE = "JBoss Fuse";
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
		
		new ServerConfig(serverRequirement).configureNewServer();
		ServerManipulator.startServer(serverRequirement.getName());
		ProjectFactory.createProject(PROJECT_ARCHETYPE);
		ProjectFactory.createProject(PROJECT_ARCHETYPE2);
		
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
		
		ServerManipulator.stopServer(serverRequirement.getName());
		new CamelProject(PROJECT_NAME).deleteProject();
		new CamelProject(PROJECT_NAME2).deleteProject();
	}
	
	@Test
	public void deployWithDeployFolderTest() {
		
		setupDeployFolder(DEPLOY_FOLDER_NAME, serverRequirement.getPath() + "/deploy/", "Some description");
		deployProject(PROJECT_NAME, DEPLOY_FOLDER_NAME);
		
		assertTrue(new ConsoleView().getConsoleText().contains("BUILD SUCCESS"));
		File jarArchive = new File(serverRequirement.getPath() + "/deploy/" + PROJECT_JAR_ARCHIVE);
		assertTrue(jarArchive.exists());
		
		assertNotNull(getJMXNode(JMX_JBOSS_FUSE, JMX_CAMEL, JMX_CAMEL2));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deployWithJMXTest() {
		
		new ProjectExplorer().getProject(PROJECT_NAME2).select();
		Matcher<String> deployTo = new WithTextMatcher(CONTEXT_DEPLOY_TO);
		Matcher<String> fuse = new WithRegexMatcher("JBoss Fuse.*");
		new ContextMenu(deployTo, fuse).select();
		new WaitUntil(new ConsoleHasText("BUILD SUCCESS"), TimePeriod.getCustom(300));
		
		assertNotNull(getJMXNode(JMX_JBOSS_FUSE, JMX_CAMEL, JMX_CAMEL2));
	}
	
	@Test
	public void deployWithFabric() {
		
		// TODO Not implemented yet!
	}
	
	/**
	 * Create a new record in the <i>Fuse Tooling --> Deploy Folders</i> Preference page.
	 * 
	 * @param name name of the deploy folder record
	 * @param path path to the JBoss Fuse server deploy folder
	 * @param description description of the deploy folder record
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
	 * @param name The project's name in <i>Project Explorer</i> view.
	 * @param deployFolder The name of deploy folder defined in <i>Fuse Tooling --> Deploy Folders</i>
	 * Preference page.
	 */
	private void deployProject(String name, String deployFolder) {
		
		new ProjectExplorer().getProject(name).select();
		new ContextMenu(CONTEXT_DEPLOY_TO, deployFolder).select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("BUILD"), TimePeriod.getCustom(300));
	}
	
	private TreeItem getJMXNode(String... path) {
		
		FuseJMXNavigator jmx = new FuseJMXNavigator();
		jmx.open();
		jmx.connectTo(JMX_JBOSS_FUSE);
		AbstractWait.sleep(TimePeriod.SHORT);
		return jmx.getNode(path);
	}
}
