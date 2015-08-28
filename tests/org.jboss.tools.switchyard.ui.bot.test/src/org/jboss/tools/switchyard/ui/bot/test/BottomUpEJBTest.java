package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.reddeer.binding.OperationOptionsPage.OPERATION_NAME;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.runtime.reddeer.ServerBase;
import org.jboss.tools.runtime.reddeer.requirement.ServerReqType;
import org.jboss.tools.runtime.reddeer.requirement.ServerRequirement.Server;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.HttpClient;
import org.jboss.tools.switchyard.ui.bot.test.util.TemplateHandler;
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
	public void bottomUpEJBTest() throws Exception {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("package", PACKAGE);
		dataModel.put("project", PROJECT);
		dataModel.put("body", "${body}");
		dataModel.put("prefix", "EJB: ");

		switchyardRequirement.project(PROJECT).impl("Bean").binding("HTTP").create();

		// Add EJB dependency
		new SwitchYardProject(PROJECT).getProjectItem("pom.xml").open();
		new DefaultCTabItem("Dependencies").activate();
		new PushButton("Add...").click();
		new DefaultShell("Select Dependency");
		new LabeledText("Group Id:").setText("javax");
		new LabeledText("Artifact Id:").setText("javaee-api");
		new LabeledText("Version: ").setText("6.0");
		new PushButton("OK").click();
		new DefaultEditor().close(true);

		// Import java file
		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit java file
		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		TextEditor textEditor = new TextEditor("HelloBean.java");
		textEditor.setText(TemplateHandler.javaSource("HelloEJBBean.java", dataModel));
		textEditor.save();
		textEditor.close(true);

		SwitchYardComponent component = new SwitchYardEditor().addComponent();
		new SwitchYardEditor().addBeanImplementation(component);

		new DefaultShell("Bean Implementation");
		new PushButton("Browse...").click();
		new DefaultShell("");
		new DefaultText(0).setText(JAVA_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		new DefaultShell("Bean Implementation");
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Bean Implementation"));
		
		new Service("Hello").delete();
		new SwitchYardComponent("Component").getContextButton("Service").click();
		new NewServiceWizard().activate().createJavaInterface("Hello").activate().finish();
		
		// Edit the interface
		new Service("Hello").doubleClick();
		textEditor = new TextEditor("Hello.java");
		textEditor.setText(TemplateHandler.javaSource("Hello.java", dataModel));
		textEditor.save();
		textEditor.close(true);

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

		// Deploy and test the project
		ServerBase server = switchyardRequirement.getConfig().getServerBase();
		server.deployProject(PROJECT);
		HttpClient httpClient = new HttpClient(server.getUrl(PROJECT));
		assertEquals("EJB: Hello apodhrad", httpClient.post("apodhrad"));
		assertEquals("EJB: Hello JBoss", httpClient.post("JBoss"));
		new WaitWhile(new ConsoleHasChanged());
	}

}
