package org.jboss.tools.runtime.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT)
@RunWith(RedDeerSuite.class)
public class ServerPresentTest {

	@InjectRequirement
	protected ServerRequirement requirement;

	@Test
	public void serverPresentTest() {
		ServersView view = new ServersView();
		view.open();
		org.jboss.reddeer.eclipse.wst.server.ui.view.Server server = view.getServer(requirement.getConfig().getName());
		assertEquals(requirement.getConfig().getName(), server.getLabel().getName());
	}

}
