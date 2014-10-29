package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.MessageIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.SendTaskWorkItemdHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-IntermediateThrowMessageEvent", project="EditorTestProject")
public class IntermediateThrowMessageEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateThrowMessageEvent");
		process.addLocalVariable("m", "String");
		process.addMessage("_2_Message", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Message Event", ElementType.MESSAGE_INTERMEDIATE_THROW_EVENT);

		MessageIntermediateThrowEvent ithrow = new MessageIntermediateThrowEvent("Message Event");
		ithrow.setMessageMapping(new Message("_2_Message", "String"), "m");
		ithrow.append("EndProcess", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Message Event", "EndProcess"), null);
		kSession.addEventListener(triggered);
		kSession.getWorkItemManager().registerWorkItemHandler("Send Task", new SendTaskWorkItemdHandler("_2_Message"));
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateThrowMessageEvent");
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
}