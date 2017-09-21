package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.runtime.reddeer.requirement.ServerConnectionType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.PRESENT, connectionType = ServerConnectionType.REMOTE)
@RunWith(RedDeerSuite.class)
public class RemoteServerTest {

	@InjectRequirement
	protected ServerRequirement requirement;

	@Test
	public void remoteServerTest() {
		assertTrue(requirement.getConfig().getServer().isRemote());

		ServersView2 view = new ServersView2();
		view.open();
		org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server server = view.getServer(requirement.getConfig().getName());
		assertEquals(requirement.getConfig().getName(), server.getLabel().getName());
	}

}
