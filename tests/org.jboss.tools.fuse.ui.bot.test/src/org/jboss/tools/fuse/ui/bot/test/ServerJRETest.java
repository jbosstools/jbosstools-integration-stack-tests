package org.jboss.tools.fuse.ui.bot.test;

import static org.jboss.reddeer.requirements.server.ServerReqState.PRESENT;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Fuse;
import static org.jboss.tools.runtime.reddeer.requirement.ServerReqType.Karaf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.fuse.reddeer.requirement.FuseRequirement;
import org.jboss.tools.fuse.reddeer.requirement.FuseRequirement.Fuse;
import org.jboss.tools.runtime.reddeer.impl.ServerKaraf;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests with a different JRE
 * 
 * @author tsedmik
 */
@Fuse(server = @Server(type = { Fuse, Karaf }, state = PRESENT))
@OpenPerspective(FuseIntegrationPerspective.class)
@CleanWorkspace
@RunWith(RedDeerSuite.class)
public class ServerJRETest extends DefaultTest {

	@InjectRequirement
	private FuseRequirement serverRequirement;

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
		FuseServerManipulator.startServer(fuse.getName());
		new ConsoleView().open();
		assertTrue(new DefaultLabel().getText().contains(fuse.getJre()));
	}
}
