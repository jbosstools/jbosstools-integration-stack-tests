package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.MessageStartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-MessageStart.bpmn2",
							saveAs="BPMN2-MessageStart.bpmn2")
public class ComplexMessageStartTest extends JBPM6ComplexTest{
	
	private String changedVar = "";

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		Process process = new Process("BPMN2-MessageStart");
		
		MessageStartEvent start = (MessageStartEvent) process.add("StartProcess", ElementType.MESSAGE_START_EVENT);
		start.setMessageMapping(new Message("HelloMessage", "String"), VARIABLE1);
		start.connectTo(new ScriptTask("Script"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
 		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Script", "EndProcess"), null); 
		kSession.addEventListener(triggered);
		
		kSession.addEventListener(new DefaultProcessEventListener() {
			public void afterProcessStarted(ProcessStartedEvent event) {
			
				changedVar = (String) ((WorkflowProcessInstance) event.getProcessInstance()).getVariable(VARIABLE1);
			}
		});
		
		
		kSession.signalEvent("Message-HelloMessage", "bpmn rules world");
		
		assertEquals("Variable "+VARIABLE1+" schould change because of start message", "bpmn rules world", changedVar);
		assertEquals("There shouldn't be a active process instance", 0, kSession.getProcessInstances().size());

	}
}
