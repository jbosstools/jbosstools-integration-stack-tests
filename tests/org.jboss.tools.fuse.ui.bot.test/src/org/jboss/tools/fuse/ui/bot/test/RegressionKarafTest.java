package org.jboss.tools.fuse.ui.bot.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.tools.fuse.reddeer.perspectives.FuseIntegrationPerspective;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.runtime.reddeer.utils.FuseServerManipulator;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Class contains test cases verifying resolved issues related to Apache Karaf Server
 * 
 * @author tsedmik
 */
@CleanWorkspace
@OpenPerspective(FuseIntegrationPerspective.class)
@RunWith(RedDeerSuite.class)
@Server(type = ServerReqType.Karaf, state = ServerReqState.PRESENT)
public class RegressionKarafTest extends DefaultTest {

	@InjectRequirement
	private ServerRequirement serverRequirement;

	/**
	 * Cleans up test environment
	 */
	@After
	public void setupClean() {

		String server = serverRequirement.getConfig().getName();
		if (FuseServerManipulator.isServerStarted(server)) {
			FuseServerManipulator.stopServer(server);
		}
		new ProjectExplorer().deleteAllProjects();
	}

	/**
	 * <p>JMX Node in servers view doesn't work if server has secured JMX access via credentials</p>
	 * <b>Link: </b><a href="https://issues.jboss.org/browse/FUSETOOLS-1264">https://issues.jboss.org/browse/FUSETOOLS-1264</a>
	 */
	@Test
	public void issue_1264() {

		String server = serverRequirement.getConfig().getName();
		FuseServerManipulator.startServer(server);
		new ServersView().open();
		TreeItem item = new DefaultTree(0).getAllItems().get(0);
		item.expand(TimePeriod.NORMAL);
		item = item.getItems().get(0);
		item.select();
		new ContextMenu("Connect...").select();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		assertTrue(item.getItems().size() != 0);
	}
}
