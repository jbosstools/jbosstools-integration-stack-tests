package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.binding.CXFBindingPage.DATA_FORMAT_MESSAGE;
import static org.jboss.tools.switchyard.reddeer.binding.CXFBindingPage.DATA_FORMAT_PAYLOAD;
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

import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.jface.wizard.WizardDialog;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.swt.impl.button.FinishButton;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
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
import org.jboss.tools.switchyard.reddeer.preference.CompositePropertiesDialog;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
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
@AutoBuilding(false)
@RunWith(RedDeerSuite.class)
public class SwitchYardEditorBindingsTest {

	public static final String CONTEXT_PATH = "Context Path";

	public static final String PROJECT = "binding_project";
	public static final String SERVICE = "HelloService";
	public static final String REFERENCE_SERVICE = "HelloRefService";
	public static final String METHOD = "sayHello";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT;
	public static final String[] GATEWAY_BINDINGS = new String[] {
		"Atom",
		"Camel Core (SEDA/Timer/URI)",
		"CXF",
		"File",
		"File Transfer (FTP/FTPS/SFTP)",
		"HTTP",
		"JCA",
		"JMS",
		"JPA",
		"Mail",
		"MQTT",
		"Network (TCP/UDP)",
		"REST",
		"RSS",
		"SAP",
		"SCA",
		"Scheduling",
		"SOAP",
		"SQL" };

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
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new SwitchYardEditor().save();
		CompositePropertiesDialog properties = new Service("HelloService").showProperties(); 
		properties.selectBindings().removeAll();
		properties.ok();
		properties = new Service("HelloRefService").showProperties();
		properties.selectBindings().removeAll();
		properties.ok();
		new SwitchYardEditor().saveAndClose();
	}

	@Test
	public void atomBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		WizardDialog wizard = new Service(SERVICE).addBinding("Atom");
		AtomBindingPage page = new AtomBindingPage();
		page.setName("atom-binding");
		page.getFeedURI().setText("http://localhost");
		page.getSplitEntries().toggle(false);
		page.getSplitEntries().toggle(true);
		page.getFilter().toggle(false);
		page.getFilter().toggle(true);
		page.getLastUpdateStartingTimestamp().setText(time);
		page.getSortEntriesbyDate().toggle(false);
		page.getSortEntriesbyDate().toggle(true);
		page.getDelayBetweenPolls().setText("1234");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectAtomBinding("atom-binding");
		assertEquals("atom-binding", page.getName());
		properties.ok();
	}

	@Test
	public void atomReferenceBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("Atom");
		AtomBindingPage page = new AtomBindingPage();
		page.setName("atom-binding");
		page.getFeedURI().setText("http://localhost");
		page.getSplitEntries().toggle(false);
		page.getSplitEntries().toggle(true);
		page.getFilter().toggle(false);
		page.getFilter().toggle(true);
		page.getLastUpdateStartingTimestamp().setText(time);
		page.getSortEntriesbyDate().toggle(false);
		page.getSortEntriesbyDate().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.atom";
		assertXPath("atom-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/feedURI");
		assertXPath("true", bindingPath + "/splitEntries");
		assertXPath("true", bindingPath + "/filter");
		assertXPath(time, bindingPath + "/lastUpdate");
		assertXPath("true", bindingPath + "/sortEntries");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectAtomBinding("atom-binding");
		assertEquals("atom-binding", page.getName());
		properties.ok();
	}

	@Test
	public void cxfBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("CXF");
		CXFBindingPage page = new CXFBindingPage();
		page.setName("cxf-binding");
		page.getCXFURI().setText("http://localhost");
		page.getWSDLURL().setText("hello.wsdl");
		page.getDataFormat().setSelection(DATA_FORMAT_POJO);
		page.getServiceClass().setText("myClass.java");
		page.getPortName().setText("port");
		page.getRelayHeaders().toggle(false);
		page.getRelayHeaders().toggle(true);
		page.getWrapped().toggle(false);
		page.getWrapped().toggle(true);
		page.getWrappedStyle().setSelection("false");
		page.getWrappedStyle().setSelection("true");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		properties.ok();
	}

	@Test
	public void cxfBindingPayloadTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("CXF");
		CXFBindingPage page = new CXFBindingPage();
		page.setName("cxf-binding");
		page.getCXFURI().setText("http://localhost");
		page.getWSDLURL().setText("hello.wsdl");
		page.getDataFormat().setSelection(DATA_FORMAT_PAYLOAD);
		page.getServiceClass().setText("myClass.java");
		page.getPortName().setText("port");
		assertFalse("SWITCHYARD-2794", page.getRelayHeaders().isEnabled());
		page.getWrapped().toggle(false);
		page.getWrapped().toggle(true);
		page.getWrappedStyle().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.cxf";
		assertXPath("cxf-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/cxfURI");
		assertXPath("hello.wsdl", bindingPath + "/wsdlURL");
		assertXPath("myClass.java", bindingPath + "/serviceClass");
		assertXPath("port", bindingPath + "/portName");
		assertXPath(DATA_FORMAT_PAYLOAD, bindingPath + "/dataFormat");
		assertXPath("0", "count(" + bindingPath + "/relayHeaders)", "SWITCHYARD-2794");
		assertXPath("true", bindingPath + "/wrapped");
		assertXPath("true", bindingPath + "/wrappedStyle");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		properties.ok();
	}

	@Test
	public void cxfBindingMessageTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("CXF");
		CXFBindingPage page = new CXFBindingPage();
		page.setName("cxf-binding");
		page.getCXFURI().setText("http://localhost");
		page.getWSDLURL().setText("hello.wsdl");
		page.getDataFormat().setSelection(DATA_FORMAT_MESSAGE);
		page.getServiceClass().setText("myClass.java");
		page.getPortName().setText("port");
		assertFalse("SWITCHYARD-2794", page.getRelayHeaders().isEnabled());
		page.getWrapped().toggle(false);
		page.getWrapped().toggle(true);
		page.getWrappedStyle().setSelection("false");
		page.getWrappedStyle().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.cxf";
		assertXPath("cxf-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/cxfURI");
		assertXPath("hello.wsdl", bindingPath + "/wsdlURL");
		assertXPath("myClass.java", bindingPath + "/serviceClass");
		assertXPath("port", bindingPath + "/portName");
		assertXPath(DATA_FORMAT_MESSAGE, bindingPath + "/dataFormat");
		assertXPath("0", "count(" + bindingPath + "/relayHeaders)", "SWITCHYARD-2794");
		assertXPath("true", bindingPath + "/wrapped");
		assertXPath("true", bindingPath + "/wrappedStyle");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		properties.ok();
	}

	@Test
	public void cxfReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("CXF");
		CXFBindingPage page = new CXFBindingPage();
		page.setName("cxf-binding");
		page.getCXFURI().setText("http://localhost");
		page.getWSDLURL().setText("hello.wsdl");
		page.getDataFormat().setSelection(DATA_FORMAT_POJO);
		page.getServiceClass().setText("myClass.java");
		page.getServiceName().setText("MyClass");
		page.getPortName().setText("port");
		page.getRelayHeaders().toggle(false);
		page.getRelayHeaders().toggle(true);
		page.getWrapped().toggle(false);
		page.getWrapped().toggle(true);
		page.getWrappedStyle().setSelection("false");
		page.getWrappedStyle().setSelection("true");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getDefaultOperationName().setText("foo");
		page.getDefaultOperationNamespace().setText("http://foo");
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		assertEquals("http://localhost", page.getCXFURI().getText());
		assertEquals("hello.wsdl", page.getWSDLURL().getText());
		assertEquals("POJO", page.getDataFormat().getSelection());
		assertEquals("myClass.java", page.getServiceClass().getText());
		assertEquals("MyClass", page.getServiceName().getText());
		properties.ok();
	}

	@Test
	public void cxfReferenceBindingPayloadTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("CXF");
		CXFBindingPage page = new CXFBindingPage();
		page.setName("cxf-binding");
		page.getCXFURI().setText("http://localhost");
		page.getWSDLURL().setText("hello.wsdl");
		page.getDataFormat().setSelection(DATA_FORMAT_PAYLOAD);
		page.getServiceClass().setText("myClass.java");
		page.getServiceName().setText("MyClass");
		page.getPortName().setText("port");
		assertFalse("SWITCHYARD-2794", page.getRelayHeaders().isEnabled());
		page.getWrapped().toggle(false);
		page.getWrapped().toggle(true);
		page.getWrappedStyle().setSelection("false");
		page.getWrappedStyle().setSelection("true");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getDefaultOperationName().setText("foo");
		page.getDefaultOperationNamespace().setText("http://foo");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.cxf";
		assertXPath("cxf-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/cxfURI");
		assertXPath("hello.wsdl", bindingPath + "/wsdlURL");
		assertXPath("myClass.java", bindingPath + "/serviceClass");
		assertXPath("MyClass", bindingPath + "/serviceName");
		assertXPath("port", bindingPath + "/portName");
		assertXPath(DATA_FORMAT_PAYLOAD, bindingPath + "/dataFormat");
		assertXPath("0", "count(" + bindingPath + "/relayHeaders)", "SWITCHYARD-2794");
		assertXPath("true", bindingPath + "/wrapped");
		assertXPath("true", bindingPath + "/wrappedStyle");
		assertXPath("admin", bindingPath + "/username");
		assertXPath("admin123$", bindingPath + "/password");
		assertXPath("foo", bindingPath + "/defaultOperationName");
		assertXPath("http://foo", bindingPath + "/defaultOperationNamespace");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectCXFBinding("cxf-binding");
		assertEquals("cxf-binding", page.getName());
		assertEquals("http://localhost", page.getCXFURI().getText());
		assertEquals("hello.wsdl", page.getWSDLURL().getText());
		assertEquals(DATA_FORMAT_PAYLOAD, page.getDataFormat().getSelection());
		assertEquals("myClass.java", page.getServiceClass().getText());
		assertEquals("MyClass", page.getServiceName().getText());
		properties.ok();
	}

	@Test
	public void camelBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("Camel");
		CamelBindingPage page = new CamelBindingPage();
		assertEquals("camel1", page.getName());
		page.setName("camel-binding");
		page.getConfigURI().setText("camel-uri");
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.uri";
		assertXPath("camel-binding", bindingPath + "/@name");
		assertXPath("camel-uri", bindingPath + "/@configURI");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectCamelBinding("camel-binding");
		assertEquals("camel-binding", page.getName());
		assertEquals("camel-uri", page.getConfigURI().getText());
		assertEquals(OPERATION_NAME, page.getOperationSelector());
		assertEquals(METHOD, page.getOperationSelectorValue());
		properties.ok();
	}

	@Test
	public void camelReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("Camel");
		CamelBindingPage page = new CamelBindingPage();
		assertEquals("camel1", page.getName());
		page.setName("camel-binding");
		page.getConfigURI().setText("camel-uri");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.uri";
		assertXPath("camel-binding", bindingPath + "/@name");
		assertXPath("camel-uri", bindingPath + "/@configURI");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectCamelBinding("camel-binding");
		assertEquals("camel-binding", page.getName());
		assertEquals("camel-uri", page.getConfigURI().getText());
		properties.ok();
	}

	 @Test
	public void ftpBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("FTP");
		FTPBindingPage page = new FTPBindingPage();
		assertEquals("ftp1", page.getName());
		page.setName("ftp-binding");
		page.getHost().setText("myhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getUseBinaryTransferMode().toggle(true);
		page.getDirectory().setText("ftp-directory");
		page.getFileName().setText("fileName");
		page.getAutoCreateMissingDirectoriesinFilePath().click();
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getInclude().setText("include");
		page.getExclude().setText("exclude");
		page.getDeleteFilesOnceProcessed().toggle(true);
		page.getProcessSubDirectoriesRecursively().toggle(true);
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		page.getPreMove().setText("preMove");
		page.getMove().setText("move");
		page.getMoveFailed().setText("moveFailed");
		page.getDelayBetweenPolls().setText("3000");
		page.getMaxMessagesPerPoll().setText("10");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectFTPBinding("ftp-binding");
		assertEquals("ftp-binding", page.getName());
		assertEquals("ftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	 @Test
	public void ftpReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("FTP");
		FTPBindingPage page = new FTPBindingPage();
		assertEquals("ftp1", page.getName());
		page.setName("ftp-binding");
		page.getHost().setText("myhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getUseBinaryTransferMode().toggle(true);
		page.getDirectory().setText("ftp-directory");
		page.getFileName().setText("fileName");
		page.getAutoCreateMissingDirectoriesinFilePath().click();
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getFileExist().setText("exist");
		page.getTempPrefix().setText("prefix");
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectFTPBinding("ftp-binding");
		assertEquals("ftp-binding", page.getName());
		assertEquals("myhost", page.getHost().getText());
		assertEquals("admin", page.getUserName().getText());
		assertEquals("admin123$", page.getPassword().getText());
		assertEquals("ftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	 @Test
	public void ftpsBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("FTPS");
		FTPSBindingPage page = new FTPSBindingPage();
		assertEquals("ftps1", page.getName());
		page.setName("ftps-binding");
		page.getHost().setText("localhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getUseBinaryTransferMode().toggle(true);
		page.getDirectory().setText("ftps-directory");
		page.getFileName().setText("filename");
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getInclude().setText("include");
		page.getExclude().setText("exclude");
		page.getDeleteFilesOnceProcessed().toggle(true);
		page.getProcessSubDirectoriesRecursively().toggle(true);
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		page.getPreMove().setText("pre-move");
		page.getMove().setText("move");
		page.getMoveFailed().setText("move-failed");
		page.getDelayBetweenPolls().setText("1200");
		page.getMaxMessagesPerPoll().setText("3");
		wizard.next();
		page.getSecurityProtocol().setSelection(FTPSBindingPage.SECURITY_PROTOCOL_SSL);
		page.getSecurityProtocol().setSelection(FTPSBindingPage.SECURITY_PROTOCOL_TLS);
		page.getImplicit().toggle(true);
		page.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_C);
		page.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_S);
		page.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_E);
		page.getExecutionProtocol().setSelection(FTPSBindingPage.EXECUTION_PROTOCOL_P);
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectFTPSBinding("ftps-binding");
		assertEquals("ftps-binding", page.getName());
		assertEquals("ftps-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void ftpsReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("FTPS");
		FTPSBindingPage page = new FTPSBindingPage();
		assertEquals("ftps1", page.getName());
		page.setName("ftps-binding");
		page.getHost().setText("localhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getUseBinaryTransferMode().toggle(true);
		page.getDirectory().setText("ftps-directory");
		page.getFileName().setText("filename");
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getFileExist().setText("exist");
		page.getTempPrefix().setText("prefix");
		wizard.next();
		page.getSecurityProtocol().setSelection(SECURITY_PROTOCOL_SSL);
		page.getImplicit().toggle(true);
		page.getExecutionProtocol().setSelection(EXECUTION_PROTOCOL_S);
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectFTPSBinding("ftps-binding");
		assertEquals("ftps-binding", page.getName());
		assertEquals("ftps-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void fileBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("File");
		FileBindingPage page = new FileBindingPage();
		assertEquals("file1", page.getName());
		page.setName("file-binding");
		page.getDirectory().setText("file-directory");
		page.getFileName().setText("test.txt");
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getInclude().setText("inc");
		page.getExclude().setText("ex");
		page.setOperationSelector(OPERATION_NAME, METHOD);
		page.getPreMove().setText("pre");
		page.getMove().setText("processed");
		page.getMoveFailed().setText("failed");
		page.getMaxMessagesPerPoll().setText("10");
		page.getDelayBetweenPolls().setText("963");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectFileBinding("file-binding");
		assertEquals("file-binding", page.getName());
		assertEquals("file-directory", page.getDirectory().getText());
		assertTrue(page.getAutoCreateMissingDirectoriesinFilePath().isChecked());
		properties.ok();
	}

	@Test
	public void fileReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("File");
		FileBindingPage page = new FileBindingPage();
		assertEquals("file1", page.getName());
		page.setName("file-binding");
		page.getDirectory().setText("file-directory");
		page.getFileName().setText("test.txt");
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getFileExist().setText("exist");
		page.getTempPrefix().setText("prefix");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.file";
		assertXPath("file-binding", bindingPath + "/@name");
		assertXPath("file-directory", bindingPath + "/directory");
		assertXPath("test.txt", bindingPath + "/fileName");
		assertXPath("true", bindingPath + "/autoCreate");
		assertXPath("exist", bindingPath + "/produce/fileExist");
		assertXPath("prefix", bindingPath + "/produce/tempPrefix");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectFileBinding("file-binding");
		assertEquals("file-binding", page.getName());
		assertEquals("file-directory", page.getDirectory().getText());
		assertTrue(page.getAutoCreateMissingDirectoriesinFilePath().isChecked());
		properties.ok();
	}

	@Test
	public void httpBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("HTTP");
		HTTPBindingPage page = new HTTPBindingPage();
		assertEquals("http1", page.getName());
		page.setName("http-binding");
		page.getContextPath().setText("http-context");
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.http";
		assertXPath("http-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("http-context", bindingPath + "/contextPath");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectHTTPBinding("http-binding");
		assertEquals("http-binding", page.getName());
		page.setName("http1");
		assertEquals("http-context", page.getContextPath().getText());
		page.getContextPath().setText("hello");
		assertEquals(OPERATION_NAME, page.getOperationSelector());
		assertEquals(METHOD, page.getOperationSelectorValue());
		page.setOperationSelector(XPATH, "/hello/method");
		properties.ok();

		new SwitchYardEditor().save();

		assertXPath("http1", bindingPath + "/@name");
		assertXPath("/hello/method", bindingPath + "/operationSelector.xpath/@expression");
		assertXPath("hello", bindingPath + "/contextPath");
	}

	@Test
	public void httpReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("HTTP");
		HTTPBindingPage page = new HTTPBindingPage();
		assertEquals("http1", page.getName());
		page.setName("http-binding");
		page.getAddress().setText("http://localhost");
		page.getMethod().setSelection("GET");
		page.getContentType().setText("coolType");
		page.getRequestTimeout().setText("1234");
		wizard.next();
		page.getAuthenticationType().setSelection("Basic");
		page.getUser().setText("admin");
		page.getPassword().setText("admin123");
		page.getHost().setText("localhost");
		page.getPort().setText("8081");
		wizard.next();
		page.getUserName().setText("proxyAdmin");
		page.getPassword().setText("proxyAdmin123");
		page.getHost().setText("proxyhost");
		page.getPort().setText("1234");
		wizard.finish();

		new SwitchYardEditor().saveAndClose();
		editor = new SwitchYardProject(PROJECT).openSwitchYardFile();

		String bindingPath = "/switchyard/composite/reference/binding.http";
		assertXPath("http-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/address");
		assertXPath("admin", bindingPath + "/basic/user");
		assertXPath("admin123", bindingPath + "/basic/password");
		assertXPath("localhost", bindingPath + "/basic/host");
		assertXPath("8081", bindingPath + "/basic/port");
		assertXPath("proxyAdmin", bindingPath + "/proxy/user");
		assertXPath("proxyAdmin123", bindingPath + "/proxy/password");
		assertXPath("proxyhost", bindingPath + "/proxy/host");
		assertXPath("1234", bindingPath + "/proxy/port");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectHTTPBinding("http-binding");
		assertEquals("http-binding", page.getName());
		page.setName("http1");
		assertEquals("http://localhost", page.getAddress().getText());
		page.getAddress().setText("http://foo");
		page.selectAuthenticationDetails();
		assertEquals("admin", page.getUser().getText());
		assertEquals("admin123", page.getPassword().getText());
		assertEquals("localhost", page.getHost().getText());
		assertEquals("8081", page.getPort().getText());
		page.getPort().setText("8181");
		page.selectProxySettings();
		assertEquals("proxyAdmin", page.getUserName().getText());
		assertEquals("proxyAdmin123", page.getPassword().getText());
		assertEquals("proxyhost", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		page.getPort().setText("${proxyPort}");
		properties.ok();

		new SwitchYardEditor().save();

		assertXPath("http1", bindingPath + "/@name");
		assertXPath("http://foo", bindingPath + "/address");
		assertXPath("GET", bindingPath + "/method");
		assertXPath("coolType", bindingPath + "/contentType");
		assertXPath("1234", bindingPath + "/timeout");
		assertXPath("admin", bindingPath + "/basic/user");
		assertXPath("admin123", bindingPath + "/basic/password");
		assertXPath("localhost", bindingPath + "/basic/host");
		assertXPath("8181", bindingPath + "/basic/port");
		assertXPath("proxyAdmin", bindingPath + "/proxy/user");
		assertXPath("proxyAdmin123", bindingPath + "/proxy/password");
		assertXPath("proxyhost", bindingPath + "/proxy/host");
		assertXPath("${proxyPort}", bindingPath + "/proxy/port");
	}

	@Test
	public void jcaBindingGenericTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("JCA");
		JCABindingPage page = new JCABindingPage();
		assertEquals("jca1", page.getName());
		page.setName("jca-binding");
		page.getResourceAdapterType().setSelection(RESOURCE_ADAPTER_GENERIC);
		page.getResourceAdapterArchive().setText("generic-ra.rar");
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		page.getEndpointMappingType().setSelection(ENDPOINT_JMS);
		page.getTransacted().setText("false");
		page.getTransacted().setText("true");
		page.getBatchSize().setText("123");
		page.getBatchTimeoutin().setText("2000");
		page.getConnectionFactoryJNDIName().setText("jndiName");
		page.getJNDIPropertiesFileName().setText("jndi.properties");
		page.getDestinationType().setSelection("javax.jms.Queue");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jca";
		assertXPath("jca-binding", bindingPath + "/@name");
		assertXPath(METHOD, bindingPath + "/operationSelector/@operationName");
		assertXPath("generic-ra.rar", bindingPath + "/inboundConnection/resourceAdapter/@name");
		assertXPath("javax.jms.Queue",
				bindingPath + "/inboundConnection/activationSpec/property[@name='destinationType']/@value");
		assertXPath("queue/YourQueueName",
				bindingPath + "/inboundConnection/activationSpec/property[@name='destination']/@value");
		assertXPath("", bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector']/@value");
		assertXPath("org.switchyard.component.jca.endpoint.JMSEndpoint",
				bindingPath + "/inboundInteraction/endpoint/@type");
		assertXPath("true", bindingPath + "/inboundInteraction/transacted");
		assertXPath("123", bindingPath + "/inboundInteraction/batchCommit/@batchSize");
		assertXPath("2000", bindingPath + "/inboundInteraction/batchCommit/@batchTimeout");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectJCABinding("jca-binding");
		assertEquals("jca-binding", page.getName());
		assertEquals(RESOURCE_ADAPTER_GENERIC, page.getResourceAdapterType().getSelection());
		assertEquals("generic-ra.rar", page.getResourceAdapterArchive().getText());
		properties.ok();
	}

	 @Test
	public void jcaBindingHornetQueueTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("JCA");
		JCABindingPage page = new JCABindingPage();
		assertEquals("jca1", page.getName());
		page.setName("jca-binding");
		page.getResourceAdapterType().setSelection(RESOURCE_ADAPTER_HORNETQ_QUEUE);
		page.getDestinationQueue().setText("queue/MyQueue");
		page.getMessageSelector().setText("ms");
		page.getAcknowledgeMode().setSelection(ACKNOWLEDGE_MODE_DUPS_OK);
		page.setOperationSelector(REGEX, "say[H|h]ello");
		wizard.next();
		page.getEndpointMappingType().setSelection(ENDPOINT_JMS);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jca";
		assertXPath("jca-binding", bindingPath + "/@name");
		assertXPath("say[H|h]ello", bindingPath + "/operationSelector.regex/@expression");
		assertXPath("hornetq-ra.rar", bindingPath + "/inboundConnection/resourceAdapter/@name");
		assertXPath("javax.jms.Queue",
				bindingPath + "/inboundConnection/activationSpec/property[@name='destinationType']/@value");
		assertXPath("queue/MyQueue",
				bindingPath + "/inboundConnection/activationSpec/property[@name='destination']/@value");
		assertXPath("ms", bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector']/@value");
		assertXPath("Dups-ok-acknowledge",
				bindingPath + "/inboundConnection/activationSpec/property[@name='acknowledgeMode']/@value");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectJCABinding("jca-binding");
		assertEquals("jca-binding", page.getName());
		assertTrue(page.getResourceAdapterType().getSelection().equals(RESOURCE_ADAPTER_HORNETQ_QUEUE));
		properties.ok();
	}

	 @Test
	public void jcaBindingHornetTopicTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("JCA");
		JCABindingPage page = new JCABindingPage();
		assertEquals("jca1", page.getName());
		page.setName("jca-binding");
		page.getResourceAdapterType().setSelection(RESOURCE_ADAPTER_HORNETQ_TOPIC);
		page.getDestinationTopic().setText("topic/MyTopic");
		page.getMessageSelector().setText("ms");
		page.getAcknowledgeMode().setSelection(ACKNOWLEDGE_MODE_AUTO);
		page.getSubscriptionDurability().setSelection(SUBSCRIPTION_NONDURABLE);
		page.getClientID().setText("clientID");
		page.getSubscriptionName().setText("sub-name");
		page.setOperationSelector(JAVA_CLASS, "myClass.java");
		wizard.next();
		page.getEndpointMappingType().setSelection(ENDPOINT_JMS);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.jca";
		assertXPath("jca-binding", bindingPath + "/@name");
		assertXPath("myClass.java", bindingPath + "/operationSelector.java/@class");
		assertXPath("hornetq-ra.rar", bindingPath + "/inboundConnection/resourceAdapter/@name");
		assertXPath("javax.jms.Topic",
				bindingPath + "/inboundConnection/activationSpec/property[@name='destinationType']/@value");
		assertXPath("topic/MyTopic",
				bindingPath + "/inboundConnection/activationSpec/property[@name='destination']/@value");
		assertXPath("ms", bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector']/@value");
		assertXPath(ACKNOWLEDGE_MODE_AUTO,
				bindingPath + "/inboundConnection/activationSpec/property[@name='acknowledgeMode']/@value");
		assertXPath("sub-name",
				bindingPath + "/inboundConnection/activationSpec/property[@name='subscriptionName']/@value");
		assertXPath(SUBSCRIPTION_NONDURABLE,
				bindingPath + "/inboundConnection/activationSpec/property[@name='subscriptionDurability']/@value");
		assertXPath("clientID", bindingPath + "/inboundConnection/activationSpec/property[@name='clientId']/@value");
		assertXPath("1",
				"count(" + bindingPath + "/inboundConnection/activationSpec/property[@name='messageSelector'])");
		assertXPath("1",
				"count(" + bindingPath + "/inboundConnection/activationSpec/property[@name='acknowledgeMode'])");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectJCABinding("jca-binding");
		assertEquals("jca-binding", page.getName());
		assertTrue(page.getResourceAdapterType().getSelection().equals(RESOURCE_ADAPTER_HORNETQ_TOPIC));
		properties.ok();
	}

	@Test
	public void jmsQueueBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("JMS");
		JMSBindingPage page = new JMSBindingPage();
		assertEquals("jms1", page.getName());
		page.setName("jms-binding");
		page.getType().setSelection(TYPE_QUEUE);
		page.getQueueTopicName().setText("myqueue");
		page.getConnectionFactory().setText("#MyFactory");
		page.getConcurrentConsumers().setText("3");
		page.getMaximumConcurrentConsumers().setText("7");
		page.getReplyTo().setText("reply-to");
		page.getSelector().setText("selector");
		page.getTransactionManager().setText("MyTX");
		page.getTransacted().toggle(false);
		page.getTransacted().toggle(true);
		page.setOperationSelector(OPERATION_NAME, METHOD);
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("myqueue", page.getQueueTopicName().getText());
		properties.ok();
	}

	@Test
	public void jmsQueueReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("JMS");
		JMSBindingPage page = new JMSBindingPage();
		assertEquals("jms1", page.getName());
		page.setName("jms-binding");
		page.getType().setSelection(TYPE_QUEUE);
		page.getQueueTopicName().setText("myqueue");
		page.getConnectionFactory().setText("#MyFactory");
		page.getConcurrentConsumers().setText("3");
		page.getMaximumConcurrentConsumers().setText("7");
		page.getReplyTo().setText("reply-to");
		page.getSelector().setText("selector");
		page.getTransactionManager().setText("MyTX");
		page.getTransacted().toggle(false);
		page.getTransacted().toggle(true);
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("myqueue", page.getQueueTopicName().getText());

		properties.ok();
	}

	@Test
	public void jmsTopicBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("JMS");
		JMSBindingPage page = new JMSBindingPage();
		assertEquals("jms1", page.getName());
		page.setName("jms-binding");
		page.getType().setSelection(TYPE_TOPIC);
		page.getQueueTopicName().setText("mytopic");
		page.getConnectionFactory().setText("#MyFactory");
		page.getConcurrentConsumers().setText("3");
		page.getMaximumConcurrentConsumers().setText("7");
		page.getReplyTo().setText("reply-to");
		page.getSelector().setText("selector");
		page.getTransactionManager().setText("MyTX");
		page.getTransacted().toggle(true);
		page.getTransacted().toggle(false);
		page.setOperationSelector(OPERATION_NAME, METHOD);
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("mytopic", page.getQueueTopicName().getText());
		properties.ok();
	}

	// @Test
	public void jmsTopicReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("JMS");
		JMSBindingPage page = new JMSBindingPage();
		assertEquals("jms1", page.getName());
		page.setName("jms-binding");
		page.getType().setSelection(TYPE_TOPIC);
		page.getQueueTopicName().setText("mytopic");
		page.getConnectionFactory().setText("#MyFactory");
		page.getConcurrentConsumers().setText("3");
		page.getMaximumConcurrentConsumers().setText("7");
		page.getReplyTo().setText("reply-to");
		page.getSelector().setText("selector");
		page.getTransactionManager().setText("MyTX");
		page.getTransacted().toggle(true);
		page.getTransacted().toggle(false);
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectJMSBinding("jms-binding");
		assertEquals("jms-binding", page.getName());
		assertEquals("mytopic", page.getQueueTopicName().getText());
		properties.ok();
	}

	@Test
	public void jpaBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("JPA");
		JPABindingPage page = new JPABindingPage();
		assertEquals("jpa1", page.getName());
		page.setName("jpa-binding");
		page.getEntityClassName().setText("EClass.java");
		page.getPersistenceUnit().setText("persistence.xml");
		page.getTransactionManager().setText("myTX");
		page.getDelete().toggle(false);
		page.getDelete().toggle(true);
		page.getLockEntity().toggle(false);
		page.getLockEntity().toggle(true);
		page.getMaximumResults().setText("5");
		page.getQuery().setText("query");
		page.getNamedQuery().setText("named-query");
		page.getNativeQuery().setText("native-query");
		page.getTransacted().toggle(false);
		page.getTransacted().toggle(true);
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectJPABinding("jpa-binding");
		assertEquals("jpa-binding", page.getName());
		assertEquals("EClass.java", page.getEntityClassName().getText());
		assertEquals("persistence.xml", page.getPersistenceUnit().getText());
		properties.ok();
	}

	// @Test
	public void jpaReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("JPA");
		JPABindingPage page = new JPABindingPage();
		assertEquals("jpa1", page.getName());
		page.setName("jpa-binding");
		page.getEntityClassName().setText("EClass.java");
		page.getPersistenceUnit().setText("persistence.xml");
		page.getTransactionManager().setText("myTX");
		page.getFlushonSend().toggle(false);
		page.getFlushonSend().toggle(true);
		page.getUsePersist().toggle(false);
		page.getUsePersist().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.jpa";
		assertXPath("jpa-binding", bindingPath + "/@name");
		assertXPath("EClass.java", bindingPath + "/entityClassName");
		assertXPath("persistence.xml", bindingPath + "/persistenceUnit");
		assertXPath("myTX", bindingPath + "/transactionManager");
		assertXPath("true", bindingPath + "/produce/flushOnSend");
		assertXPath("true", bindingPath + "/produce/usePersist");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectJPABinding("jpa-binding");
		assertEquals("jpa-binding", page.getName());
		assertEquals("EClass.java", page.getEntityClassName().getText());
		assertEquals("persistence.xml", page.getPersistenceUnit().getText());
		properties.ok();
	}

	@Test
	public void mqttBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("MQTT");
		MQTTBindingPage page = new MQTTBindingPage();
		page.setName("mqtt-binding");
		page.getHostURI().setText("tcp://localhost:1883");
		page.getSubscribeTopicName().setText("topicName");
		page.getConnectAttemptsMax().setText("111");
		page.getReconnectAttemptsMax().setText("222");
		page.getQualityofService().setSelection(QOS_EXACTLY_ONCE);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.mqtt";
		assertXPath("mqtt-binding", bindingPath + "/@name");
		assertXPath("tcp://localhost:1883", bindingPath + "/host");
		assertXPath("111", bindingPath + "/connectAttemptsMax");
		assertXPath("222", bindingPath + "/reconnectAttemptsMax");
		assertXPath(QOS_EXACTLY_ONCE, bindingPath + "/qualityOfService");
		assertXPath("topicName", bindingPath + "/subscribeTopicName");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectMQTTBinding("mqtt-binding");
		assertEquals("mqtt-binding", page.getName());
		properties.ok();
	}

	@Test
	public void mqttReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("MQTT");
		MQTTBindingPage page = new MQTTBindingPage();
		page.setName("mqtt-binding");
		page.getHostURI().setText("tcp://localhost:1883");
		page.getPublishTopicName().setText("topicName");
		page.getConnectAttemptsMax().setText("111");
		page.getReconnectAttemptsMax().setText("222");
		page.getQualityofService().setSelection(QOS_EXACTLY_ONCE);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.mqtt";
		assertXPath("mqtt-binding", bindingPath + "/@name");
		assertXPath("tcp://localhost:1883", bindingPath + "/host");
		assertXPath("111", bindingPath + "/connectAttemptsMax");
		assertXPath("222", bindingPath + "/reconnectAttemptsMax");
		assertXPath(QOS_EXACTLY_ONCE, bindingPath + "/qualityOfService");
		assertXPath("topicName", bindingPath + "/publishTopicName");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectMQTTBinding("mqtt-binding");
		assertEquals("mqtt-binding", page.getName());
		properties.ok();
	}

	@Test
	public void mailBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("Mail");
		MailBindingPage page = new MailBindingPage();
		assertEquals("mail1", page.getName());
		page.setName("mail-binding");
		page.getHost().setText("localhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getSecured().toggle(false);
		page.getSecured().toggle(true);
		page.getAccountType().setSelection(ACCOUNT_TYPE_IMAP);
		page.getFolderName().setText("inbox");
		page.getFetchSize().setText("3");
		page.getUnreadOnly().toggle(false);
		page.getUnreadOnly().toggle(true);
		page.getDelete().toggle(false);
		page.getDelete().toggle(true);
		page.setOperationSelector(OPERATION_NAME, METHOD);
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectMailBinding("mail-binding");
		assertEquals("mail-binding", page.getName());
		assertEquals("localhost", page.getHost().getText());
		properties.ok();
	}

	@Test
	public void mailReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("Mail");
		MailBindingPage page = new MailBindingPage();
		assertEquals("mail1", page.getName());
		page.setName("mail-binding");
		page.getHost().setText("localhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getSecured().toggle(false);
		page.getSecured().toggle(true);
		page.getSubject().setText("subject");
		page.getFrom().setText("from");
		page.getTo().setText("to");
		page.getCC().setText("cc");
		page.getBCC().setText("bcc");
		page.getReplyTo().setText("replyto");
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectMailBinding("mail-binding");
		assertEquals("mail-binding", page.getName());
		assertEquals("localhost", page.getHost().getText());
		properties.ok();
	}

	@Test
	public void nettyTcpBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("Netty TCP");
		NettyTCPBindingPage page = new NettyTCPBindingPage();
		assertEquals("tcp1", page.getName());
		page.setName("tcp-binding");
		page.getHost().setText("tcp-host");
		page.getPort().setText("1234");
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.tcp";
		assertXPath("tcp-binding", bindingPath + "/@name");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("tcp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectNettyTCPBinding("tcp-binding");
		assertEquals("tcp-binding", page.getName());
		assertEquals("tcp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void nettyTcpReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("Netty TCP");
		NettyTCPBindingPage page = new NettyTCPBindingPage();
		assertEquals("tcp1", page.getName());
		page.setName("tcp-binding");
		page.getHost().setText("tcp-host");
		page.getPort().setText("1234");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.tcp";
		assertXPath("tcp-binding", bindingPath + "/@name");
		assertXPath("tcp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectNettyTCPBinding("tcp-binding");
		assertEquals("tcp-binding", page.getName());
		assertEquals("tcp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void nettyUdpBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("Netty UDP");
		NettyUDPBindingPage page = new NettyUDPBindingPage();
		assertEquals("udp1", page.getName());
		page.setName("udp-binding");
		page.getHost().setText("udp-host");
		page.getPort().setText("1234");
		page.getBroadcast().toggle(false);
		page.getBroadcast().toggle(true);
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.udp";
		assertXPath("udp-binding", bindingPath + "/@name");
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("udp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("true", bindingPath + "/broadcast");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectNettyUDPBinding("udp-binding");
		assertEquals("udp-binding", page.getName());
		assertEquals("udp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void nettyUdpReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("Netty UDP");
		NettyUDPBindingPage page = new NettyUDPBindingPage();
		assertEquals("udp1", page.getName());
		page.setName("udp-binding");
		page.getHost().setText("udp-host");
		page.getPort().setText("1234");
		page.getBroadcast().toggle(false);
		page.getBroadcast().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.udp";
		assertXPath("udp-binding", bindingPath + "/@name");
		assertXPath("udp-host", bindingPath + "/host");
		assertXPath("1234", bindingPath + "/port");
		assertXPath("true", bindingPath + "/broadcast");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectNettyUDPBinding("udp-binding");
		assertEquals("udp-binding", page.getName());
		assertEquals("udp-host", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	 @Test
	public void restBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("REST");
		RESTBindingPage page = new RESTBindingPage();
		assertEquals("rest1", page.getName());
		page.setName("rest-binding");
		page.getContextPath().setText("rest-context");
		page.addInterface("Hello");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.rest";
		assertXPath("rest-binding", bindingPath + "/@name");
		assertXPath(PACKAGE + ".Hello", bindingPath + "/interfaces");
		assertXPath("rest-context", bindingPath + "/contextPath");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectRESTBinding("rest-binding");
		assertEquals("rest-binding", page.getName());
		assertEquals("rest-context", page.getContextPath().getText());
		assertTrue(page.getInterfaces().contains(PACKAGE + ".Hello"));
		properties.ok();
	}

	 @Test
	public void restReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("REST");
		RESTBindingPage page = new RESTBindingPage();
		assertEquals("rest1", page.getName());
		page.setName("rest-binding");
		page.getAddress().setText("http://localhost");
		page.addInterface("Hello");
		wizard.next();
		page.getAuthenticationType().setSelection("Basic");
		page.getUser().setText("admin");
		page.getPassword().setText("admin123");
		page.getHost().setText("localhost");
		page.getPort().setText("8081");
		wizard.next();
		page.getUserName().setText("proxyAdmin");
		page.getPassword().setText("proxyAdmin123");
		page.getHost().setText("proxyhost");
		page.getPort().setText("1234");
		wizard.finish();

		new SwitchYardEditor().saveAndClose();
		editor = new SwitchYardProject(PROJECT).openSwitchYardFile();

		String bindingPath = "/switchyard/composite/reference/binding.rest";
		assertXPath("rest-binding", bindingPath + "/@name");
		assertXPath(PACKAGE + ".Hello", bindingPath + "/interfaces");
		assertXPath("http://localhost", bindingPath + "/address");
		assertXPath("admin", bindingPath + "/basic/user");
		assertXPath("admin123", bindingPath + "/basic/password");
		assertXPath("localhost", bindingPath + "/basic/host");
		assertXPath("8081", bindingPath + "/basic/port");
		assertXPath("proxyAdmin", bindingPath + "/proxy/user");
		assertXPath("proxyAdmin123", bindingPath + "/proxy/password");
		assertXPath("proxyhost", bindingPath + "/proxy/host");
		assertXPath("1234", bindingPath + "/proxy/port");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectRESTBinding("rest-binding");
		assertEquals("rest-binding", page.getName());
		assertEquals("http://localhost", page.getAddress().getText());
		assertTrue(page.getInterfaces().contains(PACKAGE + ".Hello"));

		page.selectAuthenticationDetails();
		assertEquals("admin", page.getUser().getText());
		assertEquals("admin123", page.getPassword().getText());
		assertEquals("localhost", page.getHost().getText());
		assertEquals("8081", page.getPort().getText());

		page.selectProxySettings();
		assertEquals("proxyAdmin", page.getUserName().getText());
		assertEquals("proxyAdmin123", page.getPassword().getText());
		assertEquals("proxyhost", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());

		properties.ok();
	}

	@Test
	public void rssBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		WizardDialog wizard = new Service(SERVICE).addBinding("RSS");
		RSSBindingPage page = new RSSBindingPage();
		page.setName("rss-binding");
		page.getFeedURI().setText("http://localhost");
		page.getSplitEntries().toggle(false);
		page.getSplitEntries().toggle(true);
		page.getFilter().toggle(false);
		page.getFilter().toggle(true);
		page.getLastUpdateStartingTimestamp().setText(time);
		page.getSortEntriesbyDate().toggle(false);
		page.getSortEntriesbyDate().toggle(true);
		page.getDelayBetweenPolls().setText("1234");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectRSSBinding("rss-binding");
		assertEquals("rss-binding", page.getName());
		properties.ok();
	}

	// @Test
	public void rssReferenceBindingTest() throws Exception {
		String time = "2015-01-01T00:00:00";

		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("RSS");
		RSSBindingPage page = new RSSBindingPage();
		page.setName("rss-binding");
		page.getFeedURI().setText("http://localhost");
		page.getSplitEntries().toggle(false);
		page.getSplitEntries().toggle(true);
		page.getFilter().toggle(false);
		page.getFilter().toggle(true);
		page.getLastUpdateStartingTimestamp().setText(time);
		page.getSortEntriesbyDate().toggle(false);
		page.getSortEntriesbyDate().toggle(true);
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.rss";
		assertXPath("rss-binding", bindingPath + "/@name");
		assertXPath("http://localhost", bindingPath + "/feedURI");
		assertXPath("true", bindingPath + "/splitEntries");
		assertXPath("true", bindingPath + "/filter");
		assertXPath("2015-01-01T00:00:00", bindingPath + "/lastUpdate");
		assertXPath("true", bindingPath + "/sortEntries");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectRSSBinding("rss-binding");
		assertEquals("rss-binding", page.getName());
		properties.ok();
	}

	@Test
	public void sapIDocListBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_IDOC_LIST);
		page.getServer().setText("localhost");
		page.getIDocType().setText("docType");
		page.getIDocTypeExtension().setText("ext");
		page.getSystemRelease().setText("sysRel");
		page.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("localhost", bindingPath + "/idoclist-server/serverName");
		assertXPath("docType", bindingPath + "/idoclist-server/idocType");
		assertXPath("ext", bindingPath + "/idoclist-server/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/idoclist-server/systemRelease");
		assertXPath("appRel", bindingPath + "/idoclist-server/applicationRelease");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
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
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_IDOC_LIST);
		page.getDestinationName().setText("example.com");
		page.getIDocType().setText("docType");
		page.getIDocTypeExtension().setText("ext");
		page.getSystemRelease().setText("sysRel");
		page.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/idoclist-destination/destinationName", "SWITCHYARD-2730");
		assertXPath("docType", bindingPath + "/idoclist-destination/idocType");
		assertXPath("ext", bindingPath + "/idoclist-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/idoclist-destination/systemRelease");
		assertXPath("appRel", bindingPath + "/idoclist-destination/applicationRelease");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
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
		WizardDialog wizard = new Service(SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_SRFC);
		page.getServer().setText("localhost");
		page.getRFCName().setText("srfcName");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("localhost", bindingPath + "/srfc-server/serverName");
		assertXPath("srfcName", bindingPath + "/srfc-server/rfcName");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("localhost", page.getServer().getText());
		assertEquals("srfcName", page.getRFCName().getText());
		properties.ok();
	}

	@Test
	public void sapSRFCReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_SRFC);
		page.getDestinationName().setText("example.com");
		page.getRFCName().setText("srfcName");
		page.getTransacted().setText("${myVar}");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/srfc-destination/destinationName");
		assertXPath("srfcName", bindingPath + "/srfc-destination/rfcName");
		assertXPath("${myVar}", bindingPath + "/srfc-destination/transacted");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("srfcName", page.getRFCName().getText());
		assertEquals("${myVar}", page.getTransacted().getText());
		properties.ok();
	}

	@Test
	public void sapTRFCBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_TRFC);
		page.getServer().setText("localhost");
		page.getRFCName().setText("trfcName");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("localhost", bindingPath + "/trfc-server/serverName");
		assertXPath("trfcName", bindingPath + "/trfc-server/rfcName");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("localhost", page.getServer().getText());
		assertEquals("trfcName", page.getRFCName().getText());
		properties.ok();
	}

	@Test
	public void sapTRFCReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_TRFC);
		page.getDestinationName().setText("example.com");
		page.getRFCName().setText("trfcName");
		page.getTransacted().setText("${myVar}");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/trfc-destination/destinationName");
		assertXPath("trfcName", bindingPath + "/trfc-destination/rfcName");
		assertXPath("${myVar}", bindingPath + "/trfc-destination/transacted");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("trfcName", page.getRFCName().getText());
		assertEquals("${myVar}", page.getTransacted().getText());
		properties.ok();
	}

	@Test
	public void sapIDocReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_IDOC);
		page.getDestinationName().setText("example.com");
		page.getIDocType().setText("docType");
		page.getIDocTypeExtension().setText("ext");
		page.getSystemRelease().setText("sysRel");
		page.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/idoc-destination/destinationName");
		assertXPath("docType", bindingPath + "/idoc-destination/idocType");
		assertXPath("ext", bindingPath + "/idoc-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/idoc-destination/systemRelease");
		assertXPath("appRel", bindingPath + "/idoc-destination/applicationRelease");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
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
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_QIDOC);
		page.getDestinationName().setText("example.com");
		page.getQueueName().setText("myqueue");
		page.getIDocType().setText("docType");
		page.getIDocTypeExtension().setText("ext");
		page.getSystemRelease().setText("sysRel");
		page.getApplicationRelease().setText("appRel");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/qidoc-destination/destinationName");
		assertXPath("myqueue", bindingPath + "/qidoc-destination/queueName");
		assertXPath("docType", bindingPath + "/qidoc-destination/idocType");
		assertXPath("ext", bindingPath + "/qidoc-destination/idocTypeExtension");
		assertXPath("sysRel", bindingPath + "/qidoc-destination/systemRelease");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
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
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_QIDOC_LIST);
		page.getDestinationName().setText("example.com");
		page.getQueueName().setText("myqueue");
		page.getIDocType().setText("docType");
		page.getIDocTypeExtension().setText("ext");
		page.getSystemRelease().setText("sysRel");
		page.getApplicationRelease().setText("appRel");
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
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
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SAP");
		SAPBindingPage page = new SAPBindingPage();
		page.setName("sap-binding");
		page.getSAPObject().setSelection(SAP_OBJECT_QRFC);
		page.getDestinationName().setText("example.com");
		page.getQueueName().setText("myqueue");
		page.getRFCName().setText("qrfcName");
		page.getTransacted().setText("${myVar}");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sap";
		assertXPath("sap-binding", bindingPath + "/@name");
		assertXPath("example.com", bindingPath + "/qrfc-destination/destinationName");
		assertXPath("myqueue", bindingPath + "/qrfc-destination/queueName");
		assertXPath("qrfcName", bindingPath + "/qrfc-destination/rfcName");
		assertXPath("${myVar}", bindingPath + "/qrfc-destination/transacted");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSAPBinding("sap-binding");
		assertEquals("sap-binding", page.getName());
		assertEquals("example.com", page.getDestinationName().getText());
		assertEquals("myqueue", page.getQueueName().getText());
		assertEquals("qrfcName", page.getRFCName().getText());
		assertEquals("${myVar}", page.getTransacted().getText());
		properties.ok();
	}

	@Test
	public void scaBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("SCA");
		SCABindingPage page = new SCABindingPage();
		assertEquals("sca1", page.getName());
		page.setName("sca-binding");
		page.getClustered().setSelection("false");
		page.getClustered().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/service/binding.sca";
		assertXPath("sca-binding", bindingPath + "/@name");
		assertXPath("true", bindingPath + "/@clustered");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSCABinding("sca-binding");
		assertEquals("sca-binding", page.getName());
		assertEquals("true", page.getClustered().getText());
		properties.ok();
	}

	@Test
	public void scaReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SCA");
		SCABindingPage page = new SCABindingPage();
		assertEquals("sca1", page.getName());
		page.setName("sca-binding");
		page.getClustered().setSelection("false");
		page.getClustered().setSelection("true");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sca";
		assertXPath("sca-binding", bindingPath + "/@name");
		assertXPath("true", bindingPath + "/@clustered");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSCABinding("sca-binding");
		assertEquals("sca-binding", page.getName());
		assertEquals("true", page.getClustered().getText());
		properties.ok();
	}

	@Test
	public void sftpBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("SFTP");
		SFTPBindingPage page = new SFTPBindingPage();
		assertEquals("sftp1", page.getName());
		page.setName("sftp-binding");
		page.getHost().setText("localhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getUseBinaryTransferMode().toggle(false);
		page.getUseBinaryTransferMode().toggle(true);
		page.getDirectory().setText("sftp-directory");
		page.getFileName().setText("test.txt");
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getInclude().setText("in");
		page.getExclude().setText("ex");
		page.getDeleteFilesOnceProcessed().toggle(false);
		page.getDeleteFilesOnceProcessed().toggle(true);
		page.getProcessSubDirectoriesRecursively().toggle(false);
		page.getProcessSubDirectoriesRecursively().toggle(true);
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.next();
		page.getPreMove().setText("preMove");
		page.getMove().setText("move");
		page.getMoveFailed().setText("moveFailed");
		page.getDelayBetweenPolls().setText("1000");
		page.getMaxMessagesPerPoll().setText("2");
		wizard.next();
		page.getPrivateKeyFile().setText("private.key");
		page.getPrivateKeyFilePassphrase().setText("secret");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSFTPBinding("sftp-binding");
		assertEquals("sftp-binding", page.getName());
		assertEquals("sftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void sftpReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SFTP");
		SFTPBindingPage page = new SFTPBindingPage();
		assertEquals("sftp1", page.getName());
		page.setName("sftp-binding");
		page.getHost().setText("localhost");
		page.getPort().setText("1234");
		page.getUserName().setText("admin");
		page.getPassword().setText("admin123$");
		page.getUseBinaryTransferMode().toggle(false);
		page.getUseBinaryTransferMode().toggle(true);
		page.getDirectory().setText("sftp-directory");
		page.getFileName().setText("test.txt");
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(false);
		page.getAutoCreateMissingDirectoriesinFilePath().toggle(true);
		page.getFileExist().setText("exist");
		page.getTempPrefix().setText("prefix");
		wizard.next();
		page.getPrivateKeyFile().setText("private.key");
		page.getPrivateKeyFilePassphrase().setText("secret");
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

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSFTPBinding("sftp-binding");
		assertEquals("sftp-binding", page.getName());
		assertEquals("sftp-directory", page.getDirectory().getText());
		properties.ok();
	}

	@Test
	public void soapBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("SOAP");
		SOAPBindingPage page = new SOAPBindingPage();
		assertEquals("soap1", page.getName());
		page.setName("soap-binding");
		page.getContextPath().setText("soap-context");
		page.setWsdlURI("hello.wsdl");
		page.getWSDLPort().setText("1234");
		page.getServerPort().setText("foo");
		assertFalse(new FinishButton().isEnabled());
		page.getServerPort().setText("4321");
		assertTrue(new FinishButton().isEnabled());
		page.getServerPort().setText(":4321");
		assertTrue(new FinishButton().isEnabled());
		page.getServerPort().setText("host:4321");
		assertTrue(new FinishButton().isEnabled());
		page.getServerPort().setText("${propValue}");
		assertTrue(new FinishButton().isEnabled());
		page.getServerPort().setText(":${propValue}");
		assertTrue(new FinishButton().isEnabled());
		page.getServerPort().setText("host:${propValue}");
		assertTrue(new FinishButton().isEnabled());
		page.getServerPort().setText("${propValue}:4321");
		assertTrue(new FinishButton().isEnabled());
		page.getUnwrappedPayload().toggle(false);
		page.getUnwrappedPayload().toggle(true);
		page.getSOAPHeadersType().setSelection(SOAP_HEADERS_TYPE_DOM);
		page.getConfigFile().setText("soap.conf");
		page.getConfigName().setText("configName");
		page.getEnable().toggle(false);
		page.getEnable().toggle(true);

		page.getTemporarilyDisable().setSelection("false");
		page.getTemporarilyDisable().setSelection("true");
		page.getxopExpand().setSelection("false");
		page.getxopExpand().setSelection("true");
		page.getThreshold().setText("963");
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSOAPBinding("soap-binding");
		assertEquals("soap-binding", page.getName());
		assertEquals("soap-context", page.getContextPath().getText());
		assertEquals("${propValue}:4321", page.getServerPort().getText());
		page.getServerPort().setText("foo");
		// TODO Fix for Oxygen
		// For more info see https://github.com/eclipse/reddeer/issues/1739
		assertFalse(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText("4321");
		assertTrue(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText(":4321");
		assertTrue(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText("host:4321");
		assertTrue(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText("${propValue}");
		assertTrue(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText(":${propValue}");
		assertTrue(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText("host:${propValue}");
		assertTrue(new PushButton("Apply and Close").isEnabled());
		page.getServerPort().setText("${propValue}:4321");
		assertTrue(new PushButton("Apply and Close").isEnabled());

		properties.ok();
	}

	 @Test
	public void soapReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SOAP");
		SOAPBindingPage page = new SOAPBindingPage();
		assertEquals("soap1", page.getName());
		page.setName("soap-binding");
		page.setEndpointAddress("${propValue}:4321");
		page.setWsdlURI("hello.wsdl");
		page.getWSDLPort().setText("1234");
		page.getUnwrappedPayload().toggle(false);
		page.getUnwrappedPayload().toggle(true);
		page.getSOAPHeadersType().setSelection(SOAP_HEADERS_TYPE_DOM);
		page.getEnable().toggle(false);
		page.getEnable().toggle(true);
		page.getTemporarilyDisable().setSelection("false");
		page.getTemporarilyDisable().setSelection("true");
		page.getxopExpand().setSelection("false");
		page.getxopExpand().setSelection("true");
		page.getThreshold().setText("963");
		wizard.next();
		page.getAuthenticationType().setSelection("Basic");
		page.getUser().setText("admin");
		page.getPassword().setText("admin123");
		wizard.next();
		page.getUserName().setText("proxyAdmin");
		page.getPassword().setText("proxyAdmin123");
		page.getHost().setText("proxyhost");
		page.getPort().setText("1234");
		wizard.finish();

		new SwitchYardEditor().saveAndClose();
		editor = new SwitchYardProject(PROJECT).openSwitchYardFile();

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
		assertXPath("admin", bindingPath + "/basic/user");
		assertXPath("admin123", bindingPath + "/basic/password");
		assertXPath("proxyAdmin", bindingPath + "/proxy/user");
		assertXPath("proxyAdmin123", bindingPath + "/proxy/password");
		assertXPath("proxyhost", bindingPath + "/proxy/host");
		assertXPath("1234", bindingPath + "/proxy/port");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSOAPBinding("soap-binding");
		assertEquals("soap-binding", page.getName());
		assertEquals("${propValue}:4321", page.getEndpointAddress());

		page.selectAuthenticationDetails();
		assertEquals("admin", page.getUser().getText());
		assertEquals("admin123", page.getPassword().getText());

		page.selectProxySettings();
		assertEquals("proxyAdmin", page.getUserName().getText());
		assertEquals("proxyAdmin123", page.getPassword().getText());
		assertEquals("proxyhost", page.getHost().getText());
		assertEquals("1234", page.getPort().getText());
		properties.ok();
	}

	@Test
	public void sqlBindingTest() throws Exception {
		WizardDialog wizard = new Service(SERVICE).addBinding("SQL");
		SQLBindingPage page = new SQLBindingPage();
		assertEquals("sql1", page.getName());
		page.setName("sql-binding");
		page.getQuery().setText("sql-query");
		page.getDataSource().setText("data-source");
		page.getPlaceholder().setText("place-holder");
		page.getPeriod().setText("10");
		page.getInitialDelayMS().setText("1234");
		page.setOperationSelector(OPERATION_NAME, METHOD);
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

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSQLBinding("sql-binding");
		assertEquals("sql-binding", page.getName());
		assertEquals("sql-query", page.getQuery().getText());
		assertEquals("data-source", page.getDataSource().getText());
		properties.ok();
	}

	@Test
	public void sqlReferenceBindingTest() throws Exception {
		WizardDialog wizard = new Service(REFERENCE_SERVICE).addBinding("SQL");
		SQLBindingPage page = new SQLBindingPage();
		assertEquals("sql1", page.getName());
		page.setName("sql-binding");
		page.getQuery().setText("sql-query");
		page.getDataSource().setText("data-source");
		page.getPlaceholder().setText("place-holder");
		wizard.finish();

		new SwitchYardEditor().save();

		String bindingPath = "/switchyard/composite/reference/binding.sql";
		assertXPath("sql-binding", bindingPath + "/@name");
		assertXPath("sql-query", bindingPath + "/query");
		assertXPath("data-source", bindingPath + "/dataSourceRef");

		CompositePropertiesDialog properties = new Service(REFERENCE_SERVICE).showProperties();
		page = properties.selectBindings().selectSQLBinding("sql-binding");
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

		WizardDialog wizard = new Service(SERVICE).addBinding("Scheduling");
		SchedulingBindingPage page = new SchedulingBindingPage();
		page.setName("schedule-binding");
		page.getSchedulingType().setSelection(SCHEDULING_TYPE_CRON);
		page.getCron().setText(cron);
		page.getStartTime().setText(startTime);
		page.getEndTime().setText(endTime);
		page.getTimeZone().setSelection("Europe/Prague");
		page.setOperationSelector(OPERATION_NAME, METHOD);
		wizard.finish();

		new SwitchYardEditor().save();
		String bindingPath = "/switchyard/composite/service/binding.quartz";
		assertXPath("sayHello", bindingPath + "/operationSelector/@operationName");
		assertXPath("schedule-binding", bindingPath + "/name");
		assertXPath("0 0 12 * * ?", bindingPath + "/cron");
		assertXPath("2014-01-01T00:00:00", bindingPath + "/trigger.startTime");
		assertXPath("2015-01-01T00:00:00", bindingPath + "/trigger.endTime");
		assertXPath("Europe/Prague", bindingPath + "/trigger.timeZone");

		CompositePropertiesDialog properties = new Service(SERVICE).showProperties();
		page = properties.selectBindings().selectSchedulingBinding("HelloService1");
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
