package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.fuse.reddeer.preference.SSH2PreferencePage;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.fuse.reddeer.utils.ResourceHelper;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement;
import org.jboss.tools.fuse.ui.bot.test.requirement.ServerRequirement.Server;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests managing a Fuse server
 * 
 * @author tsedmik
 */
@Server
public class ServerTest extends RedDeerTest {
	
	@InjectRequirement
	private ServerRequirement serverRequirement;
	
	@Before
	public void setSSHHome() {
		
		SSH2PreferencePage sshPreferencePage = new SSH2PreferencePage();
		sshPreferencePage.open();
		sshPreferencePage.setSSH2Home(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/.ssh"));
		sshPreferencePage.ok();
	}

	@Test
	public void complexServerTest() {
		
		ServerManipulator.addServerRuntime(serverRequirement.getRuntime(), ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, serverRequirement.getPath()));
		assertEquals(1, ServerManipulator.getServerRuntimes().size());
		ServerManipulator.editServerRuntime(serverRequirement.getRuntime(), ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, serverRequirement.getPath()));
		ServerManipulator.addServer(serverRequirement.getName(), serverRequirement.getName(), serverRequirement.getPort(),
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
		
		// TODO Test download runtime feature
		
	}
}
