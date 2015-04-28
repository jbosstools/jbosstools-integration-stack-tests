package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.server.ServerManipulator;
import org.jboss.tools.runtime.reddeer.impl.ServerFuse;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

@Server(type = ServerReqType.Fuse, state = ServerReqState.PRESENT)
@OpenPerspective(FuseIntegrationPerspective.class)
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class ServerJRETest extends DefaultTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

	@Test
	public void testJRE() {

		ServerFuse fuse = (ServerFuse) serverRequirement.getConfig().getServerBase();
		assertNotNull("Different JRE is not specified!", fuse.getJre());
		ServerManipulator.startServer(fuse.getName());
		new ConsoleView().activate();
		assertTrue(new DefaultLabel().getText().contains(fuse.getJre()));
	}
}
