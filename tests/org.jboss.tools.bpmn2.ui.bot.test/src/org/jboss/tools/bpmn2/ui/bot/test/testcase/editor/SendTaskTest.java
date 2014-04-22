package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SendTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUES - Engine does not validate the presence of the rules.
 */
@ProcessDefinition(name="BPMN2-SendTask", project="EditorTestProject")
public class SendTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-SendTask");
		process.addLocalVariable("s", "String");
		process.addMessage("_2_Message", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Send", ElementType.SEND_TASK);

		SendTask send = new SendTask("Send");
		send.setImplementation("Unspecified");
		send.setMessage("_2_Message", "String");
		send.addParameterMapping(new ParameterMapping(new FromVariable("BPMN2-SendTask/s"), new ToDataInput("Message"), ParameterMapping.Type.INPUT));
		send.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}