package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.PRESENT)
@RunWith(RedDeerSuite.class)
public class ServerPresentTest {

	@InjectRequirement
	protected ServerRequirement requirement;

	@Test
	public void serverPresentTest() {
		ServersView2 view = new ServersView2();
		view.open();
		org.eclipse.reddeer.eclipse.wst.server.ui.cnf.Server server = view.getServer(requirement.getConfig().getName());
		assertEquals(requirement.getConfig().getName(), server.getLabel().getName());
	}

}
