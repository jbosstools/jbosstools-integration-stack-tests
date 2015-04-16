package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-IntermediateThrowEscalationEvent.bpmn2",
							saveAs="BPMN2-IntermediateThrowEscalationEvent.bpmn2",
							knownIssues={"1184422"})
public class ComplexIntermediateThrowEscalationEventTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		
		StartEvent start = new StartEvent("StartProcess");
		
		EscalationIntermediateThrowEvent ithrow = 
			(EscalationIntermediateThrowEvent) start.append("Escalation Event", ElementType.ESCALATION_INTERMEDIATE_THROW_EVENT);
		ithrow.setEscalation(new Escalation("EscName", "MyEscalation"), VARIABLE1);
		ithrow.connectTo(new EndEvent("TotalEnd"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateThrowEscalationEvent");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
