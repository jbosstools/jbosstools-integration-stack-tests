package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.binding.CXFBindingPage.DATA_FORMAT_POJO;
import static org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage.EXECUTION_PROTOCOL_S;
import static org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage.SECURITY_PROTOCOL_SSL;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.ACKNOWLEDGE_MODE_AUTO;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.ACKNOWLEDGE_MODE_DUPS_OK;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.ENDPOINT_JMS;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.RESOURCE_ADAPTER_GENERIC;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.RESOURCE_ADAPTER_HORNETQ_QUEUE;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.RESOURCE_ADAPTER_HORNETQ_TOPIC;
import static org.jboss.tools.switchyard.reddeer.binding.JCABindingPage.SUBSCRIPTION_NONDURABLE;
import static org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage.TYPE_QUEUE;
import static org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage.TYPE_TOPIC;
import static org.jboss.tools.switchyard.reddeer.binding.MQTTBindingPage.QOS_EXACTLY_ONCE;
import static org.jboss.tools.switchyard.reddeer.binding.MailBindingPage.ACCOUNT_TYPE_IMAP;
import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.JAVA_CLASS;
import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.OPERATION_NAME;
import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.REGEX;
import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.XPATH;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_IDOC;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_IDOC_LIST;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_QIDOC;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_QIDOC_LIST;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_QRFC;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_SRFC;
import static org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage.SAP_OBJECT_TRFC;
import static org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage.SOAP_HEADERS_TYPE_DOM;
import static org.jboss.tools.switchyard.reddeer.binding.SchedulingBindingPage.SCHEDULING_TYPE_CRON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.jboss.reddeer.core.handler.ShellHandler;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.switchyard.reddeer.binding.AtomBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.CXFBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.CamelBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FileBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JPABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MQTTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MailBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyTCPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyUDPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RESTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RSSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SFTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SQLBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SchedulingBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.preference.binding.BindingsPage;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.utils.PreferenceUtils;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

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
	public static final String REFERENCE_SERVICE = "HelloRefService";
	public static final String METHOD = "sayHello";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT;
	public static final String[] GATEWAY_BINDINGS = new String[] { "Atom", "Camel Core (SEDA/Timer/URI)", "CXF",
			"File", "File Transfer (FTP/FTPS/SFTP)", "HTTP", "JCA", "JMS", "JPA", "Mail", "MQTT", "Network (TCP/UDP)",
			"REST", "RSS", "SAP", "SCA", "Scheduling", "SOAP", "SQL" };

	private static String autoBuilding;

	private SwitchYardEditor editor;

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	@BeforeClass
	public static void turnOffAutoBuilding() {
		autoBuilding = PreferenceUtils.getAutoBuilding();
		PreferenceUtils.setAutoBuilding("false");
	}

	@AfterClass
	public static void turnBackAutoBuilding() {
		PreferenceUtils.setAutoBuilding(autoBuilding);
	}

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

		new SwitchYardEditor().addBeanImplementation().createJavaInterface("Hello").finish();

		new SwitchYardComponent("Hello").doubleClick();
		TextEditor textEditor = new TextEditor("Hello.java");
		textEditor.setText("package com.example.switchyard.binding_project;\n" + "import javax.ws.rs.Produces;"
				+ "import javax.ws.rs.GET;\n" + "import javax.ws.rs.Path;\n" + "import javax.ws.rs.PathParam;\n"
				+ "public interface Hello {\n" + "@GET()\n" + "@Path(\"/{name}\")\n" + "@Produces(\"text/plain\")\n"
				+ "String sayHello(@PathParam(\"name\") String name);\n}");
		textEditor.save();
		textEditor.close();

		new SwitchYardComponent("HelloBean").doubleClick();
		textEditor = new TextEditor("HelloBean.java");
		textEditor.setText("package com.example.switchyard.binding_project;\n"
				+ "import org.switchyard.component.bean.Service;\n" + "@Service(Hello.class)\n"
				+ "public class HelloBean implements Hello {\n" + "\t@Override\n"
				+ "\tpublic String sayHello(String name) {\n" + "\t\treturn \"Hello \" + name;\n" + "\t}\n" + "}");
		textEditor.save();
		textEditor.close();

		new Service("Hello").promoteService().activate().setServiceName("HelloService").finish();
		new SwitchYardComponent("HelloBean").getContextButton("Reference").click();
		new ReferenceWizard().selectJavaInterface("Hello").setServiceName("HelloRef").finish();
		new Service("HelloRef").promoteReference().activate().setServiceName("HelloRefService").finish();
		new SwitchYardEditor().save();
	}

	@AfterClass
	public static void deleteAllProjects() {
		EditorHandler.getInstance().closeAll(true);

		// workaround for deleting all projects
		Exception exception = null;
		for (int i = 0; i < 10; i++) {
			try {
				exception = null;
				new WorkbenchShell().setFocus();
				new ProjectExplorer().deleteAllProjects();
				break;
			} catch (Exception e) {
				exception = e;
			}
		}
		if (exception != null) {
			throw new RuntimeException("Cannot delete all projects", exception);
		}
	}

	@Before
	public void focusSwitchYardEditor() {
		editor = new SwitchYardProject(PROJECT).openSwitchYardFile();
	}

	@After
	public void removeAllBindings() {
		ShellHandler.getInstance().closeAllNonWorbenchShells();
		new SwitchYardEditor().save();
		new Service("HelloService").showProperties().selectBindings().removeAll().ok();
		new Service("HelloRefService").showProperties().selectBindings().removeAll().ok();
		new SwitchYardEditor().saveAndClose();
	}

	@Test
	public void atomBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		new Service(SERVICE).addBinding("Atom");
		AtomBindingPage wizard = new AtomBindingPage();
		wizard.setName("atom-binding");
		wizard.getFeedURI().setText("http://localhost");
		wizard.getSplitEntries().toggle(false);
		wizard.getSplitEntries().toggle(true);
		wizard.getFilter().toggle(false);
		wizard.getFilter().toggle(true);
		wizard.getLastUpdateStartingTimestamp().setText(time);
		wizard.getSortEntriesbyDate().toggle(false);
		wizard.getSortEntriesbyDate().toggle(true);
		wizard.getDelayBetweenPolls().setText("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.atom";
		assertXPath("atom-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/feedURI");
		assertXPath("true", bindingPath + "/splitEntries");
		assertXPath("true", bindingPath + "/filter");
		assertXPath(time, bindingPath + "/lastUpdate");
		assertXPath("true", bindingPath + "/sortEntries");
		assertXPath("1234", bindingPath + "/consume/delay");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		AtomBindingPage page = properties.selectAtomBinding("atom-binding");
		assertEquals("atom-binding", page.getName());
		properties.ok();
	}

	@Test
	public void atomReferenceBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		new Service(REFERENCE_SERVICE).addBinding("Atom");
		AtomBindingPage wizard = new AtomBindingPage();
		wizard.setName("atom-binding");
		wizard.getFeedURI().setText("http://localhost");
		wizard.getSplitEntries().toggle(false);
		wizard.getSplitEntries().toggle(true);
		wizard.getFilter().toggle(false);
		wizard.getFilter().toggle(true);
		wizard.getLastUpdateStartingTimestamp().setText(time);
		wizard.getSortEntriesbyDate().toggle(false);
		wizard.getSortEntriesbyDate().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.atom";
		assertXPath("atom-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/feedURI");
		assertXPath("true", bindingPath + "/splitEntries");
		assertXPath("true", bindingPath + "/filter");
		assertXPath(time, bindingPath + "/lastUpdate");
		assertXPath("true", bindingPath + "/sortEntries");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		AtomBindingPage page = properties.selectAtomBinding("atom-binding");
		assertEquals("atom-binding", page.getName());
		properties.ok();
	}

	@Test
	public void cxfBindingTest() throws Exception {
		new Service(SERVICE).addBinding("CXF");
		CXFBindingPage wizard = new CXFBindingPage();
		wizard.setName("cxf-binding");
		wizard.getCXFURI().setText("http://localhost");
		wizard.getWSDLURL().setText("hello.wsdl");
		wizard.getDataFormat().setSelection(DATA_FORMAT_POJO);
		wizard.getServiceClass().setText("myClass.java");
		wizard.getPortName().setText("port");
		wizard.getRelayHeaders().toggle(false);
		wizard.getRelayHeaders().toggle(true);
		wizard.getWrapped().toggle(false);
		wizard.getWrapped().toggle(true);
		wizard.getWrappedStyle().setSelection("false");
		wizard.getWrappedStyle().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.cxf";
		assertXPath("cxf-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/cxfURI");
		assertXPath("hello.wsdl", bindingPath + "/wsdlURL");
		assertXPath("myClass.java", bindingPath + "/serviceClass");
		assertXPath("port", bindingPath + "/portName");
		assertXPath(DATA_FORMAT_POJO, bindingPath + "/dataFormat");
		assertXPath("true", bindingPath + "/relayHeaders");
		assertXPath("true", bindingPath + "/wrapped");
		assertXPath("true", bindingPath + "/wrappedStyle");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		CXFBindingPage page = properties.selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		properties.ok();
	}

	@Test
	public void cxfReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("CXF");
		CXFBindingPage wizard = new CXFBindingPage();
		wizard.setName("cxf-binding");
		wizard.getCXFURI().setText("http://localhost");
		wizard.getWSDLURL().setText("hello.wsdl");
		wizard.getDataFormat().setSelection(DATA_FORMAT_POJO);
		wizard.getServiceClass().setText("myClass.java");
		wizard.getServiceName().setText("MyClass");
		wizard.getPortName().setText("port");
		wizard.getRelayHeaders().toggle(false);
		wizard.getRelayHeaders().toggle(true);
		wizard.getWrapped().toggle(false);
		wizard.getWrapped().toggle(true);
		wizard.getWrappedStyle().setSelection("false");
		wizard.getWrappedStyle().setSelection("true");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getDefaultOperationName().setText("foo");
		wizard.getDefaultOperationNamespace().setText("http://foo");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.cxf";
		assertXPath("cxf-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/cxfURI");
		assertXPath("hello.wsdl", bindingPath + "/wsdlURL");
		assertXPath("myClass.java", bindingPath + "/serviceClass");
		assertXPath("MyClass", bindingPath + "/serviceName");
		assertXPath("port", bindingPath + "/portName");
		assertXPath(DATA_FORMAT_POJO, bindingPath + "/dataFormat");
		assertXPath("true", bindingPath + "/relayHeaders");
		assertXPath("true", bindingPath + "/wrapped");
		assertXPath("true", bindingPath + "/wrappedStyle");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("foo", bindingPath + "/defaultOperationName");
		assertXPath("http://foo", bindingPath + "/defaultOperationNamespace");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		CXFBindingPage page = properties.selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		assertEquals("http://localhost", page.getCXFURI().getText());
		assertEquals("hello.wsdl", page.getWSDLURL().getText());
		assertEquals("POJO", page.getDataFormat().getSelection());
		assertEquals("myClass.java", page.getServiceClass().getText());
		assertEquals("MyClass", page.getServiceName().getText());
		properties.ok();
	}

	@Test
	public void camelBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Camel");
		CamelBindingPage wizard = new CamelBindingPage();
		assertEquals("camel1", wizard.getName());
		wizard.setName("camel-binding");
		wizard.getConfigURI().setText("camel-uri");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.uri";
		assertXPath("camel-binding", bindingPath + "/@name");
		assertXPath("camel-uri", bindingPath + "/@configURI");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		CamelBindingPage page = properties.selectCamelBinding("camel-binding");
		assertEquals("camel-binding", page.getName());
		assertEquals("camel-uri", page.getConfigURI().getText());
		assertEquals(OPERATION_NAME, page.getOperationSelector());
		assertEquals(METHOD, page.getOperationSelectorValue());
		properties.ok();
	}

	@Test
	public void camelReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("Camel");
		CamelBindingPage wizard = new CamelBindingPage();
		assertEquals("camel1", wizard.getName());
		wizard.setName("camel-binding");
		wizard.getConfigURI().setText("camel-uri");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.uri";
		assertXPath("camel-binding", bindingPath + "/@name");
		assertXPath("camel-uri", bindingPath + "/@configURI");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		CamelBindingPage page = properties.selectCamelBinding("camel-binding");
		assertEquals("camel-binding", page.getName());
		assertEquals("camel-uri", page.getConfigURI().getText());
		properties.ok();
	}

	@Test
	public void ftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTP");
		FTPBindingPage wizard = new FTPBindingPage();
		assertEquals("ftp1", wizard.getName());
		wizard.setName("ftp-binding");
		wizard.getHost().setText("myhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getUseBinaryTransferMode().toggle(true);
		wizard.getDirectory().setText("ftp-directory");
		wizard.getFileName().setText("fileName");
		wizard.getAutoCreateMissingDirectoriesinFilePath().click();
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getInclude().setText("include");
		wizard.getExclude().setText("exclude");
		wizard.getDeleteFilesOnceProcessed().toggle(true);
		wizard.getProcessSubDirectoriesRecursively().toggle(true);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		wizard.getPreMove().setText("preMove");
		wizard.getMove().setText("move");
		wizard.getMoveFailed().setText("moveFailed");
		wizard.getDelayBetweenPolls().setText("3000");
		wizard.getMaxMessagesPerPoll().setText("10");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.ftp";
		assertXPath("ftp-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("ftp-directory", bindingPath + "/directory");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("fileName", bindingPath + "/fileName");
		assertXPath("myhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("true", bindingPath + "/binary");
		assertXPath("true", bindingPath + "/consume/delete");
		assertXPath("true", bindingPath + "/consume/recursive");
		assertXPath("preMove", bindingPath + "/consume/preMove");
		assertXPath("move", bindingPath + "/consume/move");
		assertXPath("moveFailed", bindingPath + "/consume/moveFailed");
		assertXPath("include", bindingPath + "/consume/include");
		assertXPath("exclude", bindingPath + "/consume/exclude");
		assertXPath("10", bindingPath + "/consume/maxMessagesPerPoll");
		assertXPath("3000", bindingPath + "/consume/delay");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		FTPBindingPage page = properties.selectFTPBinding("ftp-binding");
		assertEquals("ftp-binding", page.getName());
		assertEquals("ftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void ftpReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("FTP");
		FTPBindingPage wizard = new FTPBindingPage();
		assertEquals("ftp1", wizard.getName());
		wizard.setName("ftp-binding");
		wizard.getHost().setText("myhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getUseBinaryTransferMode().toggle(true);
		wizard.getDirectory().setText("ftp-directory");
		wizard.getFileName().setText("fileName");
		wizard.getAutoCreateMissingDirectoriesinFilePath().click();
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getFileExist().setText("exist");
		wizard.getTempPrefix().setText("prefix");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.ftp";
		assertXPath("ftp-binding", bindingPath + "/@name");
		assertXPath("ftp-directory", bindingPath + "/directory");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("fileName", bindingPath + "/fileName");
		assertXPath("myhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("true", bindingPath + "/binary");
		assertXPath("exist", bindingPath + "/produce/fileExist");
		assertXPath("prefix", bindingPath + "/produce/tempPrefix");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		FTPBindingPage page = properties.selectFTPBinding("ftp-binding");
		assertEquals("ftp-binding", page.getName());
		assertEquals("myhost", page.getHost().getText());
		assertEquals("admin", page.getUserName().getText());
		assertEquals("admin123$", page.getPassword().getText());
		assertEquals("ftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void ftpsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTPS");
		FTPSBindingPage wizard = new FTPSBindingPage();
		assertEquals("ftps1", wizard.getName());
		wizard.setName("ftps-binding");
		wizard.getHost().setText("localhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getUseBinaryTransferMode().toggle(true);
		wizard.getDirectory().setText("ftps-directory");
		wizard.getFileName().setText("filename");
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getInclude().setText("include");
		wizard.getExclude().setText("exclude");
		wizard.getDeleteFilesOnceProcessed().toggle(true);
		wizard.getProcessSubDirectoriesRecursively().toggle(true);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		wizard.getPreMove().setText("pre-move");
		wizard.getMove().setText("move");
		wizard.getMoveFailed().setText("move-failed");
		wizard.getDelayBetweenPolls().setText("1200");
		wizard.getMaxMessagesPerPoll().setText("3");
		wizard.next();
		wizard.getSecurityProtocol().setSelection(FTPSBindingPage.SECURITY_PROTOCOL_SSL);
		wizard.getSecurityProtocol().setSelection(FTPSBindingPage.SECURITY_PROTOCOL_TLS);
		wizard.getImplicit().toggle(true);
		wizard.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_C);
		wizard.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_S);
		wizard.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_E);
		wizard.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_P);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.ftps";
		assertXPath("ftps-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("ftps-directory", bindingPath + "/directory");
		assertXPath("filename", bindingPath + "/fileName");
		assertXPath("localhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("true", bindingPath + "/binary");
		assertXPath("true", bindingPath + "/consume/delete");
		assertXPath("true", bindingPath + "/consume/recursive");
		assertXPath("pre-move", bindingPath + "/consume/preMove");
		assertXPath("move", bindingPath + "/consume/move");
		assertXPath("move-failed", bindingPath + "/consume/moveFailed");
		assertXPath("include", bindingPath + "/consume/include");
		assertXPath("exclude", bindingPath + "/consume/exclude");
		assertXPath("3", bindingPath + "/consume/maxMessagesPerPoll");
		assertXPath("1200", bindingPath + "/consume/delay");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		FTPSBindingPage page = properties.selectFTPSBinding("ftps-binding");
		assertEquals("ftps-binding", page.getName());
		assertEquals("ftps-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void ftpsReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("FTPS");
		FTPSBindingPage wizard = new FTPSBindingPage();
		assertEquals("ftps1", wizard.getName());
		wizard.setName("ftps-binding");
		wizard.getHost().setText("localhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getUseBinaryTransferMode().toggle(true);
		wizard.getDirectory().setText("ftps-directory");
		wizard.getFileName().setText("filename");
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getFileExist().setText("exist");
		wizard.getTempPrefix().setText("prefix");
		wizard.next();
		wizard.getSecurityProtocol().setSelection(SECURITY_PROTOCOL_SSL);
		wizard.getImplicit().toggle(true);
		wizard.getExecutionProtocol().setSelection(EXECUTION_PROTOCOL_S);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.ftps";
		assertXPath("ftps-binding", bindingPath + "/@name");
		assertXPath("ftps-directory", bindingPath + "/directory");
		assertXPath("filename", bindingPath + "/fileName");
		assertXPath("localhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("true", bindingPath + "/binary");
		assertXPath("exist", bindingPath + "/produce/fileExist");
		assertXPath("prefix", bindingPath + "/produce/tempPrefix");
		assertXPath("SSL", bindingPath + "/securityProtocol");
		assertXPath("true", bindingPath + "/isImplicit");
		assertXPath("S", bindingPath + "/execProt");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		FTPSBindingPage page = properties.selectFTPSBinding("ftps-binding");
		assertEquals("ftps-binding", page.getName());
		assertEquals("ftps-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void fileBindingTest() throws Exception {
		new Service(SERVICE).addBinding("File");
		FileBindingPage wizard = new FileBindingPage();
		assertEquals("file1", wizard.getName());
		wizard.setName("file-binding");
		wizard.getDirectory().setText("file-directory");
		wizard.getFileName().setText("test.txt");
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getInclude().setText("inc");
		wizard.getExclude().setText("ex");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.getPreMove().setText("pre");
		wizard.getMove().setText("processed");
		wizard.getMoveFailed().setText("failed");
		wizard.getMaxMessagesPerPoll().setText("10");
		wizard.getDelayBetweenPolls().setText("963");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.file";
		assertXPath("file-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("file-directory", bindingPath + "/directory");
		assertXPath("test.txt", bindingPath + "/fileName");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("963", bindingPath + "/consume/delay");
		assertXPath("10", bindingPath + "/consume/maxMessagesPerPoll");
		assertXPath("pre", bindingPath + "/consume/preMove");
		assertXPath("processed", bindingPath + "/consume/move");
		assertXPath("failed", bindingPath + "/consume/moveFailed");
		assertXPath("inc", bindingPath + "/consume/include");
		assertXPath("ex", bindingPath + "/consume/exclude");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		FileBindingPage page = properties.selectFileBinding("file-binding");
		assertEquals("file-binding", page.getName());
		assertEquals("file-directory", page.getDirectory().getText());
		assertTrue(page.getAutoCreateMissingDirectoriesinFilePath().isChecked());
		properties.ok();
	}

	@Test
	public void fileReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("File");
		FileBindingPage wizard = new FileBindingPage();
		assertEquals("file1", wizard.getName());
		wizard.setName("file-binding");
		wizard.getDirectory().setText("file-directory");
		wizard.getFileName().setText("test.txt");
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getFileExist().setText("exist");
		wizard.getTempPrefix().setText("prefix");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.file";
		assertXPath("file-binding", bindingPath + "/@name");
		assertXPath("file-directory", bindingPath + "/directory");
		assertXPath("test.txt", bindingPath + "/fileName");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("exist", bindingPath + "/produce/fileExist");
		assertXPath("prefix", bindingPath + "/produce/tempPrefix");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		FileBindingPage page = properties.selectFileBinding("file-binding");
		assertEquals("file-binding", page.getName());
		assertEquals("file-directory", page.getDirectory().getText());
		assertTrue(page.getAutoCreateMissingDirectoriesinFilePath().isChecked());
		properties.ok();
	}

	@Test
	public void httpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("HTTP");
		HTTPBindingPage wizard = new HTTPBindingPage();
		assertEquals("http1", wizard.getName());
		wizard.setName("http-binding");
		wizard.getContextPath().setText("http-context");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.http";
		assertXPath("http-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("http-context", bindingPath + "/contextPath");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		HTTPBindingPage page = properties.selectHTTPBinding("http-binding");
		assertEquals("http-binding", page.getName());
		page.setName("http1");
		assertEquals("http-context", page.getContextPath().getText());
		page.getContextPath().setText("hello");
		assertEquals(OPERATION_NAME, page.getOperationSelector());
		assertEquals(METHOD, page.getOperationSelectorValue());
		page.setOperationSelector(XPATH, "/hello/method");
		page.ok();

		new SwitchYardEditor().save();

		assertXPath("http1", bindingPath + "/@name");
		assertXPath("/hello/method", bindingPath + "/operationSelector.xpath/@expression");
		assertXPath("hello", bindingPath + "/contextPath");
	}

	@Test
	public void httpReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("HTTP");
		HTTPBindingPage wizard = new HTTPBindingPage();
		assertEquals("http1", wizard.getName());
		wizard.setName("http-binding");
		wizard.getAddress().setText("http://localhost");
		wizard.getMethod().setSelection("GET");
		wizard.getContentType().setText("coolType");
		wizard.getRequestTimeout().setText("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.http";
		assertXPath("http-binding", bindingPath + "/@name");
		assertXPath("http-binding", bindingPath + "/@name");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		HTTPBindingPage page = properties.selectHTTPBinding("http-binding");
		assertEquals("http-binding", page.getName());
		page.setName("http1");
		assertEquals("http://localhost", page.getAddress().getText());
		page.getAddress().setText("http://foo");
		page.ok();

		new SwitchYardEditor().save();

		assertXPath("http1", bindingPath + "/@name");
		assertXPath("http://foo", bindingPath + "/address");
		assertXPath("GET", bindingPath + "/method");
		assertXPath("coolType", bindingPath + "/contentType");
		assertXPath("1234", bindingPath + "/timeout");
	}

	@Test
	public void jcaBindingGenericTest() throws Exception {
		new Service(SERVICE).addBinding("JCA");
		JCABindingPage wizard = new JCABindingPage();
		assertEquals("jca1", wizard.getName());
		wizard.setName("jca-binding");
		wizard.getResourceAdapterType().setSelection(RESOURCE_ADAPTER_GENERIC);
		wizard.getResourceAdapterArchive().setText("generic-ra.rar");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		wizard.getEndpointMappingType().setSelection(ENDPOINT_JMS);
		wizard.getTransacted().setText("false");
		wizard.getTransacted().setText("true");
		wizard.getBatchSize().setText("123");
		wizard.getBatchTimeoutin().setText("2000");
		wizard.getConnectionFactoryJNDIName().setText("jndiName");
		wizard.getJNDIPropertiesFileName().setText("jndi.properties");
		wizard.getDestinationType().setSelection("javax.jms.Queue");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jca";
		assertXPath("jca-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("generic-ra.rar", bindingPath + "/inboundConnection/resourceAdapter/@name");
		assertXPath("javax.jms.Queue", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='destinationType']/@value");
		assertXPath("queue/YourQueueName", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='destination']/@value");
		assertXPath("", bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector']/@value");
		assertXPath("org.switchyard.component.jca.endpoint.JMSEndpoint", bindingPath
				+ "/inboundInteraction/endpoint/@type");
		assertXPath("true", bindingPath + "/inboundInteraction/transacted");
		assertXPath("123", bindingPath + "/inboundInteraction/batchCommit/@batchSize");
		assertXPath("2000", bindingPath + "/inboundInteraction/batchCommit/@batchTimeout");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		JCABindingPage page = properties.selectJCABinding("jca-binding");
		assertEquals("jca-binding", page.getName());
		assertEquals(RESOURCE_ADAPTER_GENERIC, page.getResourceAdapterType().getSelection());
		assertEquals("generic-ra.rar", page.getResourceAdapterArchive().getText());
		properties.ok();
	}

	@Test
	public void jcaBindingHornetQueueTest() throws Exception {
		new Service(SERVICE).addBinding("JCA");
		JCABindingPage wizard = new JCABindingPage();
		assertEquals("jca1", wizard.getName());
		wizard.setName("jca-binding");
		wizard.getResourceAdapterType().setSelection(RESOURCE_ADAPTER_HORNETQ_QUEUE);
		wizard.getDestinationQueue().setText("queue/MyQueue");
		wizard.getMessageSelector().setText("ms");
		wizard.getAcknowledgeMode().setSelection(ACKNOWLEDGE_MODE_DUPS_OK);
		wizard.setOperationSelector(REGEX, "say[H|h]ello");
		wizard.next();
		wizard.getEndpointMappingType().setSelection(ENDPOINT_JMS);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jca";
		assertXPath("jca-binding", bindingPath + "/@name");
		assertXPath("say[H|h]ello", bindingPath + "/operationSelector.regex/@expression");
		assertXPath("hornetq-ra.rar", bindingPath + "/inboundConnection/resourceAdapter/@name");
		assertXPath("javax.jms.Queue", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='destinationType']/@value");
		assertXPath("queue/MyQueue", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='destination']/@value");
		assertXPath("ms", bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector']/@value");
		assertXPath("Dups-ok-acknowledge", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='acknowledgeMode']/@value");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		JCABindingPage page = properties.selectJCABinding("jca-binding");
		assertEquals("jca-binding", page.getName());
		assertTrue(page.getResourceAdapterType().getSelection().equals(RESOURCE_ADAPTER_HORNETQ_QUEUE));
		properties.ok();
	}

	@Test
	public void jcaBindingHornetTopicTest() throws Exception {
		new Service(SERVICE).addBinding("JCA");
		JCABindingPage wizard = new JCABindingPage();
		assertEquals("jca1", wizard.getName());
		wizard.setName("jca-binding");
		wizard.getResourceAdapterType().setSelection(RESOURCE_ADAPTER_HORNETQ_TOPIC);
		wizard.getDestinationTopic().setText("topic/MyTopic");
		wizard.getMessageSelector().setText("ms");
		wizard.getAcknowledgeMode().setSelection(ACKNOWLEDGE_MODE_AUTO);
		wizard.getSubscriptionDurability().setSelection(SUBSCRIPTION_NONDURABLE);
		wizard.getClientID().setText("clientID");
		wizard.getSubscriptionName().setText("sub-name");
		wizard.setOperationSelector(JAVA_CLASS, "myClass.java");
		wizard.next();
		wizard.getEndpointMappingType().setSelection(ENDPOINT_JMS);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jca";
		assertXPath("jca-binding", bindingPath + "/@name");
		assertXPath("myClass.java", bindingPath + "/operationSelector.java/@class");
		assertXPath("hornetq-ra.rar", bindingPath + "/inboundConnection/resourceAdapter/@name");
		assertXPath("javax.jms.Topic", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='destinationType']/@value");
		assertXPath("topic/MyTopic", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='destination']/@value");
		assertXPath("ms", bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector']/@value");
		assertXPath(ACKNOWLEDGE_MODE_AUTO, bindingPath
				+ "/inboundConnection/activationSpec/property[@name='acknowledgeMode']/@value");
		assertXPath("sub-name", bindingPath
				+ "/inboundConnection/activationSpec/property[@name='subscriptionName']/@value");
		assertXPath(SUBSCRIPTION_NONDURABLE, bindingPath
				+ "/inboundConnection/activationSpec/property[@name='subscriptionDurability']/@value");
		assertXPath("clientID", bindingPath + "/inboundConnection/activationSpec/property[@name='clientId']/@value");
		assertXPath("1", "count(" + bindingPath
				+ "/inboundConnection/activationSpec/property[@name='messageSelector'])");
		assertXPath("1", "count(" + bindingPath
				+ "/inboundConnection/activationSpec/property[@name='acknowledgeMode'])");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		JCABindingPage page = properties.selectJCABinding("jca-binding");
		assertEquals("jca-binding", page.getName());
		assertTrue(page.getResourceAdapterType().getSelection().equals(RESOURCE_ADAPTER_HORNETQ_TOPIC));
		properties.ok();
	}

	@Test
	public void jmsQueueBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JMS");
		JMSBindingPage wizard = new JMSBindingPage();
		assertEquals("jms1", wizard.getName());
		wizard.setName("jms-binding");
		wizard.getType().setSelection(TYPE_QUEUE);
		wizard.getQueueTopicName().setText("myqueue");
		wizard.getConnectionFactory().setText("#MyFactory");
		wizard.getConcurrentConsumers().setText("3");
		wizard.getMaximumConcurrentConsumers().setText("7");
		wizard.getReplyTo().setText("reply-to");
		wizard.getSelector().setText("selector");
		wizard.getTransactionManager().setText("MyTX");
		wizard.getTransacted().toggle(false);
		wizard.getTransacted().toggle(true);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jms";
		assertXPath("jms-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("myqueue", bindingPath + "/queue");
		assertXPath("#MyFactory", bindingPath + "/connectionFactory");
		assertXPath("3", bindingPath + "/concurrentConsumers");
		assertXPath("7", bindingPath + "/maxConcurrentConsumers");
		assertXPath("reply-to", bindingPath + "/replyTo");
		assertXPath("selector", bindingPath + "/selector");
		assertXPath("true", bindingPath + "/transacted");
		assertXPath("MyTX", bindingPath + "/transactionManager");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		JMSBindingPage page = properties.selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("myqueue", page.getQueueTopicName().getText());
		properties.ok();
	}

	@Test
	public void jmsQueueReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("JMS");
		JMSBindingPage wizard = new JMSBindingPage();
		assertEquals("jms1", wizard.getName());
		wizard.setName("jms-binding");
		wizard.getType().setSelection(TYPE_QUEUE);
		wizard.getQueueTopicName().setText("myqueue");
		wizard.getConnectionFactory().setText("#MyFactory");
		wizard.getConcurrentConsumers().setText("3");
		wizard.getMaximumConcurrentConsumers().setText("7");
		wizard.getReplyTo().setText("reply-to");
		wizard.getSelector().setText("selector");
		wizard.getTransactionManager().setText("MyTX");
		wizard.getTransacted().toggle(false);
		wizard.getTransacted().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.jms";
		assertXPath("jms-binding", bindingPath + "/@name");
		assertXPath("myqueue", bindingPath + "/queue");
		assertXPath("#MyFactory", bindingPath + "/connectionFactory");
		assertXPath("3", bindingPath + "/concurrentConsumers");
		assertXPath("7", bindingPath + "/maxConcurrentConsumers");
		assertXPath("reply-to", bindingPath + "/replyTo");
		assertXPath("selector", bindingPath + "/selector");
		assertXPath("true", bindingPath + "/transacted");
		assertXPath("MyTX", bindingPath + "/transactionManager");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		JMSBindingPage page = properties.selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("myqueue", page.getQueueTopicName().getText());

		properties.ok();
	}

	@Test
	public void jmsTopicBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JMS");
		JMSBindingPage wizard = new JMSBindingPage();
		assertEquals("jms1", wizard.getName());
		wizard.setName("jms-binding");
		wizard.getType().setSelection(TYPE_TOPIC);
		wizard.getQueueTopicName().setText("mytopic");
		wizard.getConnectionFactory().setText("#MyFactory");
		wizard.getConcurrentConsumers().setText("3");
		wizard.getMaximumConcurrentConsumers().setText("7");
		wizard.getReplyTo().setText("reply-to");
		wizard.getSelector().setText("selector");
		wizard.getTransactionManager().setText("MyTX");
		wizard.getTransacted().toggle(true);
		wizard.getTransacted().toggle(false);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jms";
		assertXPath("jms-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("mytopic", bindingPath + "/topic");
		assertXPath("#MyFactory", bindingPath + "/connectionFactory");
		assertXPath("3", bindingPath + "/concurrentConsumers");
		assertXPath("7", bindingPath + "/maxConcurrentConsumers");
		assertXPath("reply-to", bindingPath + "/replyTo");
		assertXPath("selector", bindingPath + "/selector");
		assertXPath("false", bindingPath + "/transacted");
		assertXPath("MyTX", bindingPath + "/transactionManager");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		JMSBindingPage page = properties.selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("mytopic", page.getQueueTopicName().getText());
		properties.ok();
	}

	// @Test
	public void jmsTopicReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("JMS");
		JMSBindingPage wizard = new JMSBindingPage();
		assertEquals("jms1", wizard.getName());
		wizard.setName("jms-binding");
		wizard.getType().setSelection(TYPE_TOPIC);
		wizard.getQueueTopicName().setText("mytopic");
		wizard.getConnectionFactory().setText("#MyFactory");
		wizard.getConcurrentConsumers().setText("3");
		wizard.getMaximumConcurrentConsumers().setText("7");
		wizard.getReplyTo().setText("reply-to");
		wizard.getSelector().setText("selector");
		wizard.getTransactionManager().setText("MyTX");
		wizard.getTransacted().toggle(true);
		wizard.getTransacted().toggle(false);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.jms";
		assertXPath("jms-binding", bindingPath + "/@name");
		assertXPath("mytopic", bindingPath + "/topic");
		assertXPath("#MyFactory", bindingPath + "/connectionFactory");
		assertXPath("3", bindingPath + "/concurrentConsumers");
		assertXPath("7", bindingPath + "/maxConcurrentConsumers");
		assertXPath("reply-to", bindingPath + "/replyTo");
		assertXPath("selector", bindingPath + "/selector");
		assertXPath("false", bindingPath + "/transacted");
		assertXPath("MyTX", bindingPath + "/transactionManager");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		JMSBindingPage page = properties.selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("mytopic", page.getQueueTopicName().getText());
		properties.ok();
	}

	@Test
	public void jpaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JPA");
		JPABindingPage wizard = new JPABindingPage();
		assertEquals("jpa1", wizard.getName());
		wizard.setName("jpa-binding");
		wizard.getEntityClassName().setText("EClass.java");
		wizard.getPersistenceUnit().setText("persistence.xml");
		wizard.getTransactionManager().setText("myTX");
		wizard.getDelete().toggle(false);
		wizard.getDelete().toggle(true);
		wizard.getLockEntity().toggle(false);
		wizard.getLockEntity().toggle(true);
		wizard.getMaximumResults().setText("5");
		wizard.getQuery().setText("query");
		wizard.getNamedQuery().setText("named-query");
		wizard.getNativeQuery().setText("native-query");
		wizard.getTransacted().toggle(false);
		wizard.getTransacted().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jpa";
		assertXPath("jpa-binding", bindingPath + "/@name");
		assertXPath("EClass.java", bindingPath + "/entityClassName");
		assertXPath("persistence.xml", bindingPath + "/persistenceUnit");
		assertXPath("myTX", bindingPath + "/transactionManager");
		assertXPath("true", bindingPath + "/consume/consumeDelete");
		assertXPath("true", bindingPath + "/consume/consumeLockEntity");
		assertXPath("5", bindingPath + "/consume/maximumResults");
		assertXPath("query", bindingPath + "/consume/consumer.query");
		assertXPath("named-query", bindingPath + "/consume/consumer.namedQuery");
		assertXPath("native-query", bindingPath + "/consume/consumer.nativeQuery");
		assertXPath("true", bindingPath + "/consume/consumer.transacted");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		JPABindingPage page = properties.selectJPABinding("jpa-binding");
		assertEquals("jpa-binding", page.getName());
		assertEquals("EClass.java", page.getEntityClassName().getText());
		assertEquals("persistence.xml", page.getPersistenceUnit().getText());
		properties.ok();
	}

	// @Test
	public void jpaReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("JPA");
		JPABindingPage wizard = new JPABindingPage();
		assertEquals("jpa1", wizard.getName());
		wizard.setName("jpa-binding");
		wizard.getEntityClassName().setText("EClass.java");
		wizard.getPersistenceUnit().setText("persistence.xml");
		wizard.getTransactionManager().setText("myTX");
		wizard.getFlushonSend().toggle(false);
		wizard.getFlushonSend().toggle(true);
		wizard.getUsePersist().toggle(false);
		wizard.getUsePersist().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.jpa";
		assertXPath("jpa-binding", bindingPath + "/@name");
		assertXPath("EClass.java", bindingPath + "/entityClassName");
		assertXPath("persistence.xml", bindingPath + "/persistenceUnit");
		assertXPath("myTX", bindingPath + "/transactionManager");
		assertXPath("true", bindingPath + "/produce/flushOnSend");
		assertXPath("true", bindingPath + "/produce/usePersist");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		JPABindingPage page = properties.selectJPABinding("jpa-binding");
		assertEquals("jpa-binding", page.getName());
		assertEquals("EClass.java", page.getEntityClassName().getText());
		assertEquals("persistence.xml", page.getPersistenceUnit().getText());
		properties.ok();
	}

	@Test
	public void mqttBindingTest() throws Exception {
		new Service(SERVICE).addBinding("MQTT");
		MQTTBindingPage wizard = new MQTTBindingPage();
		wizard.setName("mqtt-binding");
		wizard.getHostURI().setText("tcp://localhost:1883");
		wizard.getSubscribeTopicName().setText("topicName");
		wizard.getConnectAttemptsMax().setText("111");
		wizard.getReconnectAttemptsMax().setText("222");
		wizard.getQualityofService().setSelection(QOS_EXACTLY_ONCE);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.mqtt";
		assertXPath("mqtt-binding", bindingPath + "/@name");
		assertXPath("tcp://localhost:1883", bindingPath + "/host");
		assertXPath("111", bindingPath + "/connectAttemptsMax");
		assertXPath("222", bindingPath + "/reconnectAttemptsMax");
		assertXPath(QOS_EXACTLY_ONCE, bindingPath + "/qualityOfService");
		assertXPath("topicName", bindingPath + "/subscribeTopicName");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		MQTTBindingPage page = properties.selectMQTTBinding("mqtt-binding");
		assertEquals("mqtt-binding", page.getName());
		properties.ok();
	}

	@Test
	public void mqttReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("MQTT");
		MQTTBindingPage wizard = new MQTTBindingPage();
		wizard.setName("mqtt-binding");
		wizard.getHostURI().setText("tcp://localhost:1883");
		wizard.getPublishTopicName().setText("topicName");
		wizard.getConnectAttemptsMax().setText("111");
		wizard.getReconnectAttemptsMax().setText("222");
		wizard.getQualityofService().setSelection(QOS_EXACTLY_ONCE);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.mqtt";
		assertXPath("mqtt-binding", bindingPath + "/@name");
		assertXPath("tcp://localhost:1883", bindingPath + "/host");
		assertXPath("111", bindingPath + "/connectAttemptsMax");
		assertXPath("222", bindingPath + "/reconnectAttemptsMax");
		assertXPath(QOS_EXACTLY_ONCE, bindingPath + "/qualityOfService");
		assertXPath("topicName", bindingPath + "/publishTopicName");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		MQTTBindingPage page = properties.selectMQTTBinding("mqtt-binding");
		assertEquals("mqtt-binding", page.getName());
		properties.ok();
	}

	@Test
	public void mailBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Mail");
		MailBindingPage wizard = new MailBindingPage();
		assertEquals("mail1", wizard.getName());
		wizard.setName("mail-binding");
		wizard.getHost().setText("localhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getSecured().toggle(false);
		wizard.getSecured().toggle(true);
		wizard.getAccountType().setSelection(ACCOUNT_TYPE_IMAP);
		wizard.getFolderName().setText("inbox");
		wizard.getFetchSize().setText("3");
		wizard.getUnreadOnly().toggle(false);
		wizard.getUnreadOnly().toggle(true);
		wizard.getDelete().toggle(false);
		wizard.getDelete().toggle(true);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/service/binding.mail";
		assertXPath("mail-binding", bindingPath + "/@name");
		assertXPath("true", bindingPath + "/@secure");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("localhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("imap", bindingPath + "/consume/@accountType");
		assertXPath("inbox", bindingPath + "/consume/folderName");
		assertXPath("3", bindingPath + "/consume/fetchSize");
		assertXPath("true", bindingPath + "/consume/delete");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		MailBindingPage page = properties.selectMailBinding("mail-binding");
		assertEquals("mail-binding", page.getName());
		assertEquals("localhost", page.getHost().getText());
		properties.ok();
	}

	@Test
	public void mailReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("Mail");
		MailBindingPage wizard = new MailBindingPage();
		assertEquals("mail1", wizard.getName());
		wizard.setName("mail-binding");
		wizard.getHost().setText("localhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getSecured().toggle(false);
		wizard.getSecured().toggle(true);
		wizard.getSubject().setText("subject");
		wizard.getFrom().setText("from");
		wizard.getTo().setText("to");
		wizard.getCC().setText("cc");
		wizard.getBCC().setText("bcc");
		wizard.getReplyTo().setText("replyto");
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/reference/binding.mail";
		assertXPath("mail-binding", bindingPath + "/@name");
		assertXPath("true", bindingPath + "/@secure");
		assertXPath("localhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("subject", bindingPath + "/produce/subject");
		assertXPath("from", bindingPath + "/produce/from");
		assertXPath("to", bindingPath + "/produce/to");
		assertXPath("cc", bindingPath + "/produce/CC");
		assertXPath("bcc", bindingPath + "/produce/BCC");
		assertXPath("replyto", bindingPath + "/produce/replyTo");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		MailBindingPage page = properties.selectMailBinding("mail-binding");
		assertEquals("mail-binding", page.getName());
		assertEquals("localhost", page.getHost().getText());
		properties.ok();
	}

	@Test
	public void nettyTcpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty TCP");
		NettyTCPBindingPage wizard = new NettyTCPBindingPage();
		assertEquals("tcp1", wizard.getName());
		wizard.setName("tcp-binding");
		wizard.getHost().setText("tcp-host");
		wizard.getPort().setText("1234");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.tcp";
		assertXPath("tcp-binding", bindingPath + "/@name");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("tcp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		NettyTCPBindingPage page = properties.selectNettyTCPBinding("tcp-binding");
		assertEquals("tcp-binding", page.getName());
		assertEquals("tcp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void nettyTcpReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("Netty TCP");
		NettyTCPBindingPage wizard = new NettyTCPBindingPage();
		assertEquals("tcp1", wizard.getName());
		wizard.setName("tcp-binding");
		wizard.getHost().setText("tcp-host");
		wizard.getPort().setText("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.tcp";
		assertXPath("tcp-binding", bindingPath + "/@name");
		assertXPath("tcp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		NettyTCPBindingPage page = properties.selectNettyTCPBinding("tcp-binding");
		assertEquals("tcp-binding", page.getName());
		assertEquals("tcp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void nettyUdpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty UDP");
		NettyUDPBindingPage wizard = new NettyUDPBindingPage();
		assertEquals("udp1", wizard.getName());
		wizard.setName("udp-binding");
		wizard.getHost().setText("udp-host");
		wizard.getPort().setText("1234");
		wizard.getBroadcast().toggle(false);
		wizard.getBroadcast().toggle(true);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.udp";
		assertXPath("udp-binding", bindingPath + "/@name");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("udp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("true", bindingPath + "/broadcast");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		NettyUDPBindingPage page = properties.selectNettyUDPBinding("udp-binding");
		assertEquals("udp-binding", page.getName());
		assertEquals("udp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void nettyUdpReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("Netty UDP");
		NettyUDPBindingPage wizard = new NettyUDPBindingPage();
		assertEquals("udp1", wizard.getName());
		wizard.setName("udp-binding");
		wizard.getHost().setText("udp-host");
		wizard.getPort().setText("1234");
		wizard.getBroadcast().toggle(false);
		wizard.getBroadcast().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.udp";
		assertXPath("udp-binding", bindingPath + "/@name");
		assertXPath("udp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("true", bindingPath + "/broadcast");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		NettyUDPBindingPage page = properties.selectNettyUDPBinding("udp-binding");
		assertEquals("udp-binding", page.getName());
		assertEquals("udp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void restBindingTest() throws Exception {
		new Service(SERVICE).addBinding("REST");
		RESTBindingPage wizard = new RESTBindingPage();
		assertEquals("rest1", wizard.getName());
		wizard.setName("rest-binding");
		wizard.getContextPath().setText("rest-context");
		wizard.addInterface("Hello");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.rest";
		assertXPath("rest-binding", bindingPath + "/@name");
		assertXPath(PACKAGE + ".Hello", bindingPath + "/interfaces");
		assertXPath("rest-context", bindingPath + "/contextPath");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		RESTBindingPage page = properties.selectRESTBinding("rest-binding");
		assertEquals("rest-binding", page.getName());
		assertEquals("rest-context", page.getContextPath().getText());
		assertTrue(page.getInterfaces().contains(PACKAGE + ".Hello"));
		properties.ok();
	}

	@Test
	public void restReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("REST");
		RESTBindingPage wizard = new RESTBindingPage();
		assertEquals("rest1", wizard.getName());
		wizard.setName("rest-binding");
		wizard.getAddress().setText("http://localhost");
		wizard.addInterface("Hello");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.rest";
		assertXPath("rest-binding", bindingPath + "/@name");
		assertXPath(PACKAGE + ".Hello", bindingPath + "/interfaces");
		assertXPath("http://localhost", bindingPath + "/address");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		RESTBindingPage page = properties.selectRESTBinding("rest-binding");
		assertEquals("rest-binding", page.getName());
		assertEquals("http://localhost", page.getAddress().getText());
		assertTrue(page.getInterfaces().contains(PACKAGE + ".Hello"));
		properties.ok();
	}

	@Test
	public void rssBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		new Service(SERVICE).addBinding("RSS");
		RSSBindingPage wizard = new RSSBindingPage();
		wizard.setName("rss-binding");
		wizard.getFeedURI().setText("http://localhost");
		wizard.getSplitEntries().toggle(false);
		wizard.getSplitEntries().toggle(true);
		wizard.getFilter().toggle(false);
		wizard.getFilter().toggle(true);
		wizard.getLastUpdateStartingTimestamp().setText(time);
		wizard.getSortEntriesbyDate().toggle(false);
		wizard.getSortEntriesbyDate().toggle(true);
		wizard.getDelayBetweenPolls().setText("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.rss";
		assertXPath("rss-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/feedURI");
		assertXPath("true", bindingPath + "/splitEntries");
		assertXPath("true", bindingPath + "/filter");
		assertXPath("2015-01-01T00:00:00", bindingPath + "/lastUpdate");
		assertXPath("true", bindingPath + "/sortEntries");
		assertXPath("1234", bindingPath + "/consume/delay");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		RSSBindingPage page = properties.selectRSSBinding("rss-binding");
		assertEquals("rss-binding", page.getName());
		properties.ok();
	}

	// @Test
	public void rssReferenceBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		new Service(REFERENCE_SERVICE).addBinding("RSS");
		RSSBindingPage wizard = new RSSBindingPage();
		wizard.setName("rss-binding");
		wizard.getFeedURI().setText("http://localhost");
		wizard.getSplitEntries().toggle(false);
		wizard.getSplitEntries().toggle(true);
		wizard.getFilter().toggle(false);
		wizard.getFilter().toggle(true);
		wizard.getLastUpdateStartingTimestamp().setText(time);
		wizard.getSortEntriesbyDate().toggle(false);
		wizard.getSortEntriesbyDate().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.rss";
		assertXPath("rss-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/feedURI");
		assertXPath("true", bindingPath + "/splitEntries");
		assertXPath("true", bindingPath + "/filter");
		assertXPath("2015-01-01T00:00:00", bindingPath + "/lastUpdate");
		assertXPath("true", bindingPath + "/sortEntries");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		RSSBindingPage page = properties.selectRSSBinding("rss-binding");
		assertEquals("rss-binding", page.getName());
		properties.ok();
	}

	@Test
	public void sapIDocListBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_IDOC_LIST);
		wizard.getServer().setText("localhost");
		wizard.getIDocType().setText("docType");
		wizard.getIDocTypeExtension().setText("ext");
		wizard.getSystemRelease().setText("sysRel");
		wizard.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("localhost", bindingPath + "/idoclist-server/serverName");
		assertXPath("docType", bindingPath + "/idoclist-server/idocType");
		assertXPath("ext", bindingPath + "/idoclist-server/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/idoclist-server/systemRelease");
		assertXPath("appRel", bindingPath + "/idoclist-server/applicationRelease");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("localhost", page.getServer().getText());
		assertEquals("docType", page.getIDocType().getText());
		assertEquals("ext", page.getIDocTypeExtension().getText());
		assertEquals("sysRel", page.getSystemRelease().getText());
		assertEquals("appRel", page.getApplicationRelease().getText());
		properties.ok();
	}

	@Test
	public void sapIDocListReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_IDOC_LIST);
		wizard.getDestinationName().setText("example.com");
		wizard.getIDocType().setText("docType");
		wizard.getIDocTypeExtension().setText("ext");
		wizard.getSystemRelease().setText("sysRel");
		wizard.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/idoclist-destination/destinationName", "SWITCHYARD-2730");
		assertXPath("docType", bindingPath + "/idoclist-destination/idocType");
		assertXPath("ext", bindingPath + "/idoclist-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/idoclist-destination/systemRelease");
		assertXPath("appRel", bindingPath + "/idoclist-destination/applicationRelease");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("docType", page.getIDocType().getText());
		assertEquals("ext", page.getIDocTypeExtension().getText());
		assertEquals("sysRel", page.getSystemRelease().getText());
		assertEquals("appRel", page.getApplicationRelease().getText());
		properties.ok();
	}

	@Test
	public void sapSRFCBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_SRFC);
		wizard.getServer().setText("localhost");
		wizard.getRFCName().setText("srfcName");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("localhost", bindingPath + "/srfc-server/serverName");
		assertXPath("srfcName", bindingPath + "/srfc-server/rfcName");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("localhost", page.getServer().getText());
		assertEquals("srfcName", page.getRFCName().getText());
		properties.ok();
	}

	@Test
	public void sapSRFCReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_SRFC);
		wizard.getDestinationName().setText("example.com");
		wizard.getRFCName().setText("srfcName");
		wizard.getTransacted().setText("${myVar}");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/srfc-destination/destinationName");
		assertXPath("srfcName", bindingPath + "/srfc-destination/rfcName");
		assertXPath("${myVar}", bindingPath + "/srfc-destination/transacted");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("srfcName", page.getRFCName().getText());
		assertEquals("${myVar}", page.getTransacted().getText());
		properties.ok();
	}

	@Test
	public void sapTRFCBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_TRFC);
		wizard.getServer().setText("localhost");
		wizard.getRFCName().setText("trfcName");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("localhost", bindingPath + "/trfc-server/serverName");
		assertXPath("trfcName", bindingPath + "/trfc-server/rfcName");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("localhost", page.getServer().getText());
		assertEquals("trfcName", page.getRFCName().getText());
		properties.ok();
	}

	@Test
	public void sapTRFCReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_TRFC);
		wizard.getDestinationName().setText("example.com");
		wizard.getRFCName().setText("trfcName");
		wizard.getTransacted().setText("${myVar}");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/trfc-destination/destinationName");
		assertXPath("trfcName", bindingPath + "/trfc-destination/rfcName");
		assertXPath("${myVar}", bindingPath + "/trfc-destination/transacted");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("trfcName", page.getRFCName().getText());
		assertEquals("${myVar}", page.getTransacted().getText());
		properties.ok();
	}

	@Test
	public void sapIDocReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_IDOC);
		wizard.getDestinationName().setText("example.com");
		wizard.getIDocType().setText("docType");
		wizard.getIDocTypeExtension().setText("ext");
		wizard.getSystemRelease().setText("sysRel");
		wizard.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/idoc-destination/destinationName");
		assertXPath("docType", bindingPath + "/idoc-destination/idocType");
		assertXPath("ext", bindingPath + "/idoc-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/idoc-destination/systemRelease");
		assertXPath("appRel", bindingPath + "/idoc-destination/applicationRelease");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("docType", page.getIDocType().getText());
		assertEquals("ext", page.getIDocTypeExtension().getText());
		assertEquals("sysRel", page.getSystemRelease().getText());
		assertEquals("appRel", page.getApplicationRelease().getText());
		properties.ok();
	}

	@Test
	public void sapQIDocReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_QIDOC);
		wizard.getDestinationName().setText("example.com");
		wizard.getQueueName().setText("myqueue");
		wizard.getIDocType().setText("docType");
		wizard.getIDocTypeExtension().setText("ext");
		wizard.getSystemRelease().setText("sysRel");
		wizard.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/qidoc-destination/destinationName");
		assertXPath("myqueue", bindingPath + "/qidoc-destination/queueName");
		assertXPath("docType", bindingPath + "/qidoc-destination/idocType");
		assertXPath("ext", bindingPath + "/qidoc-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/qidoc-destination/systemRelease");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("myqueue", page.getQueueName().getText());
		assertEquals("docType", page.getIDocType().getText());
		assertEquals("ext", page.getIDocTypeExtension().getText());
		assertEquals("sysRel", page.getSystemRelease().getText());
		assertEquals("appRel", page.getApplicationRelease().getText());
		properties.ok();
	}

	@Test
	public void sapQIDocListReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_QIDOC_LIST);
		wizard.getDestinationName().setText("example.com");
		wizard.getQueueName().setText("myqueue");
		wizard.getIDocType().setText("docType");
		wizard.getIDocTypeExtension().setText("ext");
		wizard.getSystemRelease().setText("sysRel");
		wizard.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/qidoclist-destination/destinationName");
		assertXPath("myqueue", bindingPath + "/qidoclist-destination/queueName");
		assertXPath("docType", bindingPath + "/qidoclist-destination/idocType");
		assertXPath("ext", bindingPath + "/qidoclist-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/qidoclist-destination/systemRelease");
		assertXPath("appRel", bindingPath + "/qidoclist-destination/applicationRelease");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("myqueue", page.getQueueName().getText());
		assertEquals("docType", page.getIDocType().getText());
		assertEquals("ext", page.getIDocTypeExtension().getText());
		assertEquals("sysRel", page.getSystemRelease().getText());
		assertEquals("appRel", page.getApplicationRelease().getText());
		properties.ok();
	}

	@Test
	public void sapQRFCReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage wizard = new SAPBindingPage();
		wizard.setName("sap-binding");
		wizard.getSAPObject().setSelection(SAP_OBJECT_QRFC);
		wizard.getDestinationName().setText("example.com");
		wizard.getQueueName().setText("myqueue");
		wizard.getRFCName().setText("qrfcName");
		wizard.getTransacted().setText("${myVar}");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/qrfc-destination/destinationName");
		assertXPath("myqueue", bindingPath + "/qrfc-destination/queueName");
		assertXPath("qrfcName", bindingPath + "/qrfc-destination/rfcName");
		assertXPath("${myVar}", bindingPath + "/qrfc-destination/transacted");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SAPBindingPage page = properties.selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("myqueue", page.getQueueName().getText());
		assertEquals("qrfcName", page.getRFCName().getText());
		assertEquals("${myVar}", page.getTransacted().getText());
		properties.ok();
	}

	@Test
	public void scaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SCA");
		SCABindingPage wizard = new SCABindingPage();
		assertEquals("sca1", wizard.getName());
		wizard.setName("sca-binding");
		wizard.getClustered().setSelection("false");
		wizard.getClustered().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sca";
		assertXPath("sca-binding", bindingPath + "/@name");
		assertXPath("true", bindingPath + "/@clustered");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SCABindingPage page = properties.selectSCABinding("sca-binding");
		assertEquals("sca-binding", page.getName());
		assertEquals("true", page.getClustered().getText());
		properties.ok();
	}

	@Test
	public void scaReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SCA");
		SCABindingPage wizard = new SCABindingPage();
		assertEquals("sca1", wizard.getName());
		wizard.setName("sca-binding");
		wizard.getClustered().setSelection("false");
		wizard.getClustered().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sca";
		assertXPath("sca-binding", bindingPath + "/@name");
		assertXPath("true", bindingPath + "/@clustered");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SCABindingPage page = properties.selectSCABinding("sca-binding");
		assertEquals("sca-binding", page.getName());
		assertEquals("true", page.getClustered().getText());
		properties.ok();
	}

	@Test
	public void sftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SFTP");
		SFTPBindingPage wizard = new SFTPBindingPage();
		assertEquals("sftp1", wizard.getName());
		wizard.setName("sftp-binding");
		wizard.getHost().setText("localhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getUseBinaryTransferMode().toggle(false);
		wizard.getUseBinaryTransferMode().toggle(true);
		wizard.getDirectory().setText("sftp-directory");
		wizard.getFileName().setText("test.txt");
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getInclude().setText("in");
		wizard.getExclude().setText("ex");
		wizard.getDeleteFilesOnceProcessed().toggle(false);
		wizard.getDeleteFilesOnceProcessed().toggle(true);
		wizard.getProcessSubDirectoriesRecursively().toggle(false);
		wizard.getProcessSubDirectoriesRecursively().toggle(true);
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		wizard.getPreMove().setText("preMove");
		wizard.getMove().setText("move");
		wizard.getMoveFailed().setText("moveFailed");
		wizard.getDelayBetweenPolls().setText("1000");
		wizard.getMaxMessagesPerPoll().setText("2");
		wizard.next();
		wizard.getPrivateKeyFile().setText("private.key");
		wizard.getPrivateKeyFilePassphrase().setText("secret");
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/service/binding.sftp";
		assertXPath("sftp-binding", bindingPath + "/@name");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("sftp-directory", bindingPath + "/directory");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("localhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("true", bindingPath + "/binary");
		assertXPath("private.key", bindingPath + "/privateKeyFile");
		assertXPath("secret", bindingPath + "/privateKeyFilePassphrase");
		assertXPath("true", bindingPath + "/consume/delete");
		assertXPath("true", bindingPath + "/consume/recursive");
		assertXPath("preMove", bindingPath + "/consume/preMove");
		assertXPath("move", bindingPath + "/consume/move");
		assertXPath("moveFailed", bindingPath + "/consume/moveFailed");
		assertXPath("in", bindingPath + "/consume/include");
		assertXPath("ex", bindingPath + "/consume/exclude");
		assertXPath("2", bindingPath + "/consume/maxMessagesPerPoll");
		assertXPath("1000", bindingPath + "/consume/delay");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SFTPBindingPage page = properties.selectSFTPBinding("sftp-binding");
		assertEquals("sftp-binding", page.getName());
		assertEquals("sftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void sftpReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SFTP");
		SFTPBindingPage wizard = new SFTPBindingPage();
		assertEquals("sftp1", wizard.getName());
		wizard.setName("sftp-binding");
		wizard.getHost().setText("localhost");
		wizard.getPort().setText("1234");
		wizard.getUserName().setText("admin");
		wizard.getPassword().setText("admin123$");
		wizard.getUseBinaryTransferMode().toggle(false);
		wizard.getUseBinaryTransferMode().toggle(true);
		wizard.getDirectory().setText("sftp-directory");
		wizard.getFileName().setText("test.txt");
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		wizard.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		wizard.getFileExist().setText("exist");
		wizard.getTempPrefix().setText("prefix");
		wizard.next();
		wizard.getPrivateKeyFile().setText("private.key");
		wizard.getPrivateKeyFilePassphrase().setText("secret");
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/reference/binding.sftp";
		assertXPath("sftp-binding", bindingPath + "/@name");
		assertXPath("sftp-directory", bindingPath + "/directory");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("localhost", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("true", bindingPath + "/binary");
		assertXPath("private.key", bindingPath + "/privateKeyFile");
		assertXPath("secret", bindingPath + "/privateKeyFilePassphrase");
		assertXPath("exist", bindingPath + "/produce/fileExist");
		assertXPath("prefix", bindingPath + "/produce/tempPrefix");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SFTPBindingPage page = properties.selectSFTPBinding("sftp-binding");
		assertEquals("sftp-binding", page.getName());
		assertEquals("sftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void soapBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SOAP");
		SOAPBindingPage wizard = new SOAPBindingPage();
		assertEquals("soap1", wizard.getName());
		wizard.setName("soap-binding");
		wizard.getContextPath().setText("soap-context");
		wizard.setWsdlURI("hello.wsdl");
		wizard.getWSDLPort().setText("1234");
		wizard.getServerPort().setText("foo");
		assertFalse(new FinishButton().isEnabled());
		wizard.getServerPort().setText("4321");
		assertTrue(new FinishButton().isEnabled());
		wizard.getServerPort().setText(":4321");
		assertTrue(new FinishButton().isEnabled());
		wizard.getServerPort().setText("host:4321");
		assertTrue(new FinishButton().isEnabled());
		wizard.getServerPort().setText("${propValue}");
		assertTrue(new FinishButton().isEnabled());
		wizard.getServerPort().setText(":${propValue}");
		assertTrue(new FinishButton().isEnabled());
		wizard.getServerPort().setText("host:${propValue}");
		assertTrue(new FinishButton().isEnabled());
		wizard.getServerPort().setText("${propValue}:4321");
		assertTrue(new FinishButton().isEnabled());
		wizard.getUnwrappedPayload().toggle(false);
		wizard.getUnwrappedPayload().toggle(true);
		wizard.getSOAPHeadersType().setSelection(SOAP_HEADERS_TYPE_DOM);
		wizard.getConfigFile().setText("soap.conf");
		wizard.getConfigName().setText("configName");
		wizard.getEnable().toggle(false);
		wizard.getEnable().toggle(true);

		wizard.getTemporarilyDisable().setSelection("false");
		wizard.getTemporarilyDisable().setSelection("true");
		wizard.getxopExpand().setSelection("false");
		wizard.getxopExpand().setSelection("true");
		wizard.getThreshold().setText("963");
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/service/binding.soap";
		assertXPath("soap-binding", bindingPath + "/@name");
		assertXPath("DOM", bindingPath + "/contextMapper/@soapHeadersType");
		assertXPath("true", bindingPath + "/messageComposer/@unwrapped");
		assertXPath("hello.wsdl", bindingPath + "/wsdl");
		assertXPath("1234", bindingPath + "/wsdlPort");
		assertXPath("${propValue}:4321", bindingPath + "/socketAddr");
		assertXPath("soap-context", bindingPath + "/contextPath");
		assertXPath("soap.conf", bindingPath + "/endpointConfig/@configFile");
		assertXPath("configName", bindingPath + "/endpointConfig/@configName");
		assertXPath("true", bindingPath + "/mtom/@enabled");
		assertXPath("963", bindingPath + "/mtom/@threshold");
		assertXPath("true", bindingPath + "/mtom/@xopExpand");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SOAPBindingPage page = properties.selectSOAPBinding("soap-binding");
		assertEquals("soap-binding", page.getName());
		assertEquals("soap-context", page.getContextPath().getText());
		assertEquals("${propValue}:4321", page.getServerPort().getText());
		page.getServerPort().setText("foo");
		assertFalse(new OkButton().isEnabled());
		page.getServerPort().setText("4321");
		assertTrue(new OkButton().isEnabled());
		page.getServerPort().setText(":4321");
		assertTrue(new OkButton().isEnabled());
		page.getServerPort().setText("host:4321");
		assertTrue(new OkButton().isEnabled());
		page.getServerPort().setText("${propValue}");
		assertTrue(new OkButton().isEnabled());
		page.getServerPort().setText(":${propValue}");
		assertTrue(new OkButton().isEnabled());
		page.getServerPort().setText("host:${propValue}");
		assertTrue(new OkButton().isEnabled());
		page.getServerPort().setText("${propValue}:4321");
		assertTrue(new OkButton().isEnabled());

		properties.ok();
	}

	@Test
	public void soapReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SOAP");
		SOAPBindingPage wizard = new SOAPBindingPage();
		assertEquals("soap1", wizard.getName());
		wizard.setName("soap-binding");
		wizard.setEndpointAddress("${propValue}:4321");
		wizard.setWsdlURI("hello.wsdl");
		wizard.getWSDLPort().setText("1234");
		wizard.getUnwrappedPayload().toggle(false);
		wizard.getUnwrappedPayload().toggle(true);
		wizard.getSOAPHeadersType().setSelection(SOAP_HEADERS_TYPE_DOM);
		wizard.getEnable().toggle(false);
		wizard.getEnable().toggle(true);
		wizard.getTemporarilyDisable().setSelection("false");
		wizard.getTemporarilyDisable().setSelection("true");
		wizard.getxopExpand().setSelection("false");
		wizard.getxopExpand().setSelection("true");
		wizard.getThreshold().setText("963");
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/reference/binding.soap";
		assertXPath("soap-binding", bindingPath + "/@name");
		assertXPath("DOM", bindingPath + "/contextMapper/@soapHeadersType");
		assertXPath("true", bindingPath + "/messageComposer/@unwrapped");
		assertXPath("hello.wsdl", bindingPath + "/wsdl");
		assertXPath("1234", bindingPath + "/wsdlPort");
		assertXPath("${propValue}:4321", bindingPath + "/endpointAddress");
		assertXPath("true", bindingPath + "/mtom/@enabled");
		assertXPath("963", bindingPath + "/mtom/@threshold");
		assertXPath("true", bindingPath + "/mtom/@xopExpand");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SOAPBindingPage page = properties.selectSOAPBinding("soap-binding");
		assertEquals("soap-binding", page.getName());
		assertEquals("${propValue}:4321", page.getEndpointAddress());
		properties.ok();
	}

	@Test
	public void sqlBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SQL");
		SQLBindingPage wizard = new SQLBindingPage();
		assertEquals("sql1", wizard.getName());
		wizard.setName("sql-binding");
		wizard.getQuery().setText("sql-query");
		wizard.getDataSource().setText("data-source");
		wizard.getPlaceholder().setText("place-holder");
		wizard.getPeriod().setText("10");
		wizard.getInitialDelayMS().setText("1234");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sql";
		assertXPath("sql-binding", bindingPath + "/@name");
		assertXPath("1234", bindingPath + "/@initialDelay");
		assertXPath("10", bindingPath + "/@period");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("sql-query", bindingPath + "/query");
		assertXPath("data-source", bindingPath + "/dataSourceRef");
		assertXPath("place-holder", bindingPath + "/placeholder");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SQLBindingPage page = properties.selectSQLBinding("sql-binding");
		assertEquals("sql-binding", page.getName());
		assertEquals("sql-query", page.getQuery().getText());
		assertEquals("data-source", page.getDataSource().getText());
		properties.ok();
	}

	@Test
	public void sqlReferenceBindingTest() throws Exception {
		new Service(REFERENCE_SERVICE).addBinding("SQL");
		SQLBindingPage wizard = new SQLBindingPage();
		assertEquals("sql1", wizard.getName());
		wizard.setName("sql-binding");
		wizard.getQuery().setText("sql-query");
		wizard.getDataSource().setText("data-source");
		wizard.getPlaceholder().setText("place-holder");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sql";
		assertXPath("sql-binding", bindingPath + "/@name");
		assertXPath("sql-query", bindingPath + "/query");
		assertXPath("data-source", bindingPath + "/dataSourceRef");

		BindingsPage properties = new Service(REFERENCE_SERVICE).showProperties().selectBindings();
		SQLBindingPage page = properties.selectSQLBinding("sql-binding");
		assertEquals("sql-binding", page.getName());
		assertEquals("sql-query", page.getQuery().getText());
		assertEquals("data-source", page.getDataSource().getText());
		properties.ok();
	}

	@Test
	public void schedulingBindingTest() throws Exception {
		String cron = "0 0 12 * * ?";
		String startTime = "2014-01-01T00:00:00";
		String endTime = "2015-01-01T00:00:00";

		new Service(SERVICE).addBinding("Scheduling");
		SchedulingBindingPage wizard = new SchedulingBindingPage();
		wizard.setName("schedule-binding");
		wizard.getSchedulingType().setSelection(SCHEDULING_TYPE_CRON);
		wizard.getCron().setText(cron);
		wizard.getStartTime().setText(startTime);
		wizard.getEndTime().setText(endTime);
		wizard.getTimeZone().setSelection("Europe/Prague");
		wizard.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/service/binding.quartz";
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("schedule-binding", bindingPath + "/name");
		assertXPath("0 0 12 * * ?", bindingPath + "/cron");
		assertXPath("2014-01-01T00:00:00", bindingPath + "/trigger.startTime");
		assertXPath("2015-01-01T00:00:00", bindingPath + "/trigger.endTime");
		assertXPath("Europe/Prague", bindingPath + "/trigger.timeZone");

		BindingsPage properties = new Service(SERVICE).showProperties().selectBindings();
		SchedulingBindingPage page = properties.selectSchedulingBinding("HelloService1");
		assertEquals(cron, page.getCron().getText());
		assertEquals(OPERATION_NAME, page.getOperationSelector());
		assertEquals(METHOD, page.getOperationSelectorValue());
		assertEquals(startTime, page.getStartTime().getText());
		assertEquals(endTime, page.getEndTime().getText());
		properties.ok();
	}

	private void assertXPath(String expected, String xpath) throws IOException {
		assertEquals(editor.getSource(), expected, editor.xpath(xpath));
	}

	private void assertXPath(String expected, String xpath, String knownIssue) throws IOException {
		try {
			assertEquals(editor.getSource(), expected, editor.xpath(xpath));
		} catch (Throwable cause) {
			throw new KnownIssueException(knownIssue, cause);
		}
	}
}
