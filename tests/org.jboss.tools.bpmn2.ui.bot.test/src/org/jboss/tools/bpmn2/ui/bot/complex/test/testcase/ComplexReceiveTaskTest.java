package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ReceiveTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jbpm.bpmn2.handler.ReceiveTaskHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-ReceiveTask.bpmn2", saveAs = "BPMN2-ReceiveTask.bpmn2")
public class ComplexReceiveTaskTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		ReceiveTask receive = (ReceiveTask) start.append("Receive", ElementType.RECEIVE_TASK);
		receive.setImplementation("Unspecified");
		receive.setMessage("HelloMessage", STRING);
		receive.setTarget(VARIABLE1);
		receive.connectTo(new TerminateEndEvent("EndProcess"));
	}

	@TestPhase(phase = Phase.RUN)
	public void assertRunOfProcessModel(KieSession kSession) {
		ReceiveTaskHandler receiveTaskHandler = new ReceiveTaskHandler(kSession);
		kSession.getWorkItemManager().registerWorkItemHandler("Receive Task", receiveTaskHandler);
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession.startProcess("BPMN2ReceiveTask");
		JbpmAssertions.assertProcessInstanceActive(processInstance, kSession);

		receiveTaskHandler.setKnowledgeRuntime(kSession);
		receiveTaskHandler.messageReceived("HelloMessage", "Hello john!");
		assertEquals("Hello john!", processInstance.getVariable("VariableOne"));
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
