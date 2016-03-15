package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.properties.SectionToolItem;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-SpecialCharactersAndLengths.bpmn2", saveAs = "BPMN2-SpecialCharactersAndLenghths.bpmn2", noErrorsInValidation = false)
public class ComplexSpecialCharactersAndLenghtsTest extends JBPM6ComplexTest {

	private static final String PROCESS = "BPMN2-SpecialCharactersAndLengths";
	private static final String PROBLEM_ONE = "Global Variable";

	private Process process;
	private UserTask userTask;

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		process = new Process(PROCESS);
		process.addLocalVariable("var-iable", "String", true);
		new SectionToolItem("Global Variable List for Process \"BPMN2-SpecialCharactersAndLengths\"", "Add").click();
		process.setExecutable(false);
		process.setPackageName("1 invalid");
		process.addInterface("Document", "org.w3c.dom.Document");

		StringBuilder longTaskName = new StringBuilder();
		for (int i = 0; i < 300; i++) {
			longTaskName.append("X");
		}

		userTask = new UserTask("User Task 1");
		userTask.setTaskName(longTaskName.toString());
		userTask.setGroupId("#{variable},secondGroup");
		userTask.setPriority("2147483660");

		process.click();
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1188153() {
		String error = getErrorsFromProblemsView(PROBLEM_ONE);
		assertTrue("BZ 1188153, data type of variable", error.startsWith(PROBLEM_ONE));
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1189365() {
		assertTrue("BZ 1189365, isExucutable shouldn't be removed from source code",
				isInSourceCode("isExecutable=\"false\""));
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1179075() {
		process.click();
		String variableName = process.getFirstLocalVariable();
		assertEquals("BZ 1179075, invalid characters in variable name", "variable", variableName);
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1182875() {
		assertTrue("BZ 1182875, package name", isInSourceCode("tns:packageName=\"invalid\""));
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1113139() {
		assertEquals("BZ 1113139, special characters in group id", "#{variable},secondGroup", userTask.getGroupId());
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1167754() {
		assertTrue("BZ 1167754, very very big priority", isInSourceCode(">2147483660</bpmn2:from>"));
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1179057() {
		assertEquals("BZ 1179057, max length of user task attributes", 255, userTask.getTaskName().length());
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validateBZ1188909() {
		assertTrue("BZ 1188909, interface package validation", isInSourceCode(
				"<bpmn2:interface id=\"Interface_1\" implementationRef=\"org.w3c.dom.Document\" name=\"Document\"/>"));
	}
}
