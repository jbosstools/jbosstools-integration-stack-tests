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
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
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

	/**
	 * Prepares test environment
	 */
	@Before
	public void setupCleanUp() {

		FuseServerManipulator.deleteAllServers();
		FuseServerManipulator.deleteAllServerRuntimes();
	}

	/**
	 * <p>Tests adding/modifying/removing a server and a server runtime</p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>add a new server runtime</li>
	 * <li>edit the server runtime (change name)</li>
	 * <li>add a new server</li>
	 * <li>start the server</li>
	 * <li>stop the server</li>
	 * <li>remove the server</li>
	 * <li>remove the server runtime</li>
	 * </ol>
	 */
	@Test
	public void testComplexServer() {

		ServerKaraf fuse = (ServerKaraf) serverRequirement.getConfig().getServerBase();

		FuseServerManipulator.addServerRuntime(fuse.getRuntimeType(), fuse.getHome());
		assertEquals("New server runtime is not listed in Server Runtimes", 1, FuseServerManipulator.getServerRuntimes().size());
		FuseServerManipulator.editServerRuntime(fuse.getRuntimeType(), fuse.getHome());
		FuseServerManipulator.addServer(fuse.getServerType(), fuse.getHost(), fuse.getName(), fuse.getPort(), fuse.getUsername(), fuse.getPassword());
		assertEquals("No server's record is in Servers View", 1, FuseServerManipulator.getServers().size());
		assertTrue("New server is not listed in Servers View", FuseServerManipulator.isServerPresent(fuse.getName()));
		FuseServerManipulator.startServer(fuse.getName());
		assertTrue("Server is not started", FuseServerManipulator.isServerStarted(fuse.getName()));
		assertTrue("There are some errors in error log", getErrorMessages() == 0);
		deleteErrorLog();
		FuseServerManipulator.stopServer(fuse.getName());
		assertFalse("Server is not stopped", FuseServerManipulator.isServerStarted(fuse.getName()));
		assertTrue("There are some errors in error log", getErrorMessages() == 0);
		FuseServerManipulator.removeServer(fuse.getName());
		assertEquals("Server is listed in Servers View after deletion", 0, FuseServerManipulator.getServers().size());
		FuseServerManipulator.removeServerRuntime(fuse.getRuntimeType());
		assertEquals("Server runtime is listed after deletion", 0, FuseServerManipulator.getServerRuntimes().size());
	}
}
