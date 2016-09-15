package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.MessageIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.SendTaskWorkItemdHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-IntermediateThrowMessageEvent.bpmn2", saveAs = "BPMN2-IntermediateThrowMessageEvent.bpmn2")
public class ComplexIntermediateThrowMessageEventTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");

		MessageIntermediateThrowEvent ithrow = (MessageIntermediateThrowEvent) start.append("Message Event",
				ElementType.MESSAGE_INTERMEDIATE_THROW_EVENT);
		ithrow.setMessageMapping(new Message("_2_Message", STRING), VARIABLE1);
		ithrow.connectTo(new EndEvent("EndProcess"));
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "Message Event", "EndProcess"), null);
		kSession.addEventListener(triggered);
		kSession.getWorkItemManager().registerWorkItemHandler("Send Task", new SendTaskWorkItemdHandler("_2_Message"));
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateThrowMessageEvent");

		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
