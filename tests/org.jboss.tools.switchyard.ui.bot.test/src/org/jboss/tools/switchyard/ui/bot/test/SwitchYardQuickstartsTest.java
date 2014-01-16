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
 * Test importing switchyard quickstarts
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.PRESENT)
@RunWith(SwitchyardSuite.class)
public class SwitchYardQuickstartsTest extends QuickstartsTest {

	public SwitchYardQuickstartsTest() {
		super("quickstarts/switchyard");
	}

	@Test
	public void beanServiceTest() {
		testQuickstart("bean-service");
	}

	@Test
	public void bpelServiceTest() {
		testQuickstart("bpel-service/jms_binding");
		deleteAllProjects();
		testQuickstart("bpel-service/loan_approval");
		deleteAllProjects();
		testQuickstart("bpel-service/say_hello");
		deleteAllProjects();
		testQuickstart("bpel-service/simple_correlation");
		deleteAllProjects();
	}

	@Test
	public void bpmServiceTest() {
		testQuickstart("bpm-service");
	}

	@Test
	public void camelBindingTest() {
		testQuickstart("camel-binding");
	}

	@Test
	public void camelFtpBindingTest() {
		testQuickstart("camel-ftp-binding");
	}

	@Test
	public void camelJaxbTest() {
		testQuickstart("camel-jaxb");
	}

	@Test
	public void camelJmsBindingTest() {
		testQuickstart("camel-jms-binding");
	}

	@Test
	public void camelJpaBindingTest() {
		testQuickstart("camel-jpa-binding");
	}

	@Test
	public void camelMailTest() {
		testQuickstart("camel-mail-binding");
	}

	@Test
	public void camelNettyBindingTest() {
		testQuickstart("camel-netty-binding");
	}

	@Test
	public void camelQuartzBindingTest() {
		testQuickstart("camel-quartz-binding");
	}

	@Test
	public void camelServiceBindingTest() {
		testQuickstart("camel-service");
	}

	@Test
	public void camelSoapProxyTest() {
		testQuickstart("camel-soap-proxy");
	}

	@Test
	public void camelSqlBindingTest() {
		testQuickstart("camel-sql-binding");
	}

	@Test
	public void camelEarDeploymentTest() {
		testQuickstart("ear-deployment");
	}

	@Test
	public void httpBindingTest() {
		testQuickstart("http-binding");
	}

	@Test
	public void jcaInflowHornetqTest() {
		testQuickstart("jca-inflow-hornetq");
	}

	@Test
	public void jcaOutboundHornetqTest() {
		testQuickstart("jca-outbound-hornetq");
	}

	@Test
	public void remoteInvokerTest() {
		testQuickstart("remote-invoker");
	}

	@Test
	public void restBindingTest() {
		testQuickstart("rest-binding");
	}

	@Test
	public void rulesCamelCbrTest() {
		testQuickstart("rules-camel-cbr");
	}

	@Test
	public void rulesInterviewTest() {
		testQuickstart("rules-interview");
	}

	@Test
	public void rulesInterviewContainerTest() {
		testQuickstart("rules-interview-container");
	}

	@Test
	public void rulesInterviewDtableTest() {
		testQuickstart("rules-interview-dtable");
	}

	@Test
	public void soapAddressingTest() {
		testQuickstart("soap-addressing");
	}

	@Test
	public void soapAttachmentTest() {
		testQuickstart("soap-attachment");
	}

	@Test
	public void soapBindingRpcTest() {
		testQuickstart("soap-binding-rpc");
	}
	
	@Test
	public void soapMtomTest() {
		testQuickstart("soap-mtom");
	}

	@Test
	public void transformJaxbTest() {
		testQuickstart("transform-jaxb");
	}

	@Test
	public void transformJsonTest() {
		testQuickstart("transform-json");
	}

	@Test
	public void transformSmooksTest() {
		testQuickstart("transform-smooks");
	}

	@Test
	public void transformXsltTest() {
		testQuickstart("transform-xslt");
	}

	@Test
	public void validateXmlTest() {
		testQuickstart("validate-xml");
	}

}
