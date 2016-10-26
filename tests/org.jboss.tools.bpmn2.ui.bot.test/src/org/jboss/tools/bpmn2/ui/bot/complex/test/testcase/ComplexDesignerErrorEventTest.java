package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-DesignerErrorEvent.bpmn2", saveAs = "BPMN2-DesignerErrorEvent.bpmn2", knownIssues={"RHBPMS-4342"})
public class ComplexDesignerErrorEventTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("start");
		start.setName("Start");
	}
	
	@TestPhase(phase = Phase.VALIDATE)
	public void checkErrorEventDefinition() {
		Process process = new Process("BPMN2-DesignerErrorEvent");
		List<ErrorRef> definedErrors = process.getErrors();
		assertEquals(1, definedErrors.size());
		assertEquals("java.lang.RuntimeException", definedErrors.get(0).getDataType());
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2DesignerErrorEvent");
		JbpmAssertions.assertProcessInstanceAborted(processInstance, kSession);
	}
}
