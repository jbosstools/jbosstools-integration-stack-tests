package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-ImportInterface.bpmn2", saveAs = "BPMN2-ImportInterface.bpmn2")
/**
 * @BZ 1011163, 1040161, 1052918
 */
public class ComplexImportInterfaceTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		Process process = new Process("BPMN2-ImportInterface");
		process.importInterface("java.util.Date");
		process.click();
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession
				.startProcess("BPMN2ImportInterface");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
