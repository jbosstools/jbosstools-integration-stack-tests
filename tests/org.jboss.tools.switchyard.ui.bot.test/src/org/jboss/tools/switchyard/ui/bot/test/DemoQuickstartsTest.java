package org.jboss.tools.switchyard.ui.bot.test;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing demo quickstarts
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.PRESENT)
@RunWith(SwitchyardSuite.class)
public class DemoQuickstartsTest extends QuickstartsTest {

	public DemoQuickstartsTest() {
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
