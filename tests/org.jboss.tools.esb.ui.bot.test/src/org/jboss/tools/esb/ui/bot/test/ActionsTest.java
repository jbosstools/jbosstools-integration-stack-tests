package org.jboss.tools.esb.ui.bot.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.esb.reddeer.editor.ESBEditor;
import org.jboss.tools.esb.reddeer.wizard.ESBActionWizard;
import org.jboss.tools.esb.reddeer.wizard.ESBProjectWizard;
import org.jboss.tools.esb.ui.bot.test.util.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author apodhrad
 *
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class ActionsTest {

	public static final String PROJECT_NAME = "esb-actions";
	public static final String ESB_FILE = "jboss-esb.xml";
	public static final String NAME_SUFFIX = "-action";
	public static final String SERVICE = "aaa";
	public static final String CATEGORY = "bbb";
	public static final String DESCRIPTION = "ccc";

	public Logger log = Logger.getLogger(ActionsTest.class);

	@BeforeClass
	public static void createProject() {
		new WorkbenchShell().maximize();
		new ESBProjectWizard().openWizard().setName(PROJECT_NAME).finish();
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("esbcontent", "META-INF", ESB_FILE).open();
		new ESBEditor().addService(SERVICE, "bbb", "ccc");
		new ESBEditor().save();
	}

	@AfterClass
	public static void deleteProject() {
		new ProjectExplorer().getProject(PROJECT_NAME).delete(true);
	}

	@Before
	public void openFile() {
		new ProjectExplorer().getProject(PROJECT_NAME).getProjectItem("esbcontent", "META-INF", ESB_FILE).open();
	}

	@After
	public void saveAndCloseEditor() {
		try {
			new ESBEditor().save();
			new ESBEditor().close();
		} catch (Exception e) {

		}
	}

	// TODO: add editing test for each action
	// TODO: add test for removing actions

	@Test
	public void customActionTest() {
		String name = "custom";
		String clazz = "java.lang.Object";

		ESBActionWizard wizard = new ESBEditor().addCustomActionToService(SERVICE);
		assertFalse(new PushButton("OK").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Class:*", "java.lang.Object");
		wizard.ok();

		check(name, clazz);
	}

	@Test
	public void bpmProcessorTest() {
		String action = "BPM Processor";
		String category = "BPM";
		String name = "bpm-processor";
		String clazz = "org.jboss.soa.esb.services.jbpm.actions.BpmProcessor";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setCombo("Command:*", "NewProcessInstanceCommand");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void bpm5ProcessorTest() {
		String action = "BPM 5 Processor";
		String category = "BPM";
		String name = "bpm5-processor";
		String clazz = "org.jboss.soa.esb.services.jbpm5.actions.Bpm5Processor";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Process Definition Name:*", name + "processDefName");
		wizard.setText("Process ID:*", name + "processID");
		wizard.setCombo("Process Action:*", "startProcess");
		wizard.setCombo("Process Action:*", "signalEvent");
		wizard.setCombo("Process Action:*", "abortProcessInstance");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void bpmRulesProcessorTest() {
		String action = "Business Rules Processor";
		String category = "BPM";
		String name = "bpm-rules-processor";
		String clazz = "org.jboss.soa.esb.actions.BusinessRulesProcessor";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, action + "...");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void byteArrayTest() {
		String action = "Byte Array To String";
		String category = "Converters/Transformers";
		String name = "byte-array-to-string";
		String clazz = "org.jboss.soa.esb.actions.converters.ByteArrayToString";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void commandInterpreterTest() {
		String action = "Command Interpreter";
		String category = "Converters/Transformers";
		String name = "command-interpreter";
		String clazz = "org.jboss.soa.esb.actions.jbpm.CommandInterpreter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void longToDateTest() {
		String action = "Long To Date";
		String category = "Converters/Transformers";
		String name = "long-to-date";
		String clazz = "org.jboss.soa.esb.actions.converters.LongToDateConverter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void messagePersisterTest() {
		String action = "Message Persister";
		String category = "Converters/Transformers";
		String name = "message-persister";
		String clazz = "org.jboss.soa.esb.actions.MessagePersister";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Message Store Class:*", "java.lang.Object");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void objectInvokeTest() {
		String action = "Object Invoke";
		String category = "Converters/Transformers";
		String name = "object-invoke";
		String clazz = "org.jboss.soa.esb.actions.converters.ObjectInvoke";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Class Processor:*", "java.lang.Object");
		wizard.setText("Class Method:*", "toString");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void objectToCsvStringTest() {
		String action = "Object To CSV String";
		String category = "Converters/Transformers";
		String name = "object-to-csv-string";
		String clazz = "org.jboss.soa.esb.actions.converters.ObjectToCSVString";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Bean Properties:*", "a=a");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void objectToXStreamTest() {
		String action = "Object To XStream";
		String category = "Converters/Transformers";
		String name = "object-to-xstream";
		String clazz = "org.jboss.soa.esb.actions.converters.ObjectToXStream";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void smooksActionTest() {
		String action = "Smooks Action";
		String category = "Converters/Transformers";
		String name = "smooks-action";
		String clazz = "org.jboss.soa.esb.smooks.SmooksAction";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Smooks Config:*", "soomks-config.xml");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void xStreamToObjectTest() {
		String action = "XStream To Object";
		String category = "Converters/Transformers";
		String name = "xstream-to-object";
		String clazz = "org.jboss.soa.esb.actions.converters.XStreamToObject";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Incoming Type:*", "java.lang.Object");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void xsltActionTest() {
		String action = "XSLT Action";
		String category = "Converters/Transformers";
		String name = "xslt-action";
		String clazz = "org.jboss.soa.esb.actions.transformation.xslt.XsltAction";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Template File:*", "template.xsl");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void systemPrintlnTest() {
		String action = "System Println";
		String category = "Miscellaneous";
		String name = "system-println";
		String clazz = "org.jboss.soa.esb.actions.SystemPrintln";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Message:*", "Hello!");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void schemaValidationTest() {
		String action = "Schema Validation";
		String category = "Miscellaneous";
		String name = "schema-validation";
		String clazz = "org.jboss.soa.esb.actions.validation.SchemaValidationAction";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, action + "...");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Schema:*", "schema.xsd");
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void aggregatorTest() {
		String action = "Aggregator";
		String category = "Routers";
		String name = "aggregator";
		String clazz = "org.jboss.soa.esb.actions.Aggregator";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void cbrDroolsTest() {
		String action = "Content Based Router (Drools)";
		String category = "Routers";
		String name = "cbr-drools";
		String clazz = "org.jboss.soa.esb.actions.ContentBasedRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, "Add Drools Router");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Rule Set:*", "rules");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void cbrGenericTest() {
		String action = "Content Based Router (Generic)";
		String category = "Routers";
		String name = "cbr-generic";
		String clazz = "org.jboss.soa.esb.actions.ContentBasedRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action,
				"Add Content Based Router");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setCombo("CBR Alias:*", "Xpath");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.setCombo("CBR Alias:*", "Drools");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setCombo("CBR Alias:*", "Regex");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void cbrRegexTest() {
		String action = "Content Based Router (Regex)";
		String category = "Routers";
		String name = "cbr-regex";
		String clazz = "org.jboss.soa.esb.actions.ContentBasedRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, "Add Regex Router");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void cbrXPathTest() {
		String action = "Content Based Router (XPath)";
		String category = "Routers";
		String name = "cbr-xpath";
		String clazz = "org.jboss.soa.esb.actions.ContentBasedRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, "Add XPath Router");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void echoRouterTest() {
		String action = "Echo Router";
		String category = "Routers";
		String name = "echo-router";
		String clazz = "org.jboss.soa.esb.actions.routing.EchoRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void emailRouterTest() {
		String action = "EMail Router";
		String category = "Routers";
		String name = "email-router";
		String clazz = "org.jboss.soa.esb.actions.routing.email.EmailRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void emailWiretapTest() {
		String action = "EMail Wiretap";
		String category = "Routers";
		String name = "email-wiretap";
		String clazz = "org.jboss.soa.esb.actions.routing.email.EmailWiretap";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void httpRouterTest() {
		String action = "HTTP Router";
		String category = "Routers";
		String name = "http-router";
		String clazz = "org.jboss.soa.esb.actions.routing.http.HttpRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, "Add HTTP Wiretap");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void jmsRouterTest() {
		String action = "JMS Router";
		String category = "Routers";
		String name = "jms-router";
		String clazz = "org.jboss.soa.esb.actions.routing.JMSRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void notifierTest() {
		String action = "Notifier";
		String category = "Routers";
		String name = "notifier";
		String clazz = "org.jboss.soa.esb.actions.Notifier";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void staticRouterTest() {
		String action = "Static Router";
		String category = "Routers";
		String name = "static-router";
		String clazz = "org.jboss.soa.esb.actions.StaticRouter";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void staticWiretapTest() {
		String action = "Static Wiretap";
		String category = "Routers";
		String name = "static-wiretap";
		String clazz = "org.jboss.soa.esb.actions.StaticWiretap";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void syncServiceInvokerTest() {
		String action = "Sync Service Invoker";
		String category = "Routers";
		String name = "sync-service-invoker";
		String clazz = "org.jboss.soa.esb.actions.SyncServiceInvoker";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setCombo("Service Category:*", CATEGORY);
		wizard.setCombo("Service Name:*", SERVICE);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void scriptingTest() {
		String action = "Scripting";
		String category = "Scripting";
		String name = "scripting";
		String clazz = "org.jboss.soa.esb.actions.scripting.ScriptingAction";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action, "Add Scripting Action");
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Script:*", "script.sh");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void groovyActionProcessorTest() {
		String action = "Groovy Action Processor";
		String category = "Scripting";
		String name = "groovy-scripting";
		String clazz = "org.jboss.soa.esb.actions.scripting.GroovyActionProcessor";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Script:*", "script.groovy");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void ejbProcessorTest() {
		String action = "EJB Processor";
		String category = "Services";
		String name = "ejb-processor";
		String clazz = "org.jboss.soa.esb.actions.EJBProcessor";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void soapProcessorTest() {
		String action = "SOAP Processor";
		String category = "Webservices";
		String name = "soap-processor";
		String clazz = "org.jboss.soa.esb.actions.soap.SOAPProcessor";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Jbossws Endpoint:*", "endpoint");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void soapClientTest() {
		String action = "SOAP Client";
		String category = "Webservices";
		String name = "soap-client";
		String clazz = "org.jboss.soa.esb.actions.soap.SOAPClient";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Wsdl:*", "wsdl");
		wizard.setText("Soap Action:*", "action");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void soapProxyTest() {
		String action = "SOAP Proxy";
		String category = "Webservices";
		String name = "soap-proxy";
		String clazz = "org.jboss.soa.esb.actions.soap.proxy.SOAPProxy";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Wsdl:*", "wsdl");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	@Test
	public void soapWiseClientTest() {
		String action = "SOAP Wise Client";
		String category = "Webservices";
		String name = "soap-wise-client";
		String clazz = "org.jboss.soa.esb.actions.soap.wise.SOAPClient";

		ESBActionWizard wizard = new ESBEditor().addActionToService(SERVICE, category, action);
		assertFalse(new PushButton("Finish").isEnabled());
		wizard.setName(name + NAME_SUFFIX);
		wizard.setText("Wsdl:*", "wsdl");
		wizard.setText("Operation:*", "operation");
		assertTrue(new PushButton("Finish").isEnabled());
		wizard.finish();

		check(name, clazz);
	}

	private void check(String name, String clazz) {
		String source = new ESBEditor().getSource();
		String xpath = "count(//jbossesb/services/service[@name='" + SERVICE + "']/actions/action[@name='" + name
				+ NAME_SUFFIX + "' and @class='" + clazz + "'])=1";
		Assertions.assertXmlContentBool(source, xpath);
		Assertions.assertEmptyProblemsView("after " + name + " was added");
	}

}
