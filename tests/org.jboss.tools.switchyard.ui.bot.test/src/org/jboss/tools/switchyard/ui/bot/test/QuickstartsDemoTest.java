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
 * Test importing demo quickstarts
 * 
 * @author apodhrad
 * 
 */
@OpenPerspective(JavaEEPerspective.class)
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.PRESENT))
@RunWith(RedDeerSuite.class)
public class QuickstartsDemoTest extends QuickstartsTest {

	public QuickstartsDemoTest() {
		super("quickstarts/switchyard/demos");
	}

	@Test
	public void clusterTest() {
		testQuickstart("cluster");
	}

	@Test
	public void multiAppTest() {
		testQuickstart("multiApp");
	}

	@Test
	public void ordersTest() {
		testQuickstart("orders");
	}

	@Test
	public void policySecurityBasicTest() {
		testQuickstart("policy-security-basic");
	}

	@Test
	public void policySecurityBasicPropagateTest() {
		testQuickstart("policy-security-basic-propagate");
	}

	@Test
	public void policySecurityCertTest() {
		testQuickstart("policy-security-cert");
	}

	@Test
	public void policySecuritySamlTest() {
		testQuickstart("policy-security-saml");
	}

	@Test
	public void policySecurityWssSignEncryptTest() {
		testQuickstart("policy-security-wss-signencrypt");
	}

	@Test
	public void policySecurityWssUsernameTest() {
		testQuickstart("policy-security-wss-username");
	}

	@Test
	public void policyTransactionTest() {
		testQuickstart("policy-transaction");
	}

	@Test
	public void transactionPropagationTest() {
		testQuickstart("transaction-propagation");
	}
}
