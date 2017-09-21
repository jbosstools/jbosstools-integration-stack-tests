package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.fuse.reddeer.requirement.FuseRequirement.Fuse;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test importing demo quickstarts
 * 
 * @author apodhrad
 * 
 */
@Fuse
@SwitchYard
@OpenPerspective(JavaEEPerspective.class)
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
