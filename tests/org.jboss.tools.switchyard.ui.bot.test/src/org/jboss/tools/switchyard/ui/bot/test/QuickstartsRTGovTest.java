package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.jboss.tools.runtime.reddeer.requirement.ServerImplementationType;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardServerRestriction;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing rtgov quickstarts
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(state = ServerRequirementState.PRESENT)
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class QuickstartsRTGovTest extends QuickstartsTest {

	@RequirementRestriction
	public static RequirementMatcher getRequirementMatcher() {
		return new SwitchYardServerRestriction(ServerImplementationType.ANY);
	}

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
