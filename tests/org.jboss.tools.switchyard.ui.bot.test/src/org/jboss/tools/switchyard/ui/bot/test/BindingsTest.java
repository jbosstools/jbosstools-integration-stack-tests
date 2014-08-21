package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.CamelBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FileBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JPABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MailBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyTCPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyUDPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RESTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SFTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SQLBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SchedulingBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.PropertiesPreferencePage;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.DefaultServiceWizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for creating various bindings
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class BindingsTest {

	public static final String CONTEXT_PATH = "Context Path";

	public static final String PROJECT = "binding_project";
	public static final String SERVICE = "HelloService";
	public static final String OPERATION = "sayHello";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT;
	public static final String[] GATEWAY_BINDINGS = new String[] { "Camel Core (SEDA/Timer/URI)", "File",
			"File Transfer (FTP/FTPS/SFTP)", "HTTP", "JCA", "JMS", "JPA", "Mail", "Network (TCP/UDP)", "REST", "SCA",
			"Scheduling", "SOAP", "SQL" };
	public static final String[] BINDINGS = new String[] { "Camel", "FTP", "FTPS", "File", "HTTP", "JCA", "JMS", "JPA",
			"Mail", "Netty TCP", "Netty UDP", "REST", "SCA", "SFTP", "SOAP", "SQL", "Scheduling" };

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	@BeforeClass
	public static void createProject() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		
		switchyardRequirement.project(PROJECT).binding(GATEWAY_BINDINGS).create();

		// Sometimes the editor is not displayed properly, this happens only
		// when the project is created by bot
		new SwitchYardEditor().close();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("SwitchYard").open();
		new ProjectExplorer().getProject(PROJECT).select();

		// Create new interface
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();
		wizard.getFirstPage().setName("Hello");
		wizard.finish();

		TextEditor textEditor = new TextEditor("Hello.java");
		textEditor.setText("package com.example.switchyard.binding_project;\n" + "import javax.ws.rs.Produces;"
				+ "import javax.ws.rs.GET;\n" + "import javax.ws.rs.Path;\n" + "import javax.ws.rs.PathParam;\n"
				+ "public interface Hello {\n" + "@GET()\n" + "@Path(\"/{name}\")\n" + "@Produces(\"text/plain\")\n"
				+ "String sayHello(@PathParam(\"name\") String name);\n}");
		textEditor.save();
		textEditor.close();
	}

	@AfterClass
	public static void deleteProject() {
		new WorkbenchShell();
		new SwitchYardEditor().saveAndClose();
		new ProjectExplorer().getProject(PROJECT).delete(true);
	}

	@Before
	public void addService() {
		new SwitchYardEditor().addComponent("Service");
		new DefaultShell("New Service");
		new SWTWorkbenchBot().shell("New Service").activate();
		new DefaultServiceWizard().selectJavaInterface("Hello").setServiceName("HelloService").finish();
		new SwitchYardEditor().save();
	}

	@After
	public void deleteService() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new Service("HelloService").delete();
		new SwitchYardEditor().save();
	}

	@Test
	public void camelBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Camel");
		BindingWizard<CamelBindingPage> wizard = BindingWizard.createCamelBindingWizard();
		assertEquals("camel1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("camel-binding");
		wizard.getBindingPage().setConfigURI("camel-uri");
		wizard.getBindingPage().setOperation(OPERATION);
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("camel-binding", properties.getCamelBindingPage().getName());
		assertEquals("camel-uri", properties.getCamelBindingPage().getConfigURI());
		assertEquals(OPERATION, properties.getHTTPBindingPage().getOperation());
		properties.ok();
	}

	@Test
	public void ftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTP");
		BindingWizard<FTPBindingPage> wizard = BindingWizard.createFTPBindingWizard();
		assertEquals("ftp1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("ftp-binding");
		wizard.getBindingPage().setDirectory("ftp-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("ftp-binding", properties.getCamelBindingPage().getName());
		assertEquals("ftp-directory", properties.getFTPBindingPage().getDirectory());
		properties.ok();
	}

	@Test
	public void ftpsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTPS");
		BindingWizard<FTPSBindingPage> wizard = BindingWizard.createFTPSBindingWizard();
		assertEquals("ftps1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("ftps-binding");
		wizard.getBindingPage().setDirectory("ftps-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("ftps-binding", properties.getCamelBindingPage().getName());
		assertEquals("ftps-directory", properties.getFTPSBindingPage().getDirectory());
		properties.ok();
	}

	@Test
	public void fileBindingTest() throws Exception {
		new Service(SERVICE).addBinding("File");
		BindingWizard<FileBindingPage> wizard = BindingWizard.createFileBindingWizard();
		assertEquals("file1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("file-binding");
		wizard.getBindingPage().setDirectory("file-directory");
		wizard.getBindingPage().setDirAutoCreation(true);
		wizard.getBindingPage().setMoveDirectory("processed");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("file-binding", properties.getCamelBindingPage().getName());
		assertEquals("file-directory", properties.getFileBindingPage().getDirectory());
		assertTrue(properties.getFileBindingPage().isDirAutoCreation());
		properties.ok();
	}

	@Test
	public void httpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("HTTP");
		BindingWizard<HTTPBindingPage> wizard = BindingWizard.createHTTPBindingWizard();
		assertEquals("http1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("http-binding");
		wizard.getBindingPage().setContextPath("http-context");
		wizard.getBindingPage().setOperation(OPERATION);
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("http-binding", properties.getCamelBindingPage().getName());
		assertEquals("http-context", properties.getHTTPBindingPage().getContextPath());
		assertEquals(OPERATION, properties.getHTTPBindingPage().getOperation());
		new PushButton("OK").click();
	}

	@Test
	public void jcaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JCA");

		BindingWizard<JCABindingPage> wizard = BindingWizard.createJCABindingWizard();
		assertEquals("jca1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("jca-binding");
		wizard.getBindingPage().selectGenericResourceAdapter();
		wizard.getBindingPage().setResourceAdapterArchive("resource-adapter.jar");
		wizard.next();
		wizard.getBindingPage().selectJMSEndpoint();
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("jca-binding", properties.getCamelBindingPage().getName());
		assertTrue(properties.getJCABindingPage().isGenericResourceAdapter());
		assertEquals("resource-adapter.jar", properties.getJCABindingPage().getResourceAdapterArchive());
		properties.ok();
	}

	@Test
	public void jmsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JMS");
		BindingWizard<JMSBindingPage> wizard = BindingWizard.createJMSBindingWizard();
		assertEquals("jms1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("jms-binding");
		wizard.getBindingPage().setQueueTopicName("queue-name");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("jms-binding", properties.getCamelBindingPage().getName());
		assertEquals("queue-name", properties.getJMSBindingPage().getQueueTopicName());
		properties.ok();
	}

	@Test
	public void jpaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JPA");
		BindingWizard<JPABindingPage> wizard = BindingWizard.createJPABindingWizard();
		assertEquals("jpa1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("jpa-binding");
		wizard.getBindingPage().setEntityClassName("entity-class");
		wizard.getBindingPage().setPersistenceUnit("persistence.xml");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("jpa-binding", properties.getCamelBindingPage().getName());
		assertEquals("entity-class", properties.getJPABindingPage().getEntityClassName());
		assertEquals("persistence.xml", properties.getJPABindingPage().getPersistenceUnit());
		properties.ok();
	}

	@Test
	public void mailBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Mail");
		BindingWizard<MailBindingPage> wizard = BindingWizard.createMailBindingWizard();
		assertEquals("mail1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("mail-binding");
		wizard.getBindingPage().setHost("mail-host");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("mail-binding", properties.getCamelBindingPage().getName());
		assertEquals("mail-host", properties.getMailBindingPage().getHost());
		properties.ok();
	}

	@Test
	public void nettyTcpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty TCP");
		BindingWizard<NettyTCPBindingPage> wizard = BindingWizard.createNettyTCPBindingWizard();
		assertEquals("tcp1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("tcp-binding");
		wizard.getBindingPage().setHost("tcp-host");
		wizard.getBindingPage().setPort("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("tcp-binding", properties.getCamelBindingPage().getName());
		assertEquals("tcp-host", properties.getNettyTCPBindingPage().getHost());
		assertEquals("1234", properties.getNettyTCPBindingPage().getPort());
		properties.ok();
	}

	@Test
	public void nettyUdpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty UDP");
		BindingWizard<NettyUDPBindingPage> wizard = BindingWizard.createNettyUDPBindingWizard();
		assertEquals("udp1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("udp-binding");
		wizard.getBindingPage().setHost("udp-host");
		wizard.getBindingPage().setPort("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("udp-binding", properties.getCamelBindingPage().getName());
		assertEquals("udp-host", properties.getNettyUDPBindingPage().getHost());
		assertEquals("1234", properties.getNettyUDPBindingPage().getPort());
		properties.ok();
	}

	@Test
	public void restBindingTest() throws Exception {
		new Service(SERVICE).addBinding("REST");
		BindingWizard<RESTBindingPage> wizard = BindingWizard.createRESTBindingWizard();
		assertEquals("rest1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("rest-binding");
		wizard.getBindingPage().setContextPath("rest-context");
		wizard.getBindingPage().addInterface("Hello");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("rest-binding", properties.getCamelBindingPage().getName());
		assertEquals("rest-context", properties.getRESTBindingPage().getContextPath());
		assertTrue(properties.getRESTBindingPage().getInterfaces().contains(PACKAGE + ".Hello"));
		properties.ok();
	}

	@Test
	public void scaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SCA");
		BindingWizard<SCABindingPage> wizard = BindingWizard.createSCABindingWizard();
		assertEquals("sca1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("sca-binding");
		wizard.getBindingPage().setClustered(true);
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("sca-binding", properties.getCamelBindingPage().getName());
		assertTrue(properties.getSCABindingPage().isClustered());
		properties.ok();
	}

	@Test
	public void sftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SFTP");
		BindingWizard<SFTPBindingPage> wizard = BindingWizard.createSFTPBindingWizard();
		assertEquals("sftp1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("sftp-binding");
		wizard.getBindingPage().setDirectory("sftp-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("sftp-binding", properties.getCamelBindingPage().getName());
		assertEquals("sftp-directory", properties.getSFTPBindingPage().getDirectory());
		properties.ok();
	}

	@Test
	public void soapBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SOAP");
		BindingWizard<SOAPBindingPage> wizard = BindingWizard.createSOAPBindingWizard();
		assertEquals("soap1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("soap-binding");
		wizard.getBindingPage().setContextPath("soap-context");
		wizard.getBindingPage().setWsdlURI("soap-wsdl");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("soap-binding", properties.getCamelBindingPage().getName());
		assertEquals("soap-context", properties.getSOAPBindingPage().getContextPath());
		properties.ok();
	}

	@Test
	public void sqlBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SQL");
		BindingWizard<SQLBindingPage> wizard = BindingWizard.createSQLBindingWizard();
		assertEquals("sql1", wizard.getBindingPage().getName());
		wizard.getBindingPage().setName("sql-binding");
		wizard.getBindingPage().setQuery("sql-query");
		wizard.getBindingPage().setDataSource("data-source");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("sql-binding", properties.getCamelBindingPage().getName());
		assertEquals("sql-query", properties.getSQLBindingPage().getQuery());
		assertEquals("data-source", properties.getSQLBindingPage().getDataSource());
		properties.ok();
	}

	@Test
	public void schedulingBindingTest() throws Exception {
		String cron = "0 0 12 * * ?";
		String startTime = "2014-01-01T00:00:00";
		String endTime = "2015-01-01T00:00:00";

		new Service(SERVICE).addBinding("Scheduling");
		BindingWizard<SchedulingBindingPage> wizard = BindingWizard.createSchedulingBindingWizard();
		wizard.getBindingPage().setName("schedule-binding");
		wizard.getBindingPage().setCron(cron);
		wizard.getBindingPage().setOperation(OPERATION);
		wizard.getBindingPage().setStartTime(startTime);
		wizard.getBindingPage().setEndTime(endTime);
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals(cron, properties.getSchedulingBindingPage().getCron());
		assertEquals(OPERATION, properties.getHTTPBindingPage().getOperation());
		assertEquals(startTime, properties.getSchedulingBindingPage().getStartTime());
		assertEquals(endTime, properties.getSchedulingBindingPage().getEndTime());
		properties.ok();
	}
}
