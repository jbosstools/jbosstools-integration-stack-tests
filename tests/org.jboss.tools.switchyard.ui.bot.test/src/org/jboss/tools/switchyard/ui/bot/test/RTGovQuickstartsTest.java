package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing rtgov quickstarts
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.PRESENT)
@RunWith(SwitchyardSuite.class)
public class RTGovQuickstartsTest extends QuickstartsTest {

	public RTGovQuickstartsTest() {
		super("quickstarts/overlord/rtgov");
	}
	
	@Test
	public void activityClientTest() {
		testQuickstart("activityclient");
	}

	@Test
	public void orderMgmtTest() {
		testQuickstart("ordermgmt");
	}

	@Test
	public void policyTest() {
		testQuickstart("policy");
	}

	@Test
	public void slaTest() {
		testQuickstart("sla");
	}
}
