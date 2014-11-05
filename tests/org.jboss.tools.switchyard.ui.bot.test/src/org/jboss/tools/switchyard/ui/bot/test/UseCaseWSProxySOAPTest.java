package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Reference;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.SOAPService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Web Service Proxy Test
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class UseCaseWSProxySOAPTest {

	public static final String PROJECT = "proxy_soap";
	public static final String PACKAGE = "com.example.switchyard.proxy_soap";
	private static final String WSDL = "Hello.wsdl";

	private SOAPService webService;
	
	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@Before
	@After
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Before
	public void startWebService() {
		webService = new SOAPService(8123);
		webService.start();
	}

	@After
	public void stopWebService() {
		webService.stop();
	}

	@Test
	public void wsProxySoapTest() throws Exception {
		/* Create SwicthYard Project */
		switchyardRequirement.project(PROJECT).impl("Camel Route").binding("SOAP").create();

		/* Import Resources */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/wsdl", WSDL);
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/test/resources").select();
		new ImportFileWizard().importFile("resources/messages/WSProxy", "soap-request.xml");
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/test/resources").select();
		new ImportFileWizard().importFile("resources/messages/WSProxy", "soap-response.xml");

		/* Create Camel Route */
		new CamelJavaWizard().open().setName("Proxy").selectWSDLInterface(WSDL).setServiceName("Hello").finish();

		new Service("Hello").promoteService().setServiceName("ProxyService").finish();
		new Service("ProxyService").addBinding("SOAP");
		SOAPBindingPage soapWizard = new SOAPBindingPage();
		soapWizard.setName("soap-binding");
		soapWizard.setContextPath(PROJECT);
		soapWizard.setServerPort(":18080");
		soapWizard.finish();
		new SwitchYardComponent("Proxy").getContextButton("Reference").click();
		new ReferenceWizard().activate().selectWSDLInterface(WSDL).setServiceName("HelloRef").finish();
		new Reference("HelloRef").promoteReference().setServiceName("HelloService").finish();
		new Service("HelloService").getContextButton("Binding", "SOAP").click();
		SOAPBindingPage wizard = new SOAPBindingPage();
		wizard.setName("soap-binding");
		wizard.setEndpointAddress("http://localhost:8123/soap");
		wizard.finish();

		new SwitchYardEditor().save();

		/* Edit Camel Route */
		new SwitchYardComponent("Proxy").doubleClick();
		new TextEditor("Proxy.java").typeAfter("from(", ".to(\"switchyard://HelloRef\")").saveAndClose();
		new SwitchYardEditor().save();

		/* Test Web Service Proxy */
		new Service("Hello").newServiceTestClass().setPackage(PACKAGE).selectMixin("HTTP Mix-in").finish();
		
		new TextEditor("HelloTest.java")
				.deleteLineWith("Object message")
				.deleteLineWith("Object result")
				.deleteLineWith("getContent")
				.deleteLineWith("assertTrue")
				.type("httpMixIn.postResourceAndTestXML(\"http://localhost:18080/proxy_soap/HelloServiceService\", \"soap-request.xml\", \"soap-response.xml\");")
				.saveAndClose();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new SwitchYardEditor().save();
		ProjectItem item = new ProjectExplorer().getProject(PROJECT).getProjectItem("src/test/java", PACKAGE,
				"HelloTest.java");
		new ProjectItemExt(item).runAsJUnitTest();
		AbstractWait.sleep(TimePeriod.NORMAL);
		new WaitUntil(new JUnitHasFinished(), TimePeriod.LONG);

		JUnitView jUnitView = new JUnitView();
		jUnitView.open();
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());
	}
}
