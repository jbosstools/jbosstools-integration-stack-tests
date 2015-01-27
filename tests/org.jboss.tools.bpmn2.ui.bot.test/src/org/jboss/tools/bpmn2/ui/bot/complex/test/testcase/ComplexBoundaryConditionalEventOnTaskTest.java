package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-BoundaryConditionalEventOnTask.bpmn2",
							saveAs="BPMN2-BoundaryConditionalEventOnTask.bpmn2")
public class ComplexBoundaryConditionalEventOnTaskTest extends JBPM6ComplexTest{

	@TestPhase(phase=Phase.MODEL)
	public void model() {
				
		UserTask userTask1 = new UserTask("User Task");
		
		// ISSUE: language should be 'http://www.jboss.org/drools/rule' but it's not available.
		ConditionalBoundaryEvent boundaryEvent = 
			(ConditionalBoundaryEvent) userTask1.addEvent("Conditional Boundary Event", ElementType.CONDITIONAL_BOUNDARY_EVENT);
		boundaryEvent.setScript("Rule", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		
		boundaryEvent.connectTo(new ScriptTask("Condition met"));
		
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler workItemHandler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
		
		
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList("StartProcess" ,
				"User Task", "User Task 2", "End 1", "End 2", "Condition met"), null);
		kSession.addEventListener(triggeredNodes);
		
		org.jbpm.bpmn2.objects.Person person = new org.jbpm.bpmn2.objects.Person();
		person.setName("john");
		kSession.insert(person);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2BoundaryConditionalEventOnTask");
		kSession.fireAllRules();
		WorkItem workItem = workItemHandler.getWorkItem("User Task");
		if(workItem != null) {
			workItemHandler.completeWorkItem(workItem,kSession.getWorkItemManager());
		}
		workItem = workItemHandler.getWorkItem("User Task 2");
		workItemHandler.completeWorkItem(workItem, kSession.getWorkItemManager());
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
