package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.widget.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Creation test from existing BPM process
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@RunWith(SwitchyardSuite.class)
public class BottomUpBPMN2Test extends RedDeerTest {

	public static final String PROJECT = "bpmn2_project";
	public static final String PACKAGE = "com.example.switchyard.bpmn2_project";
	public static final String BPMN2_FILE = "sample.bpmn";

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
	public void bottomUpBPMN2Test() throws Exception {
		new WorkbenchShell().maximize();
		
		String version = SwitchyardSuite.getLibraryVersion();
		new SwitchYardProjectWizard(PROJECT, version).impl("BPM (jBPM)").create();
		Project project = new ProjectExplorer().getProject(PROJECT);

		// Import BPMN process
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/bpmn", BPMN2_FILE);

		// Add component
		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Process (BPMN)");
		new Component("Component").click();

		// Select existing implementation
		new PushButton("Browse...").click();
		new DefaultShell("Select Resource");
		new DefaultText(0).setText(BPMN2_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		new PushButton("Finish").click();

		// Create new service and interface
		new Component("Component").contextButton("Service").click();
		new NewServiceWizard().activate().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new TextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);").saveAndClose();

		// Edit the BPMN process
		new Component("Component").showProperties();
		new DefaultTreeItem("Implementation").select();
		new LabeledText("Process ID:").setText("com.sample.bpmn.hello");

		new DefaultTabItem("Operations").activate();
		new PushButton("Add").click();

		new DefaultTable(0).getItem(0).doubleClick(0);
		new DefaultText(1).setText("sayHello");
		new DefaultTable(0).select(0);

		new PushButton(2, "Add").click();
		new DefaultTable(2).getItem(0).doubleClick(1);
		new DefaultText(1).setText("name");
		new DefaultTable(0).select(0);

		new PushButton(3, "Add").click();
		new DefaultTable(3).getItem(0).doubleClick(0);
		new DefaultText(1).setText("result");
		new DefaultTable(0).select(0);

		new PushButton("OK").click();

		new SwitchYardEditor().save();

		// Create HelloTest
		new Service("Hello").newServiceTestClass();
		new TextEditor("HelloTest.java").deleteLineWith("String message").type("String message=\"BPMN2\";")
				.deleteLineWith("assertTrue").type("Assert.assertEquals(\"Hello BPMN2\", result);").saveAndClose();
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
