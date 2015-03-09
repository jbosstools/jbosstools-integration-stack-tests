package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUES - When a connection is missing e.g. "Task 3" and "Gateway" are not connected 
 *          validator does not complain!
 */
@ProcessDefinition(name="BPMN2-AdHocProcess",  project="EditorTestProject")
public class AdHocProcessTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-AdHocProcess");
		process.setAddHoc(true);
		process.add("Task 3", ElementType.SCRIPT_TASK);

		new StartEvent("StartProcess").delete();
		
		ScriptTask task3 = new ScriptTask("Task 3");
		// ISSUE: Empty values can be set only at the beginning!
		task3.setScript(ScriptLanguage.JAVA, "System.out.println(\"Task3\");");
		task3.append("Gateway", ElementType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway gateway = new ExclusiveGateway("Gateway");
		gateway.append("End", ElementType.TERMINATE_END_EVENT, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway.append("Task 4", ElementType.SCRIPT_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		// Select the activity - it was deselected during append
		gateway.select();
		// Rules are not supported anymore
		gateway.setCondition("Gateway -> End", "Rule", "org.jbpm.bpmn2.objects.Person()");
		gateway.setCondition("Gateway -> Task 4", "Rule", "not org.jbpm.bpmn2.objects.Person()");
		
		ScriptTask task4 = new ScriptTask("Task 4");
		task4.setScript(ScriptLanguage.JAVA, "System.out.println(\"Task4\");");

		// Finish parallel activities
		process.add("Task 2", ElementType.SCRIPT_TASK, task3, Position.NORTH);
		
		ScriptTask task2 = new ScriptTask("Task 2");
		task2.setScript(ScriptLanguage.JAVA, "System.out.println(\"Task2\");");
		
		process.add("Task 1", ElementType.SCRIPT_TASK, task2, Position.NORTH);
		
		ScriptTask task1 = new ScriptTask("Task 1");
		task1.setScript(ScriptLanguage.JAVA, "System.out.println(\"Task1\");");
		
		process.add("User", ElementType.USER_TASK, task3, Position.SOUTH);
		new UserTask("User").setTaskName("TaskForUser");
	}
	
}