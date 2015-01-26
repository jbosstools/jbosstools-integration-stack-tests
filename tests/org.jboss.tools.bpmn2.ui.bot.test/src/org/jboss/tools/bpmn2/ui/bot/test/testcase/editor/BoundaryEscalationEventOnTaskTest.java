package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;

/**
 * ISSUE - language should be 'http://www.jboss.org/drools/rule' but it's not available.
 */
@ProcessDefinition(name="BPMN2-BoundaryEscalationEventOnTask", project="EditorTestProject")
public class BoundaryEscalationEventOnTaskTest extends JBPM6BaseTest {

	private static final String VARIABLE3 = "VariableThree";
	
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
		subHandler.setScript("Java", "System.out.println(\"Handling SubProcess: \" + "+VARIABLE2 + " +\" \" + " + VARIABLE3+");");
		subHandler.append("SubHandlerEnd", ElementType.END_EVENT, Position.SOUTH);
		
		UserTask userTask = new UserTask("User Task");
		userTask.addActor("Mary");
		userTask.setOnExitScript("Java", "throw new RuntimeException();");
		userTask.append("EndProcess", ElementType.END_EVENT);
		
		EscalationBoundaryEvent boundaryEvent =
			(EscalationBoundaryEvent) userTask.addEvent("Escalation Boundary Event", ElementType.ESCALATION_BOUNDARY_EVENT);
		
		boundaryEvent.setEscalation(escalation, VARIABLE1);
		boundaryEvent.append("Script Task", ElementType.SCRIPT_TASK, Position.NORTH_EAST);
		boundaryEvent.setCancelActivity(false);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.setScript("Java", "System.out.println(\"Handling UserTask: \" + "+VARIABLE1 + ");");
		scriptTask.append("EndProcess2", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Split", "User Task", "Script Task", "EndProcess", "EndProcess2", 
				"SubHandler", "SubHandlerEnd", "SubStart", "ThrowEscalation"),
			Arrays.asList("OnlyForSyntaxEnd", "SubEnd"));
		
		kSession.addEventListener(triggered);
		
		Map<String, Object> sessionArgs = new HashMap<String, Object>();
		sessionArgs.put(VARIABLE2, new java.lang.RuntimeException());
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2BoundaryEscalationEventOnTask", sessionArgs);
		
		WorkItem item = handler.getWorkItem("User Task");
		while(item == null){
			System.out.println("waiting");
			item = handler.getWorkItem("User Task");
		}
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		Object var1 = ((WorkflowProcessInstance) processInstance).getVariable(VARIABLE1);
		Object var2 = ((WorkflowProcessInstance) processInstance).getVariable(VARIABLE2);
		Object var3 = ((WorkflowProcessInstance) processInstance).getVariable(VARIABLE3);
		
		assertNotNull(VARIABLE1 + " shouldn't be null", var1);
		assertNotNull(VARIABLE2 + " shouldn't be null", var2);
		assertNotNull(VARIABLE3 + " shouldn't be null", var3);
		
		assertTrue(VARIABLE1 + " should be instance of RuntimeException", var1 instanceof RuntimeException);
		assertTrue(VARIABLE2 + " should be instance of RuntimeException", var2 instanceof RuntimeException);
		assertTrue(VARIABLE3 + " should be instance of RuntimeException", var3 instanceof RuntimeException);
	}
	
}