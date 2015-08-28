package org.jboss.tools.switchyard.ui.bot.test;

import static org.jboss.tools.switchyard.ui.bot.test.util.TemplateHandler.javaSource;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.uiforms.api.Section;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.widget.DefaultTextExt;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.ui.bot.test.util.TemplateHandler;
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
@SwitchYard
@RunWith(RedDeerSuite.class)
public class BottomUpBPMN2Test {

	public static final String PROJECT = "bpmn2_project";
	public static final String PACKAGE = "com.example.switchyard.bpmn2_project";
	public static final String BPMN2_FILE = "sample.bpmn";

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
	public void bottomUpBPMN2Test() throws Exception {
		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("package", PACKAGE);
		dataModel.put("project", PROJECT);
		dataModel.put("body", "${body}");
		
		new WorkbenchShell().maximize();

		switchyardRequirement.project(PROJECT).impl("BPM (jBPM)").create();

		// Import BPMN process
		new SwitchYardProject(PROJECT).getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/bpmn", BPMN2_FILE);

		// Add component
		new SwitchYardEditor().addComponent();
		new SwitchYardEditor().addBPMNImplementation(new SwitchYardComponent("Component"));

		// Select existing implementation
		new PushButton("Browse...").click();
		new DefaultShell("Select Resource");
		new DefaultText(0).setText(BPMN2_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		new PushButton("Finish").click();

		// Create new service and interface
		new SwitchYardComponent("Component").getContextButton("Service").click();
		new NewServiceWizard().activate().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		TextEditor textEditor = new TextEditor("Hello.java");
		textEditor.setText(TemplateHandler.javaSource("Hello.java", dataModel));
		textEditor.save();
		textEditor.close(true);

		// Edit the BPMN process
		new SwitchYardComponent("Component").showProperties();
		new DefaultTreeItem("Implementation").select();
		new LabeledText("Process ID:").setText("com.sample.bpmn.hello");
		new DefaultTabItem("Operations").activate();

		Section operationsSection = new DefaultSection("Operations");
		new PushButton(operationsSection, "Add").click();
		new DefaultTable(operationsSection).getItem(0).doubleClick(0);
		new DefaultTextExt(operationsSection, 0).setText("sayHello");
		new DefaultTable(operationsSection).select(0);

		Section inputsSection = new DefaultSection("Inputs");
		new PushButton(inputsSection, "Add").click();
		new DefaultTable(inputsSection).getItem(0).doubleClick(1);
		new DefaultTextExt(inputsSection, 1).setText("name");
		new DefaultTable(inputsSection).select(0);

		Section outputsSection = new DefaultSection("Outputs");
		new PushButton(outputsSection, "Add").click();
		new DefaultTable(outputsSection).getItem(0).doubleClick(0);
		new DefaultTextExt(outputsSection, 0).setText("result");
		new DefaultTable(outputsSection).select(0);

		new PushButton("OK").click();

		new SwitchYardEditor().save();

		// Create HelloTest
		new Service("Hello").createNewServiceTestClass();
		textEditor = new TextEditor("HelloTest.java");
		textEditor.setText(javaSource("HelloSimpleTest.java", dataModel));
		textEditor.save();
		textEditor.close(true);

		// Run the test
		new ProjectExplorer().open();
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
