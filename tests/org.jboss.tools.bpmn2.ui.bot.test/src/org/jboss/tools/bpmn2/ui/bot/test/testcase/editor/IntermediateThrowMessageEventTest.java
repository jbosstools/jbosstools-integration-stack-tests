package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.MessageIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-IntermediateThrowMessageEvent", project="EditorTestProject")
public class IntermediateThrowMessageEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateThrowMessageEvent");
		process.addLocalVariable("m", "String");
		Message msg = new Message("_2_Message", "String");
		process.addMessage(msg);
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Message Event", ElementType.MESSAGE_INTERMEDIATE_THROW_EVENT);

		MessageIntermediateThrowEvent ithrow = new MessageIntermediateThrowEvent("Message Event");
		ithrow.setMessageMapping(msg, "m");
		ithrow.append("EndProcess", ElementType.END_EVENT);
	}
	
}