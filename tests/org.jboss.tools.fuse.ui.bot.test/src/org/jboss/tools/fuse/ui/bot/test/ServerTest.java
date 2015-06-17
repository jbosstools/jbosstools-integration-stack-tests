package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests managing a Fuse server
 * 
 * @author tsedmik
 */
@Server(type = {ServerReqType.Fuse, ServerReqType.Karaf, ServerReqType.ServiceMix}, state = ServerReqState.PRESENT)
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class ServerTest extends DefaultTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@Before
	public void cleanUp() {

		ServerManipulator.removeServer(serverRequirement.getConfig().getName());
		ServerManipulator.removeServerRuntime(serverRequirement.getConfig().getName() + " Runtime");
	}

	@Test
	public void complexServerTest() {

		ServerKaraf fuse = (ServerKaraf) serverRequirement.getConfig().getServerBase();

		ServerManipulator.addServerRuntime(fuse.getName(), fuse.getHome());
		assertEquals("New server runtime is not listed in Server Runtimes", 1, ServerManipulator.getServerRuntimes().size());
		ServerManipulator.editServerRuntime(fuse.getName(), fuse.getHome());
		ServerManipulator.addServer(fuse.getServerType(), fuse.getHost(), fuse.getName(), fuse.getPort(), fuse.getUsername(), fuse.getPassword());
		assertEquals("No server's record is in Servers View", 1, ServerManipulator.getServers().size());
		assertTrue("New server is not listed in Servers View", ServerManipulator.isServerPresent(fuse.getName()));
		ServerManipulator.startServer(fuse.getName());
		assertTrue("Server is not started", ServerManipulator.isServerStarted(fuse.getName()));
		assertTrue("There are some errors in error log", getErrorMessages() == 0);
		deleteErrorLog();
		ServerManipulator.stopServer(fuse.getName());
		assertFalse("Server is not stopped", ServerManipulator.isServerStarted(fuse.getName()));
		assertTrue("There are some errors in error log", getErrorMessages() == 0);
		ServerManipulator.removeServer(fuse.getName());
		assertEquals("Server is listed in Servers View after deletion", 0, ServerManipulator.getServers().size());
		ServerManipulator.removeServerRuntime(fuse.getName());
		assertEquals("Server runtime is listed after deletion", 0, ServerManipulator.getServerRuntimes().size());
	}
}
