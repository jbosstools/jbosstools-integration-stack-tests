package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.ui.bot.test.util.TemplateHandler.javaSource;
import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
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
public class UseCaseSimpleTest {

	private static final String PROJECT = "simple";
	private static final String PACKAGE = "com.example.switchyard.simple";

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

	@Test
	public void simpleTest() throws Exception {
		switchyardRequirement.project(PROJECT).impl("Bean").binding("SOAP").create();

		new SwitchYardEditor().addBeanImplementation().createJavaInterface("ExampleService").finish();

		new SwitchYardComponent("ExampleService").doubleClick();
		TextEditor textEditor = new TextEditor("ExampleService.java");
		textEditor.setText(javaSource("ExampleService.java", PACKAGE));
		textEditor.close(true);

		new SwitchYardComponent("ExampleServiceBean").doubleClick();
		textEditor = new TextEditor("ExampleServiceBean.java");
		textEditor.setText(javaSource("ExampleServiceBean.java", PACKAGE));
		textEditor.close(true);

		new SwitchYardEditor().save();

		new Service("ExampleService").createNewServiceTestClass();
		textEditor = new TextEditor("ExampleServiceTest.java");
		textEditor.setText(javaSource("ExampleServiceTest.java", PACKAGE));
		textEditor.close(true);

		new SwitchYardEditor().save();

		ProjectItem item = new SwitchYardProject(PROJECT).getProjectItem("src/test/java", PACKAGE,
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

		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE, "ExampleServiceTransformers.java")
				.open();
		textEditor = new TextEditor("ExampleServiceTransformers.java");
		textEditor.setText(javaSource("ExampleServiceTransformers.java", PACKAGE));
		textEditor.close(true);

		new Service("ExampleServicePortType").addBinding("SOAP");
		SOAPBindingPage soapWizard = new SOAPBindingPage();
		soapWizard.getContextPath().setText(PROJECT);
		soapWizard.finish();

		new SwitchYardEditor().save();

		/* Test SOAP Response */
		final ServerBase server = switchyardRequirement.getConfig().getServerBase();
		server.deployProject(PROJECT);
		new WaitUntil(new WaitCondition() {
			
			@Override
			public boolean test() {
				try {
					SoapClient.testResponses(server.getUrl(PROJECT + "/ExampleService?wsdl"), "Hello");
				} catch (Throwable t) {
					return false;
				}
				return true;
			}
			
			@Override
			public String description() {
				return "Checking the deployed project";
			}
		}, TimePeriod.LONG, false);
		SoapClient.testResponses(server.getUrl(PROJECT + "/ExampleService?wsdl"), "Hello");

		new WaitWhile(new ConsoleHasChanged());
	}
}
