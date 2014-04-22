package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ReceiveTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUE - Looks like this test creates on itemDefinition plus (should be 1 but there are 2)
 *         <itemDefinition .../>
 */
@ProcessDefinition(name="BPMN2-ReceiveTask", project="EditorTestProject")
public class ReceiveTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-ReceiveTask");
		process.addLocalVariable("s", "String");
		process.addMessage("HelloMessage", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Receive", ElementType.RECEIVE_TASK);
		
		ReceiveTask receive = new ReceiveTask("Receive");
		receive.setImplementation("Unspecified");
		receive.setMessage("HelloMessage", "String");
		receive.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}