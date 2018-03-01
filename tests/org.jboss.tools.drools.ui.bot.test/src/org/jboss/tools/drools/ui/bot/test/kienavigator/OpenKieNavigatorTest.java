package org.jboss.tools.drools.ui.bot.test.kienavigator;

import java.io.IOException;

import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(state = ServerRequirementState.RUNNING)
@RunWith(RedDeerSuite.class)
public class OpenKieNavigatorTest extends KieNavigatorTestParent {

	@InjectRequirement
	private ServerRequirement serverReq;

	@Test
	public void openKieNavigatorTest() throws IOException, InterruptedException { 
		Assert.assertEquals(1, knv.getServers().size());
	}
}
