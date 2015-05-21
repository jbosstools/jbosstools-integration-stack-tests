package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.OPERATION_NAME;
import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SimpleTextEditor;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Creation test from existing Camel route
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class BottomUpCamelTest {

	public static final String PROJECT = "camel_project";
	public static final String PACKAGE = "com.example.switchyard.camel_project";
	public static final String JAVA_FILE = "MyRouteBuilder";
	
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
	public void bottomUpCamelTest() throws Exception {
		switchyardRequirement.project(PROJECT).impl("Camel Route").binding("HTTP").create();

		// Import java file
		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit java file
		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		new SimpleTextEditor(JAVA_FILE + ".java").deleteLineWith("package")
				.type("package " + PACKAGE + ";").saveAndClose();

		// Add component
		new SwitchYardEditor().addComponent();
		new SwitchYardEditor().addCamelJavaImplementation(new SwitchYardComponent("Component"));

		// Select existing implementation
		new PushButton("Browse...").click();
		new DefaultShell("Select entries");
		new DefaultText(0).setText(JAVA_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		new DefaultShell("Camel Implementation");
		new PushButton("Finish").click();

		// Create new service and interface
		new SwitchYardComponent("Component").getContextButton("Service").click();
		new NewServiceWizard().activate().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new SimpleTextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);")
				.saveAndClose();

		// Edit the camel route
		new SwitchYardComponent("Component").doubleClick();
		new SimpleTextEditor(JAVA_FILE + ".java").deleteLineWith("file:in")
				.type("from(\"switchyard://Hello\")").deleteLineWith("file:out").type(";")
				.saveAndClose();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().setServiceName("HelloService").finish();

		// Add HTTP binding
		new Service("HelloService").addBinding("HTTP");
		HTTPBindingPage httpWizard = new HTTPBindingPage();
		httpWizard.setName("http-binding");
		httpWizard.getContextPath().setText(PROJECT);
		httpWizard.setOperationSelector(OPERATION_NAME, "sayHello");
		httpWizard.finish();
		
		new SwitchYardEditor().save();
		
		// Create HelloTest
		new Service("Hello").createNewServiceTestClass();
		new SimpleTextEditor("HelloTest.java").deleteLineWith("String message").type("String message=\"Camel\";")
				.deleteLineWith("assertTrue").type("Assert.assertEquals(\"Hello Camel\", result);").saveAndClose();
		new SwitchYardEditor().save();

		// Tun the test
		ProjectItem item = new SwitchYardProject(PROJECT).getProjectItem("src/test/java", PACKAGE, "HelloTest.java");
		new ProjectItemExt(item).runAsJUnitTest();
		new WaitUntil(new JUnitHasFinished(), TimePeriod.LONG);

		// Check the test
		JUnitView jUnitView = new JUnitView();
		jUnitView.open();
		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());
	}
}
