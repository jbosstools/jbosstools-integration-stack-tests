package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.EscalationBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-BoundaryEscalationEventOnTask.bpmn2",
							saveAs="BPMN2-BoundaryEscalationEventOnTask.bpmn2")
public class ComplexBoundaryEscalationEventOnTaskTest extends JBPM6ComplexTest {
	
	private static final String VARIABLE3 = "Property_3";
	
	@TestPhase(phase=Phase.MODEL)
	public void model(){
		Escalation escalation = new Escalation("MyEscalation", "java.lang.RuntimeException");
		
		SubProcess subProcess = new SubProcess("SubProcess");
		
		StartEvent subStart = new StartEvent("SubStart");
		subStart.append("ThrowEscalation", ElementType.ESCALATION_INTERMEDIATE_THROW_EVENT);
		
		EscalationIntermediateThrowEvent throwEvent = new EscalationIntermediateThrowEvent("ThrowEscalation");
		throwEvent.setEscalation(escalation, VARIABLE2);
		throwEvent.connectTo(new EndEvent("SubEnd"));
		
		subProcess.add("EscalationBoundary", ElementType.ESCALATION_BOUNDARY_EVENT);
		
		EscalationBoundaryEvent boundaryEventSub = new EscalationBoundaryEvent("EscalationBoundary");
		boundaryEventSub.setEscalation(escalation, VARIABLE3);
		boundaryEventSub.connectTo(new ScriptTask("SubHandler"));
		
		UserTask userTask = new UserTask("User Task");
		EscalationBoundaryEvent boundaryEvent =
			(EscalationBoundaryEvent) userTask.addEvent("Escalation Boundary Event", ElementType.ESCALATION_BOUNDARY_EVENT);
		
		boundaryEvent.setEscalation(escalation, VARIABLE1);
		boundaryEvent.connectTo(new ScriptTask("Script Task"));
		boundaryEvent.setCancelActivity(false);
		boundaryEvent.select();
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Split", "User Task", "Script Task", "EndProcess", "EndProcess2", 
				"SubHandler", "SubHandlerEnd", "SubStart", "ThrowEscalation"),
			Arrays.asList("OnlyForSyntaxEnd", "SubEnd"));
		
		kSession.addEventListener(triggered);
		
		Map<String, Object> sessionArgs = new HashMap<String, Object>();
		sessionArgs.put("Property_2", new java.lang.RuntimeException());
		
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
