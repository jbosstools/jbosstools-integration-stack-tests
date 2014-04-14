package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.preference.ServerRuntimePreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement.Server;
import org.jboss.tools.fuse.ui.bot.test.utils.ServerConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests managing a Fuse server
 * 
 * @author tsedmik
 */
@Server
public class ServerTest extends RedDeerTest {
	
	private static final String ADD_BUTTON = "Add...";
	private static final String NEW_WINDOW = "New Server Runtime Environment";
	private static final String SERVER_SECTION = "JBoss Fuse";
	private static final String FINISH_BUTTON = "Finish";
	private static final String CANCEL_BUTTON = "Cancel";
	private static final String PREFERENCES_WINDOW = "Preferences";
	
	private static boolean setUpIsDone = false;
	
	@InjectRequirement
	private ServerRequirement serverRequirement;
	
	@Before
	public void setUp() {
		
		if (setUpIsDone) {
			return;
		}
		
		new ServerConfig(serverRequirement).configureServerEnvironment();
		
		setUpIsDone = true;
	}

	@Test
	public void complexServerTest() {
		
		ServerManipulator.addServerRuntime(serverRequirement.getRuntime(), serverRequirement.getPath());
		assertEquals(1, ServerManipulator.getServerRuntimes().size());
		ServerManipulator.editServerRuntime(serverRequirement.getRuntime(), serverRequirement.getPath());
		ServerManipulator.addServer(serverRequirement.getType(), serverRequirement.getHostname(), serverRequirement.getName(), serverRequirement.getPort(),
				serverRequirement.getUsername(), serverRequirement.getPassword());
		assertEquals(1, ServerManipulator.getServers().size());
		assertTrue(ServerManipulator.isServerPresent(serverRequirement.getName()));
		ServerManipulator.startServer(serverRequirement.getName());
		assertTrue(ServerManipulator.isServerStarted(serverRequirement.getJmxname()));
		ServerManipulator.stopServer(serverRequirement.getName());
		assertFalse(ServerManipulator.isServerStarted(serverRequirement.getJmxname()));
		ServerManipulator.removeServer(serverRequirement.getName());
		assertEquals(0, ServerManipulator.getServers().size());
		ServerManipulator.removeServerRuntime(serverRequirement.getRuntime());
		assertEquals(0, ServerManipulator.getServerRuntimes().size());
	}
	
	/**
	 * Tests the issue: https://issues.jboss.org/browse/ECLIPSE-1067
	 */
	@Test
	public void runtimeManipulationTest() {
		
		new ServerRuntimePreferencePage().open();
		
		new PushButton(ADD_BUTTON).click();
		new DefaultShell(NEW_WINDOW).setFocus();
		new DefaultTreeItem(SERVER_SECTION).expand();
		
		// tests the _Finish_ button
		for (TreeItem item : new DefaultTreeItem(SERVER_SECTION).getItems()) {
			if (!item.getText().startsWith("JBoss")) continue;
			AbstractWait.sleep(TimePeriod.SHORT);
			item.select();
			try {
				
				assertFalse(new PushButton(FINISH_BUTTON).isEnabled());
			} catch (AssertionError ex) {
				
				new DefaultTreeItem(SERVER_SECTION).select();
				AbstractWait.sleep(TimePeriod.SHORT);
				new PushButton(CANCEL_BUTTON).click();
				AbstractWait.sleep(TimePeriod.NORMAL);
				new DefaultShell().close();
				throw ex;
			}
		}
		
		// tests the _Cancel_ button
		AbstractWait.sleep(TimePeriod.SHORT);
		new DefaultTreeItem(SERVER_SECTION, serverRequirement.getRuntime()).select();
		new PushButton(CANCEL_BUTTON).click();
		try {
			
			assertTrue(new DefaultShell().getText().equals(PREFERENCES_WINDOW));
		} catch (AssertionError ex) {
			
			new DefaultShell().close();
			new DefaultTreeItem(SERVER_SECTION).select();
			AbstractWait.sleep(TimePeriod.SHORT);
			new PushButton(CANCEL_BUTTON).click();
			AbstractWait.sleep(TimePeriod.NORMAL);
			new DefaultShell().close();
			throw ex;
		}
	}
	
}
