package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing rtgov quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT) )
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class QuickstartsRTGovTest extends QuickstartsTest {

	public QuickstartsRTGovTest() {
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
