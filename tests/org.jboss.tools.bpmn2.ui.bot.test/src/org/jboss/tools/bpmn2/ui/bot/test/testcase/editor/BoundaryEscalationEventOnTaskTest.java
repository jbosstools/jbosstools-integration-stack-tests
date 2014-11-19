package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.EscalationBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * ISSUE - language should be 'http://www.jboss.org/drools/rule' but it's not available.
 */
@ProcessDefinition(name="BPMN2-BoundaryEscalationEventOnTask", project="EditorTestProject")
public class BoundaryEscalationEventOnTaskTest extends JBPM6BaseTest {

	private static final String VARIABLE3 = "Property_3";
	
	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-BoundaryEscalationEventOnTask");
		process.addLocalVariable(VARIABLE1, "Object");
		process.addLocalVariable(VARIABLE2, "Object");
		process.addLocalVariable(VARIABLE3, "Object");
		Escalation escalation = new Escalation("MyEscalation", "java.lang.RuntimeException");
		process.addEscalation(escalation);
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Split", ElementType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway("Split");
		gateway.setDirection(Direction.DIVERGING);
		gateway.append("SubProcess", ElementType.SUB_PROCESS, Position.NORTH_EAST);
		gateway.append("User Task", ElementType.USER_TASK, Position.SOUTH_EAST);
		
		SubProcess subProcess = new SubProcess("SubProcess");
		subProcess.add("SubStart", ElementType.START_EVENT);
		subProcess.append("OnlyForSyntaxEnd", ElementType.END_EVENT);
		
		StartEvent subStart = new StartEvent("SubStart");
		subStart.append("ThrowEscalation", ElementType.ESCALATION_INTERMEDIATE_THROW_EVENT);
		
		EscalationIntermediateThrowEvent throwEvent = new EscalationIntermediateThrowEvent("ThrowEscalation");
		throwEvent.setEscalation(escalation, VARIABLE2);
		throwEvent.append("SubEnd", ElementType.END_EVENT);
		
		subProcess.add("EscalationBoundary", ElementType.ESCALATION_BOUNDARY_EVENT);
		
		EscalationBoundaryEvent boundaryEventSub = new EscalationBoundaryEvent("EscalationBoundary");
		boundaryEventSub.setEscalation(escalation, VARIABLE3);
		boundaryEventSub.append("SubHandler", ElementType.SCRIPT_TASK, Position.WEST);
		
		ScriptTask subHandler = new ScriptTask("SubHandler");
		subHandler.setScript("Java", "System.out.println(\"Handling SubProcess: \"+"+VARIABLE2 + "+\" \"+" + VARIABLE3+");");
		subHandler.append("SubHandlerEnd", ElementType.END_EVENT, Position.SOUTH);
		

		UserTask userTask = new UserTask("User Task");
		userTask.addActor("Mary");
		userTask.setOnExitScript("Java", "throw new RuntimeException();");
		userTask.append("EndProcess", ElementType.END_EVENT);
		userTask.addEvent("Escalation Boundary Event", ElementType.ESCALATION_BOUNDARY_EVENT);

		EscalationBoundaryEvent boundaryEvent = new EscalationBoundaryEvent("Escalation Boundary Event");
		boundaryEvent.setEscalation(escalation, VARIABLE1);
		boundaryEvent.append("Script Task", ElementType.SCRIPT_TASK, Position.SOUTH);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.setScript("Java", "System.out.println(\"Handling UserTask: \"+"+VARIABLE1 + ");");
		scriptTask.append("EndProcess2", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2BoundaryEscalationEventOnTask");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
}