package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Creation test from existing Camel route
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
public class BottomUpCamelTest extends RedDeerTest {

	public static final String PROJECT = "camel_project";
	public static final String PACKAGE = "com.example.switchyard.camel_project";
	public static final String JAVA_FILE = "MyRouteBuilder";
	
	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

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
		String version = SwitchyardSuite.getLibraryVersion();
		new SwitchYardProjectWizard(PROJECT, version).impl("Camel Route").binding("HTTP").create();
		Project project = new ProjectExplorer().getProject(PROJECT);

		// Import java file
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit java file
		project.getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("package")
				.type("package " + PACKAGE + ";").saveAndClose();

		// Add component
		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Camel (Java)");
		new Component("Component").click();

		// Select existing implementation
		new PushButton("Browse...").click();
		bot.shell("Select entries").activate();
		new DefaultText(0).setText(JAVA_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		bot.shell("Camel Implementation").activate();
		new PushButton("Finish").click();

		// Create new service and interface
		new Component("Component").contextButton("Service").click();
		new NewServiceWizard().activate().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new TextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);")
				.saveAndClose();

		// Edit the camel route
		new Component("Component").doubleClick();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("file:in")
				.type("from(\"switchyard://Hello\")").deleteLineWith("file:out").type(";")
				.saveAndClose();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().setServiceName("HelloService").finish();

		// Add HTTP binding
		new Service("HelloService").addBinding("HTTP");
		BindingWizard<HTTPBindingPage> httpWizard = BindingWizard.createHTTPBindingWizard();
		httpWizard.getBindingPage().setContextPath(PROJECT);
		httpWizard.getBindingPage().setOperation("sayHello");
		httpWizard.finish();
		
		new SwitchYardEditor().save();
		
		// Create HelloTest
		new Service("Hello").createNewServiceTestClass();
		new TextEditor("HelloTest.java").deleteLineWith("String message").type("String message=\"Camel\";")
				.deleteLineWith("assertTrue").type("Assert.assertEquals(\"Hello Camel\", result);").saveAndClose();
		new SwitchYardEditor().save();

		// Tun the test
		ProjectItem item = project.getProjectItem("src/test/java", PACKAGE, "HelloTest.java");
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
