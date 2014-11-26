package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
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
 * ISSUES - When a connection is missing e.g. "Task 3" and "Gateway" are not connected 
 *          validator does not complain!
 */
@ProcessDefinition(name="BPMN2-AdHocProcess",  project="EditorTestProject")
public class AdHocProcessTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-AdHocProcess");
		//process.addImport("org.jbpm.bpmn2.objects.Person");
		process.setAddHoc(true);
		//process.add("Task 3", ElementType.SCRIPT_TASK);

		StartEvent start = new StartEvent("StartProcess");
		start.append("Task 3", ElementType.SCRIPT_TASK);
		
		ScriptTask task3 = new ScriptTask("Task 3");
		// ISSUE: Empty values can be set only at the beginning!
		task3.setScript("", "System.out.println(\"Task3\");");
		task3.append("Gateway", ElementType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway gateway = new ExclusiveGateway("Gateway");
		gateway.setDirection(Direction.DIVERGING);
		gateway.append("User", ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway.append("Task 4", ElementType.SCRIPT_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);

		// Rules are not supported anymore
		gateway.setCondition("Gateway -> User", "Rule", "org.jbpm.bpmn2.objects.Person()");
		gateway.setCondition("Gateway -> Task 4", "Rule", "not org.jbpm.bpmn2.objects.Person()");
		
		UserTask userTask = new UserTask("User");
		userTask.append("End", ElementType.TERMINATE_END_EVENT);
		
		ScriptTask task4 = new ScriptTask("Task 4");
		task4.setScript("", "System.out.println(\"Task4\");");
		task4.append("ScriptEnd", ElementType.END_EVENT, ConnectionType.SEQUENCE_FLOW, Position.EAST);

		// Finish parallel activities
		process.add("Task 2", ElementType.SCRIPT_TASK, task3, Position.NORTH);
		
		ScriptTask task2 = new ScriptTask("Task 2");
		task2.setScript("", "System.out.println(\"Task2\");");
		
		process.add("Task 1", ElementType.SCRIPT_TASK, task2, Position.NORTH);
		
		ScriptTask task1 = new ScriptTask("Task 1");
		task1.setScript("", "System.out.println(\"Task1\");");
}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "Task 1", "Task 2", "Task 3", "User", "Gateway", "End"), 
				Arrays.asList("ScriptEnd", "Task 4"));
		kSession.addEventListener(triggered);
		
		org.jbpm.bpmn2.objects.Person person = new org.jbpm.bpmn2.objects.Person();
		person.setName("john");
		kSession.insert(person);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2AdHocProcess");
		
		kSession.signalEvent("Task 1", null, processInstance.getId());
		kSession.signalEvent("Task 2", null, processInstance.getId());
		
		WorkItem item = handler.getWorkItem("User");
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
}