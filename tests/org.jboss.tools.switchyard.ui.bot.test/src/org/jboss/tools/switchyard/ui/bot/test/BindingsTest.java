package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
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
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.DefaultServiceWizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

// TODO Add checking all attributes
// TODO Add test for editing existing bindings (now it is commented out)

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

	private SwitchYardEditor editor;

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	@BeforeClass
	public static void maximizeWorkbench() {
		new WorkbenchShell().maximize();
	}

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
		new SwitchYardEditor().saveAndClose();
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

		new SwitchYardEditor().addService();
		new DefaultShell("New Service");
		new SWTWorkbenchBot().shell("New Service").activate();
		new DefaultServiceWizard().selectJavaInterface("Hello").setServiceName("HelloService").finish();
		new SwitchYardEditor().save();
	}

	@AfterClass
	public static void deleteProject() {
		new WorkbenchShell().maximize();
		new SwitchYardProject(PROJECT).delete(true);
	}

	@Before
	public void focusSwitchYardEditor() {
		editor = new SwitchYardEditor();
	}

	public void addService() {
		editor = new SwitchYardEditor();
		new SwitchYardEditor().addService();
		new DefaultShell("New Service");
		new SWTWorkbenchBot().shell("New Service").activate();
		new DefaultServiceWizard().selectJavaInterface("Hello").setServiceName("HelloService").finish();
		new SwitchYardEditor().save();
	}

	@After
	public void removeAllBindings() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new Service("HelloService").showProperties().selectBindings().removeAll().ok();
		new SwitchYardEditor().save();
	}

	@Test
	public void camelBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Camel");
		CamelBindingPage wizard = new CamelBindingPage();
		assertEquals("camel1", wizard.getName());
		// wizard.setName("camel-binding");
		wizard.setConfigURI("camel-uri");
		wizard.setOperation(OPERATION);
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("camel1", editor.xpath("/switchyard/composite/service/binding.uri/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties() .selectBindings();
		 * CamelBindingPage page = properties.selectCamelBinding("camel-binding");
		 * assertEquals("camel1", page.getName()); assertEquals("camel-uri", page.getConfigURI());
		 * assertEquals(OPERATION, page.getOperation()); properties.ok();
		 */
	}

	@Test
	public void ftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTP");
		FTPBindingPage wizard = new FTPBindingPage();
		assertEquals("ftp1", wizard.getName());
		// wizard.setName("ftp-binding");
		wizard.setDirectory("ftp-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("ftp1", editor.xpath("/switchyard/composite/service/binding.ftp/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * FTPBindingPage page = properties.selectFTPBinding("ftp-binding"); assertEquals("ftp1",
		 * page.getName()); assertEquals("ftp-directory", page.getDirectory()); properties.ok();
		 */
	}

	@Test
	public void ftpsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTPS");
		FTPSBindingPage wizard = new FTPSBindingPage();
		assertEquals("ftps1", wizard.getName());
		// wizard.setName("ftps-binding");
		wizard.setDirectory("ftps-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("ftps1", editor.xpath("/switchyard/composite/service/binding.ftps/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * FTPSBindingPage page = properties.selectFTPSBinding("ftps-binding");
		 * assertEquals("ftps1", page.getName()); assertEquals("ftps-directory",
		 * page.getDirectory()); properties.ok();
		 */
	}

	@Test
	public void fileBindingTest() throws Exception {
		new Service(SERVICE).addBinding("File");
		FileBindingPage wizard = new FileBindingPage();
		assertEquals("file1", wizard.getName());
		// wizard.setName("file-binding");
		wizard.setDirectory("file-directory");
		wizard.setDirAutoCreation(true);
		wizard.setMoveDirectory("processed");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("file1", editor.xpath("/switchyard/composite/service/binding.file/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * FileBindingPage page = properties.selectFileBinding("file-binding");
		 * assertEquals("file1", page.getName()); assertEquals("file-directory",
		 * page.getDirectory()); assertTrue(page.isDirAutoCreation()); properties.ok();
		 */
	}

	@Test
	public void httpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("HTTP");
		HTTPBindingPage wizard = new HTTPBindingPage();
		assertEquals("http1", wizard.getName());
		// wizard.setName("http-binding");
		wizard.setContextPath("http-context");
		wizard.setOperation(OPERATION);
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("http1", editor.xpath("/switchyard/composite/service/binding.http/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * HTTPBindingPage page = properties.selectHTTPBinding("http-binding");
		 * assertEquals("http1", page.getName()); assertEquals("http-context",
		 * page.getContextPath()); assertEquals(OPERATION, page.getOperation()); new
		 * PushButton("OK").click();
		 */
	}

	@Test
	public void jcaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JCA");

		JCABindingPage wizard = new JCABindingPage();
		assertEquals("jca1", wizard.getName());
		// wizard.setName("jca-binding");
		wizard.selectGenericResourceAdapter();
		wizard.setResourceAdapterArchive("resource-adapter.jar");
		wizard.next();
		wizard.selectJMSEndpoint();
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("jca1", editor.xpath("/switchyard/composite/service/binding.jca/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * JCABindingPage page = properties.selectJCABinding("jca-binding"); assertEquals("jca1",
		 * page.getName()); assertTrue(page.isGenericResourceAdapter());
		 * assertEquals("resource-adapter.jar", page.getResourceAdapterArchive()); properties.ok();
		 */
	}

	@Test
	public void jmsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JMS");
		JMSBindingPage wizard = new JMSBindingPage();
		assertEquals("jms1", wizard.getName());
		// wizard.setName("jms-binding");
		wizard.setQueueTopicName("queue-name");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("jms1", editor.xpath("/switchyard/composite/service/binding.jms/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; JMSBindingPage page = properties . selectJMSBinding ( "jms-binding" ) ; assertEquals
		 * ( "jms-binding" , page . getName ( ) ) ; assertEquals ( "queue-name" , page .
		 * getQueueTopicName ( ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void jpaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JPA");
		JPABindingPage wizard = new JPABindingPage();
		assertEquals("jpa1", wizard.getName());
		// wizard.setName("jpa-binding");
		wizard.setEntityClassName("entity-class");
		wizard.setPersistenceUnit("persistence.xml");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("jpa1", editor.xpath("/switchyard/composite/service/binding.jpa/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; JPABindingPage page = properties . selectJPABinding ( "jpa-binding" ) ; assertEquals
		 * ( "jpa-binding" , page . getName ( ) ) ; assertEquals ( "entity-class" , page .
		 * getEntityClassName ( ) ) ; assertEquals ( "persistence.xml" , page . getPersistenceUnit (
		 * ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void mailBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Mail");
		MailBindingPage wizard = new MailBindingPage();
		assertEquals("mail1", wizard.getName());
		// wizard.setName("mail-binding");
		wizard.setHost("mail-host");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("mail1", editor.xpath("/switchyard/composite/service/binding.mail/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; MailBindingPage page = properties . selectMailBinding ( "mail-binding" ) ;
		 * assertEquals ( "mail-binding" , page . getName ( ) ) ; assertEquals ( "mail-host" , page
		 * . getHost ( ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void nettyTcpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty TCP");
		NettyTCPBindingPage wizard = new NettyTCPBindingPage();
		assertEquals("tcp1", wizard.getName());
		// wizard.setName("tcp-binding");
		wizard.setHost("tcp-host");
		wizard.setPort("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("tcp1", editor.xpath("/switchyard/composite/service/binding.tcp/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; NettyTCPBindingPage page = properties . selectNettyTCPBinding ( "tcp-binding" ) ;
		 * assertEquals ( "tcp-binding" , page . getName ( ) ) ; assertEquals ( "tcp-host" , page .
		 * getHost ( ) ) ; assertEquals ( "1234" , page . getPort ( ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void nettyUdpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty UDP");
		NettyUDPBindingPage wizard = new NettyUDPBindingPage();
		assertEquals("udp1", wizard.getName());
		// wizard.setName("udp-binding");
		wizard.setHost("udp-host");
		wizard.setPort("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("udp1", editor.xpath("/switchyard/composite/service/binding.udp/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; NettyUDPBindingPage page = properties . selectNettyUDPBinding ( "udp-binding" ) ;
		 * assertEquals ( "udp-binding" , page . getName ( ) ) ; assertEquals ( "udp-host" , page .
		 * getHost ( ) ) ; assertEquals ( "1234" , page . getPort ( ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void restBindingTest() throws Exception {
		new Service(SERVICE).addBinding("REST");
		RESTBindingPage wizard = new RESTBindingPage();
		assertEquals("rest1", wizard.getName());
		// wizard.setName("rest-binding");
		wizard.setContextPath("rest-context");
		wizard.addInterface("Hello");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("rest1", editor.xpath("/switchyard/composite/service/binding.rest/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; RESTBindingPage page = properties . selectRESTBinding ( "est-binding" ) ;
		 * assertEquals ( "rest-binding" , page . getName ( ) ) ; assertEquals ( "rest-context" ,
		 * page . getContextPath ( ) ) ; assertTrue ( page . getInterfaces ( ) . contains ( PACKAGE
		 * + ".Hello" ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void scaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SCA");
		SCABindingPage wizard = new SCABindingPage();
		assertEquals("sca1", wizard.getName());
		// wizard.setName("sca-binding");
		wizard.setClustered(true);
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("sca1", editor.xpath("/switchyard/composite/service/binding.sca/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; SCABindingPage page = properties . selectSCABinding ( "sca-binding" ) ; assertEquals
		 * ( "sca-binding" , page . getName ( ) ) ; assertTrue ( page . isClustered ( ) ) ;
		 * properties . ok ( ) ;
		 */
	}

	@Test
	public void sftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SFTP");
		SFTPBindingPage wizard = new SFTPBindingPage();
		assertEquals("sftp1", wizard.getName());
		// wizard.setName("sftp-binding");
		wizard.setDirectory("sftp-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("sftp1", editor.xpath("/switchyard/composite/service/binding.sftp/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; SFTPBindingPage page = properties . selectSFTPBinding ( "sftp-binding" ) ;
		 * assertEquals ( "sftp-binding" , page . getName ( ) ) ; assertEquals ( "sftp-directory" ,
		 * page . getDirectory ( ) ) ; properties . ok ( ) ;
		 */
	}

	@Test
	public void soapBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SOAP");
		SOAPBindingPage wizard = new SOAPBindingPage();
		assertEquals("soap1", wizard.getName());
		// wizard.setName("soap-binding");
		wizard.setContextPath("soap-context");
		wizard.setWsdlURI("soap-wsdl");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("soap1", editor.xpath("/switchyard/composite/service/binding.soap/@name"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * SOAPBindingPage page = properties.selectSOAPBinding("soap-binding");
		 * assertEquals("soap1", page.getName()); assertEquals("soap-context",
		 * page.getContextPath()); properties.ok();
		 */
	}

	@Test
	public void sqlBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SQL");
		SQLBindingPage wizard = new SQLBindingPage();
		assertEquals("sql1", wizard.getName());
		// wizard.setName("sql-binding");
		wizard.setQuery("sql-query");
		wizard.setDataSource("data-source");
		wizard.setPeriod("10");
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("sql1", editor.xpath("/switchyard/composite/service/binding.sql/@name"));
		// assertEquals("10",
		// editor.xpath("/switchyard/composite/service/binding.sql/@period"));
		// assertEquals("sql-query",
		// editor.xpath("/switchyard/composite/service/binding.sql/query"));
		// assertEquals("data-source",
		// editor.xpath("/switchyard/composite/service/binding.sql/dataSourceRef"));

		/*
		 * BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		 * SQLBindingPage page = properties.selectSQLBinding("sql-binding"); assertEquals("sql1",
		 * page.getName()); assertEquals("sql-query", page.getQuery()); assertEquals("data-source",
		 * page.getDataSource()); properties.ok();
		 */
	}

	@Test
	public void schedulingBindingTest() throws Exception {
		String cron = "0 0 12 * * ?";
		String startTime = "2014-01-01T00:00:00";
		String endTime = "2015-01-01T00:00:00";

		new Service(SERVICE).addBinding("Scheduling");
		SchedulingBindingPage wizard = new SchedulingBindingPage();
		// wizard.setName("schedule-binding");
		wizard.setCron(cron);
		wizard.setOperation(OPERATION);
		wizard.setStartTime(startTime);
		wizard.setEndTime(endTime);
		wizard.finish();

		new SwitchYardEditor().save();

		assertEquals("HelloService1", editor.xpath("/switchyard/composite/service/binding.quartz/@name"));

		/*
		 * BindingsPage properties = new Service ( SERVICE ) . showProperties ( ) . selectBindings (
		 * ) ; SchedulingBindingPage page = properties . selectSchedulingBinding ( "schdule-binding"
		 * ) ; assertEquals ( cron , page . getCron ( ) ) ; assertEquals ( OPERATION , page .
		 * getOperation ( ) ) ; assertEquals ( startTime , page . getStartTime ( ) ) ; assertEquals
		 * ( endTime , page . getEndTime ( ) ) ; properties . ok ( ) ;
		 */
	}
}
