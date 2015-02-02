package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SendTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.SendTaskWorkItemdHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-SendTask.bpmn2",
							saveAs="BPMN2-SendTask.bpmn2")
public class ComplexSendTaskTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		Message msg = new Message("msgName", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		
		SendTask send = (SendTask) start.append("Send", ElementType.SEND_TASK);
		send.setImplementation("Unspecified");
		send.setOperation("Interface 1/operationName", msg, msg, new ErrorRef("errName", "errCode", "String"));
		send.setMessage(msg.getName(), msg.getDataType());
		send.addParameterMapping(new ParameterMapping(new FromVariable("s"), new ToDataInput(msg.getName(), msg.getDataType()), ParameterMapping.Type.INPUT));
		send.connectTo(new TerminateEndEvent("EndProcess"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Send", "EndProcess"), null);
		kSession.addEventListener(triggeredNodes);
		
		kSession.getWorkItemManager().registerWorkItemHandler("Send Task", new SendTaskWorkItemdHandler("msgName"));
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("s", "Initialized string");
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2SendTask", params);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
