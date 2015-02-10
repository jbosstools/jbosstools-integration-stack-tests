package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.CompensationBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.CompensationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-CompensationEvent.bpmn2",
							saveAs="BPMN2-CompensationEvent.bpmn2")
public class ComplexCompensationEvent extends JBPM6ComplexTest {
	
	private static final String EXPECTED_VALUE = "CompensatedValue";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		UserTask userTask = new UserTask("WillBeReRun");
		userTask.append("ThrowCompensation", ElementType.COMPENSATION_INTERMEDIATE_THROW_EVENT);
		
		CompensationIntermediateThrowEvent throwEvent = new CompensationIntermediateThrowEvent("ThrowCompensation");
		throwEvent.setCompensationActivity("WillBeReRun");
		throwEvent.connectTo(new EndEvent("EndProcess"));
		
		
		CompensationBoundaryEvent handlerStart = 
			(CompensationBoundaryEvent) userTask.addEvent("HandlerStart", ElementType.COMPENSATION_BOUNDARY_EVENT);
		handlerStart.setCompensationActivity("WillBeReRun");
		
		ScriptTask handler = new ScriptTask("Handler");
		handlerStart.connectTo(handler, ConnectionType.ASSOCIATION_ONE_WAY);
		handler.setIsForCompensation(true);
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(VARIABLE1, "NonCompensatedValue");
		
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession.startProcess("BPMN2CompensationEvent", args);
		
		handler.completeWorkItem(handler.getWorkItem("WillBeReRun"), kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		assertEquals(EXPECTED_VALUE, processInstance.getVariable(VARIABLE1));
	}
}
