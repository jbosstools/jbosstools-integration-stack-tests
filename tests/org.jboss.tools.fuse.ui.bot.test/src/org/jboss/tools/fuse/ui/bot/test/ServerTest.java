package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.test.RedDeerTest;
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
	
	@Test
	public void downloadServerRuntimeTest() {
		
		// TODO Test download runtime feature
	}
}
