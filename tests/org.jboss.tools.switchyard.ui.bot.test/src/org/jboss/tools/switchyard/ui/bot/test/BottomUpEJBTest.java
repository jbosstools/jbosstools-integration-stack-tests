package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.OPERATION_NAME;
import static org.junit.Assert.assertEquals;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.server.ServerDeployment;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.HttpClient;
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
@SwitchYard(server = @Server(type = ServerReqType.ANY, state = ServerReqState.RUNNING))
@RunWith(RedDeerSuite.class)
public class BottomUpEJBTest {

	public static final String PROJECT = "ejb_project";
	public static final String PACKAGE = "com.example.switchyard.ejb_project";
	public static final String JAVA_FILE = "HelloBean";

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

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
		switchyardRequirement.project(PROJECT).impl("Bean").binding("HTTP").create();
		Project project = new ProjectExplorer().getProject(PROJECT);

		// Add EJB dependency
		project.getProjectItem("pom.xml").open();
		new DefaultCTabItem("Dependencies").activate();
		new PushButton("Add...").click();
		bot.shell("Select Dependency").activate();
		new LabeledText("Group Id:").setText("javax");
		new LabeledText("Artifact Id:").setText("javaee-api");
		new LabeledText("Version: ").setText("6.0");
		new PushButton("OK").click();
		bot.activeEditor().saveAndClose();

		// Import java file
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit java file
		project.getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("package")
				.type("package " + PACKAGE + ";").saveAndClose();

		new SwitchYardEditor().addComponent();
		new SwitchYardEditor().addBeanImplementation(new SwitchYardComponent("Component"));

		bot.shell("Bean Implementation").activate();
		new PushButton("Browse...").click();
		new DefaultText(0).setText(JAVA_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		bot.shell("Bean Implementation").activate();
		new PushButton("Finish").click();

		new SwitchYardComponent("Component").getContextButton("Service").click();
		new NewServiceWizard().activate().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new TextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);")
				.saveAndClose();

		// Edit the camel route
		new SwitchYardComponent("Component").doubleClick();
		new TextEditor(JAVA_FILE + ".java")
				.typeAfter("package", "import org.switchyard.component.bean.Service;")
				.typeBefore("@Stateless", "@Service(Hello.class)").deleteLineWith("HelloBean")
				.type("public class HelloBean implements Hello {").saveAndClose();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().setServiceName("HelloService").finish();

		// Add HTTP binding
		new Service("HelloService").addBinding("HTTP");
		HTTPBindingPage httpWizard = new HTTPBindingPage();
		httpWizard.setContextPath(PROJECT);
		httpWizard.setOperationSelector(OPERATION_NAME, "sayHello");
		httpWizard.finish();
		
		new SwitchYardEditor().save();

		// Deploy and test the project
		new ServerDeployment(switchyardRequirement.getConfig().getName()).deployProject(PROJECT);
		String url = "http://localhost:8080/" + PROJECT;
		HttpClient httpClient = new HttpClient(url);
		assertEquals("EJB: Hello apodhrad", httpClient.post("apodhrad"));
		assertEquals("EJB: Hello JBoss", httpClient.post("JBoss"));
		new WaitWhile(new ConsoleHasChanged());
	}

}
