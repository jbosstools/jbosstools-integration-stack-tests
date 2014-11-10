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
import org.jboss.tools.switchyard.reddeer.binding.RESTBindingPage;
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
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.RESTService;
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
public class UseCaseWSProxyRESTTest {

	public static final String SERVICE = "Hello";
	public static final String SERVICE_REF = "HelloRef";
	public static final String REST_URL = "http://localhost:8123/rest";
	public static final String REST_SERVICE = "HelloRESTService";
	public static final String PROJECT = "proxy_rest";
	public static final String PACKAGE = "com.example.switchyard.proxy_rest";

	private RESTService restService;
	
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
	public void startRestService() {
		restService = new RESTService(8123);
		restService.start();
	}

	@After
	public void stopRestService() {
		restService.stop();
	}

	@Test
	public void wsProxyRestTest() {
		/* Create SwicthYard Project */
		switchyardRequirement.project(PROJECT).impl("Camel Route").binding("SOAP", "REST").create();
		new CamelJavaWizard().open().setName("Proxy").createJavaInterface("Hello").finish();

		/* Import Resources */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/test/resources").select();
		new ImportFileWizard().importFile("resources/messages/WSProxyREST", "soap-request.xml");
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/test/resources").select();
		new ImportFileWizard().importFile("resources/messages/WSProxyREST", "soap-response.xml");

		/* Edit Hello interface */
		new Service("Hello").doubleClick();

		new TextEditor("Hello.java").typeBefore("Hello", "import javax.ws.rs.Produces;").newLine()
				.type("import javax.ws.rs.GET;").newLine().type("import javax.ws.rs.Path;").newLine()
				.type("import javax.ws.rs.PathParam;").newLine().typeAfter("Hello", "@GET()").newLine()
				.type("@Path(\"/{name}\")").newLine().type("@Produces(\"text/plain\")").newLine()
				.type("String sayHello(@PathParam(\"name\") String name);").saveAndClose();
		new SwitchYardEditor().save();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().createWSDLInterface("Hello.wsdl");
		wizard.activate().next();
		wizard.setTransformerType("Java Transformer").next();
		wizard.setName("HelloTransformer").finish();
		new SwitchYardEditor().save();

		/* Edit HelloTransformer */
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/java", PACKAGE, "HelloTransformer.java")
				.open();
		new TextEditor("HelloTransformer.java")
				.deleteLineWith("ToSayHello")
				.type("public static String transformStringToSayHelloResponse(String from) {")
				.deleteLineWith("return null")
				.type("return \"<sayHelloResponse xmlns=\\\"urn:com.example.switchyard:" + PROJECT + ":1.0\\\">"
						+ "<string>\"+ from + \"</string></sayHelloResponse>\";").deleteLineWith("return null")
				.type("return from.getTextContent().trim();").saveAndClose();
		new SwitchYardEditor().save();

		/* Expose Proxy Service Through SOAP */
		new Service("HelloPortType").addBinding("SOAP");
		SOAPBindingPage soapWizard = new SOAPBindingPage();
		soapWizard.setName("soap-binding");
		soapWizard.getContextPath().setText(PROJECT);
		soapWizard.getServerPort().setText(":18080");
		soapWizard.finish();

		new SwitchYardEditor().save();

		/* Reference to RESTful Service */
		new SwitchYardComponent("Proxy").getContextButton("Reference").click();
		new ReferenceWizard().selectJavaInterface("Hello").setServiceName(SERVICE_REF).finish();
		new Reference(SERVICE_REF).promoteReference().setServiceName(REST_SERVICE).finish();
		new Service(REST_SERVICE).addBinding("REST");
		RESTBindingPage restWizard = new RESTBindingPage();
		restWizard.setAddress(REST_URL);
		restWizard.addInterface("Hello");
		wizard.finish();

		/* Edit Camel Route */
		new SwitchYardComponent("Proxy").doubleClick();
		new TextEditor("Proxy.java").typeAfter("from(", ".to(\"switchyard://" + SERVICE_REF + "\")").saveAndClose();
		new SwitchYardEditor().save();

		/* Test Web Service Proxy */
		new Service("Hello").newServiceTestClass().setPackage(PACKAGE).selectMixin("HTTP Mix-in").finish();
		new TextEditor("HelloTest.java")
				.deleteLineWith("String message")
				.deleteLineWith("String result")
				.deleteLineWith("getContent")
				.deleteLineWith("assertTrue")
				.type("httpMixIn.postResourceAndTestXML(\"http://localhost:18080/proxy_rest/Hello\", \"soap-request.xml\", \"soap-response.xml\");")
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
