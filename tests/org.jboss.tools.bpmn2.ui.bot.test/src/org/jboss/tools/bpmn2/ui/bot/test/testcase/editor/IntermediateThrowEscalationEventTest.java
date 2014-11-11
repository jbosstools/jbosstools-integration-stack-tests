package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.EventSubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.EscalationStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-IntermediateThrowEscalationEvent", project="EditorTestProject")
public class IntermediateThrowEscalationEventTest extends JBPM6BaseTest {

	private static final String VARIABLE = "procVar";

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateThrowEscalationEvent");
		process.addLocalVariable(VARIABLE, "String");
		process.addEscalation("EscName", "MyEscalation");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Escalation Event", ElementType.ESCALATION_INTERMEDIATE_THROW_EVENT);

		EscalationIntermediateThrowEvent ithrow = new EscalationIntermediateThrowEvent("Escalation Event");
		ithrow.setEscalation(new Escalation("EscName", "MyEscalation"), VARIABLE);
		ithrow.append("TotalEnd", ElementType.END_EVENT);
		
		process.select();
		process.add("EscalationHandler", ElementType.EVENT_SUB_PROCESS, ithrow, Position.SOUTH);
		
		EventSubProcess escalationHandler = new EventSubProcess("EscalationHandler");
		escalationHandler.add("HandlerStart", ElementType.ESCALATION_START_EVENT);		
		
		EscalationStartEvent handlerStart = new EscalationStartEvent("HandlerStart");
		handlerStart.setEscalation(new Escalation("EscName", "MyEscalation"), VARIABLE);
		handlerStart.append("EscalationPrinter", ElementType.SCRIPT_TASK);
		
		
		ScriptTask script = new ScriptTask("EscalationPrinter", escalationHandler);
		script.setScript("Java", "System.out.println(\"" + VARIABLE + "\");");
		script.append("HandlerEnd", ElementType.END_EVENT,Position.SOUTH_EAST);
		
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateThrowEscalationEvent");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}