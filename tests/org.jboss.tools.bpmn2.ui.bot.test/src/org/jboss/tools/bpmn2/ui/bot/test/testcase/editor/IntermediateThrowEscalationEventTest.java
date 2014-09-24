package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-IntermediateThrowEscalationEvent", project="EditorTestProject")
public class IntermediateThrowEscalationEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateThrowEscalationEvent");
		process.addLocalVariable("variable", "String");
		process.addEscalation("EscName", "MyEscalation", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Escalation Event", ElementType.ESCALATION_INTERMEDIATE_THROW_EVENT);

		EscalationIntermediateThrowEvent ithrow = new EscalationIntermediateThrowEvent("Escalation Event");
		ithrow.setEscalation(new Escalation("EscName", "MyEscalation", "String"), "variable");
		ithrow.append("EndProcess", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateThrowEscalationEvent");
		assertTrue(processInstance.getState() == ProcessInstance.STATE_COMPLETED);
	}
}