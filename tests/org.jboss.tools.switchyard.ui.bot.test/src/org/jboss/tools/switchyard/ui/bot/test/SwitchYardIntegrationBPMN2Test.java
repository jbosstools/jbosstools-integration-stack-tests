package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.execution.annotation.RunIf;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.switchyard.activities.SwitchYardServiceTask;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.component.SwitchYardComponent;
import org.jboss.tools.switchyard.reddeer.condition.JUnitHasFinished;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.project.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.project.SwitchYardProject;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement;
import org.jboss.tools.switchyard.reddeer.requirement.SwitchYardRequirement.SwitchYard;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.jboss.tools.switchyard.ui.bot.test.condition.SwitchYardRequirementSupportBPMN;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Create simple BPMN process with a switchyard service task, run JUnit test.
 * 
 * @author apodhrad
 * 
 */
@SwitchYard
@RunWith(RedDeerSuite.class)
public class SwitchYardIntegrationBPMN2Test {

	private static final String PROJECT = "switchyard-bpm-processgreet";
	private static final String PACKAGE = "org.switchyard.quickstarts.bpm.service";
	private static final String GROUP_ID = "org.switchyard.quickstarts.bpm.service";
	private static final String PROCESS_GREET = "ProcessGreet";
	private static final String BPMN_FILE_NAME = "ProcessGreet.bpmn";
	private static final String PACKAGE_MAIN_JAVA = "src/main/java";
	private static final String PACKAGE_MAIN_RESOURCES = "src/main/resources";
	private static final String EVAL_GREET = "EvalGreet";
	private static final String EVAL_GREET_BEAN = EVAL_GREET + "Bean";

	@InjectRequirement
	private static SwitchYardRequirement switchyardRequirement;

	@Before
	@After
	public void saveAndCloseAllEditors() {
		EditorHandler.getInstance().closeAll(true);
	}

	@Test
	@RunIf(conditionClass = SwitchYardRequirementSupportBPMN.class)
	public void switchyardBPMN2IntegrationTest() {
		new WorkbenchShell().maximize();

		// Create new Switchyard project, Add support for Bean, BPM
		switchyardRequirement.project(PROJECT).impl("Bean", "BPM (jBPM)").groupId(GROUP_ID).packageName(PACKAGE)
				.create();
		openFile(PROJECT, PACKAGE_MAIN_RESOURCES, "META-INF", "switchyard.xml");

		new SwitchYardEditor().addBPMNImplementation().setFileName(BPMN_FILE_NAME).createJavaInterface(PROCESS_GREET)
				.finish();
		new SwitchYardEditor().autoLayout();
		new SwitchYardEditor().save();

		new SwitchYardEditor().addBeanImplementation().createJavaInterface(EVAL_GREET).finish();
		new SwitchYardEditor().save();

		// reference to bean
		new SwitchYardComponent(PROCESS_GREET).getContextButton("Reference").click();
		new ReferenceWizard().selectJavaInterface(EVAL_GREET).finish();
		new SwitchYardEditor().save();

		// declare ProcessGreet interface
		new Service(PROCESS_GREET, 0).openTextEditor();
		new TextEditor(PROCESS_GREET + ".java").setText("package org.switchyard.quickstarts.bpm.service;\n\n"
				+ "public interface ProcessGreet {\n" + "\tpublic boolean checkGreetIsPolite(String greet);\n" + "}");
		// declare EvalGreet interface
		openFile(PROJECT, PACKAGE_MAIN_JAVA, PACKAGE, EVAL_GREET + ".java");
		new TextEditor(EVAL_GREET + ".java").setText("package org.switchyard.quickstarts.bpm.service;\n\n"
				+ "public interface EvalGreet {\n" + "\tpublic boolean checkGreet(String greet);\n" + "}");
		// implement EvalGreetBean
		openFile(PROJECT, PACKAGE_MAIN_JAVA, PACKAGE, EVAL_GREET_BEAN + ".java");
		new TextEditor(EVAL_GREET_BEAN + ".java").setText("package org.switchyard.quickstarts.bpm.service;\n\n"
				+ "import org.switchyard.component.bean.Service;\n\n" + "@Service(EvalGreet.class)"
				+ "public class EvalGreetBean implements EvalGreet {\n\n" + "\t@Override\n"
				+ "\tpublic boolean checkGreet(String greet) {\n"
				+ "\t\treturn (greet.equals(\"Good evening\")) ? true : false;\n" + "}\n" + "}");

		// BPM Process and its properties
		openFile(PROJECT, PACKAGE_MAIN_RESOURCES, BPMN_FILE_NAME);
		new DefaultEditor(PROCESS_GREET);
		new WorkbenchShell().setFocus();
		new DefaultEditor(PROCESS_GREET);
		new Process(null).setName(PROCESS_GREET);

		new TerminateEndEvent("EndProcess").delete();
		new StartEvent("StartProcess").append("EvalGreet", ElementType.SWITCHYARD_SERVICE_TASK);
		SwitchYardServiceTask task = new SwitchYardServiceTask("EvalGreet");
		task.setTaskAttribute("Operation Name", "checkGreet");
		task.setTaskAttribute("Service Name", EVAL_GREET);
		task.addParameterMapping(new ParameterMapping(new FromVariable("Parameter"),
				new ToDataInput("Parameter", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromDataOutput("Result", "String"), new ToVariable("Result"),
				ParameterMapping.Type.OUTPUT));
		task.append("EndProcess", ElementType.TERMINATE_END_EVENT);

		checkBug_SWITCHYARD_2484();

		openFile(PROJECT, PACKAGE_MAIN_RESOURCES, "META-INF", "switchyard.xml");

		// Junit
		new Service(PROCESS_GREET, 0).createNewServiceTestClass();
		new TextEditor(PROCESS_GREET + "Test.java").setText("package org.switchyard.quickstarts.bpm.service;\n\n"
				+ "import org.junit.Assert;\n" + "import org.junit.Test;\n" + "import org.junit.runner.RunWith;\n"
				+ "import org.switchyard.component.test.mixins.cdi.CDIMixIn;\n"
				+ "import org.switchyard.test.Invoker;\n" + "import org.switchyard.test.ServiceOperation;\n"
				+ "import org.switchyard.test.SwitchYardRunner;\n"
				+ "import org.switchyard.test.SwitchYardTestCaseConfig;\n"
				+ "import org.switchyard.test.SwitchYardTestKit;\n\n" + "@RunWith(SwitchYardRunner.class)\n"
				+ "@SwitchYardTestCaseConfig(config = SwitchYardTestCaseConfig.SWITCHYARD_XML, mixins = { CDIMixIn.class })\n"
				+ "public class ProcessGreetTest {\n\n" + "\tprivate SwitchYardTestKit testKit;\n"
				+ "\tprivate CDIMixIn cdiMixIn;\n" + "\t@ServiceOperation(\"ProcessGreet\")\n"
				+ "\tprivate Invoker service;\n\n" + "\t@Test\n"
				+ "\tpublic void testCheckGreetIsPolite() throws Exception {\n"
				+ "\t\tboolean result = service.operation(\"checkGreetIsPolite\").sendInOut(\"Good evening\").getContent(boolean.class);\n"
				+ "\t\tAssert.assertTrue(result);\n"
				+ "\t\tAssert.assertFalse(service.operation(\"checkGreetIsPolite\").sendInOut(\"hi\").getContent(Boolean.class));\n"
				+ "\t}\n" + "}");

		try {
			new ShellMenu("File", "Save All").select();
			new DefaultShell("Configure BPMN2 Project");
			new PushButton("No").click();
		} catch (Exception e) {
			// ok, no shell was popup
		}

		new SwitchYardEditor().save();
		new SwitchYardProject(PROJECT).update(); 

		new ProjectExplorer().open();
		ProjectItem item = new SwitchYardProject(PROJECT).getProjectItem("src/test/java", PACKAGE,
				PROCESS_GREET + "Test.java");
		new ProjectItemExt(item).runAsJUnitTest();
		new WaitUntil(new JUnitHasFinished(), TimePeriod.LONG);

		assertEquals("1/1", new JUnitView().getRunStatus());
		assertEquals(0, new JUnitView().getNumberOfErrors());
		assertEquals(0, new JUnitView().getNumberOfFailures());
	}

	private void openFile(String... file) {
		// focus on project explorer
		new WorkbenchView("General", "Project Explorer").open();
		new DefaultTreeItem(file).doubleClick();
	}

	private void checkBug_SWITCHYARD_2484() {
		try {
			new ShellMenu("File", "Save All").select();
			new DefaultShell("Configure BPMN2 Project");
			new PushButton("No").click();
			;
		} catch (Exception e) {
			// ok, no shell was popup
		}

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ProblemsView problemsView = new ProblemsView();
		problemsView.open();
		for (Problem error : problemsView.getProblems(ProblemType.ERROR)) {
			System.out.println(error.getDescription());
			if (error.getDescription().startsWith("Data Input Association has missing or incomplete Source")) {
				Assert.fail("SWITCHYARD_2484: SwitchYard Sevice Task generates wrong data inputs");
			}
		}
	}

}
