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
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests with a different JRE
 * 
 * @author tsedmik
 */
@Server(type = { ServerReqType.Fuse, ServerReqType.Karaf, ServerReqType.ServiceMix }, state = ServerReqState.PRESENT)
@OpenPerspective(FuseIntegrationPerspective.class)
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class ServerJRETest extends DefaultTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

	/**
	 * <p>
	 * Tries to run JBoss Fuse server with different JRE than runs JBDS
	 * </p>
	 * <b>Steps:</b>
	 * <ol>
	 * <li>add new JRE different from that runs JBDS</li>
	 * <li>add new JBoss Fuse Server Runtime with different JRE</li>
	 * <li>start the server</li>
	 * <li>check Console View whether the server starting with the selected JRE</li>
	 * </ol>
	 */
	@Test
	public void testJRE() {

		ServerKaraf fuse = (ServerKaraf) serverRequirement.getConfig().getServerBase();
		assertNotNull("Different JRE is not specified!", fuse.getJre());
		ServerManipulator.startServer(fuse.getName());
		new ConsoleView().open();
		assertTrue(new DefaultLabel().getText().contains(fuse.getJre()));
	}
}
