package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

/**
 * ISSUE - language should be 'http://www.jboss.org/drools/rule' but it's not available.
 */
@ProcessDefinition(name="BPMN2-BoundaryConditionalEventOnTask", project="EditorTestProject", needPerson=true)
public class BoundaryConditionalEventOnTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-BoundaryConditionalEventOnTask");
		process.addImport("org.jbpm.bpmn2.objects.Person");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ElementType.USER_TASK);
		
		UserTask userTask1 = new UserTask("User Task");
		userTask1.addActor("john");
		userTask1.append("User Task 2", ElementType.USER_TASK, Position.NORTH_EAST);
		userTask1.addEvent("Conditional Boundary Event", ElementType.CONDITIONAL_BOUNDARY_EVENT);

		// ISSUE: language should be 'http://www.jboss.org/drools/rule' but it's not available.
		ConditionalBoundaryEvent boundaryEvent = new ConditionalBoundaryEvent("Conditional Boundary Event");
		boundaryEvent.setScript("Rule", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		
		boundaryEvent.append("Condition met", ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		UserTask userTask2 = new UserTask("User Task 2");
		userTask2.addActor("john");
		userTask2.append("End 1", ElementType.END_EVENT);
		
		ScriptTask scriptTask1 = new ScriptTask("Condition met");
		scriptTask1.setScript("", "System.out.println(\"Conditional boundary event executed\";)");
		scriptTask1.append("End 2", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
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