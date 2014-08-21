package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Bean;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.server.ServerDeployment;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.SoapClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This simple switchyard test performs the following tasks:
 * 
 * <ol>
 * <li>Create a SwitchYard project
 * <li>Create a Java service interface
 * <li>Create a bean component implementation
 * <li>Create and execute a unit test for the service
 * <li>Create a WSDL service interface
 * <li>Add a SOAP gateway for accessing the service
 * <li>Create a transformer
 * <li>Create and execute a unit test for the transformer
 * <li>Create and execute a unit test for the SOAP gateway
 * <li>Deploy the application to a server
 * <li>Test the deployed application
 * </ol>
 * 
 * @author apodhrad
 * 
 */
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING))
@OpenPerspective(JavaEEPerspective.class)
@RunWith(RedDeerSuite.class)
public class SimpleTest {

	private static final String PROJECT = "simple";
	private static final String PACKAGE = "com.example.switchyard.simple";

	@InjectRequirement
	private SwitchYardRequirement switchyardRequirement;

	@Before @After
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}
	
	@Test
	public void simpleTest() throws Exception {
		switchyardRequirement.project(PROJECT).impl("Bean").binding("SOAP").create();

		new Bean().setService("ExampleService").create();

		new Component("ExampleService").doubleClick();
		new TextEditor("ExampleService.java").typeAfter("interface",
				"String sayHello(String name);").saveAndClose();

		new Component("ExampleServiceBean").doubleClick();
		new TextEditor("ExampleServiceBean.java").typeAfter("public class", "@Override").newLine()
				.type("public String sayHello(String name) {").newLine()
				.type("return \"Hello \" + name;}").saveAndClose();
		new SwitchYardEditor().save();

		new Service("ExampleService").createNewServiceTestClass();
		new SwitchYardEditor().save();

		new TextEditor("ExampleServiceTest.java").deleteLineWith("String message")
				.type("String message=\"Andrej\";").deleteLineWith("assertTrue")
				.type("Assert.assertEquals(\"Hello Andrej\", result);").saveAndClose();
		new SwitchYardEditor().save();

		ProjectItem item = getProject().getProjectItem("src/test/java", PACKAGE,
				"ExampleServiceTest.java");
		new ProjectItemExt(item).runAsJUnitTest();
		new WaitUntil(new JUnitHasFinished(), TimePeriod.LONG);

		JUnitView jUnitView = new JUnitView();
		jUnitView.open();
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());

		PromoteServiceWizard wizard = new Service("ExampleService").promoteService();
		wizard.activate().createWSDLInterface("ExampleService.wsdl");
		wizard.activate().next();
		wizard.setTransformerType("Java Transformer").next();
		wizard.setName("ExampleServiceTransformers").finish();

		new ProjectExplorer().getProject(PROJECT)
				.getProjectItem("src/main/java", PACKAGE, "ExampleServiceTransformers.java").open();
		new TextEditor("ExampleServiceTransformers.java")
				.deleteLineWith("ToSayHello")
				.type("public static String transformStringToSayHelloResponse(String from) {")
				.deleteLineWith("return null")
				.type("return \"<sayHelloResponse xmlns=\\\"urn:com.example.switchyard:" + PROJECT
						+ ":1.0\\\">" + "<string>\"+ from + \"</string></sayHelloResponse>\";")
				.deleteLineWith("return null").type("return from.getTextContent().trim();")
				.saveAndClose();

		new Service("ExampleServicePortType").addBinding("SOAP");
		BindingWizard<SOAPBindingPage> soapWizard = BindingWizard.createSOAPBindingWizard();
		soapWizard.getBindingPage().setContextPath(PROJECT);
		soapWizard.finish();
		
		new SwitchYardEditor().save();

		/* Test SOAP Response */
		new ServerDeployment(switchyardRequirement.getConfig().getName()).deployProject(PROJECT);
		try {
			SoapClient.testResponses("http://localhost:8080/" + PROJECT + "/ExampleService?wsdl", "Hello");
		} catch (Exception ex) {
			throw ex;
		}

		new WaitWhile(new ConsoleHasChanged());
	}

	private static Project getProject() {
		return new ProjectExplorer().getProject(PROJECT);
	}

}
