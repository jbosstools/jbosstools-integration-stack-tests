package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-IntermediateCatchSignalSingle.bpmn2",
							saveAs="BPMN2-IntermediateCatchSignalSingle.bpmn2")
public class ComplexIntermediateCatchSignalSingleTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		UserTask userTask = new UserTask("User Task");
		
		SignalIntermediateCatchEvent catchEvent = 
			(SignalIntermediateCatchEvent) userTask.append("Catch", ElementType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
		catchEvent.setSignalMapping(new Signal("BatmanSignal"), VARIABLE1);
		catchEvent.connectTo(new ScriptTask("Script Task"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "User Task", "Catch", "Script Task", "EndProcess"), null);
		kSession.addEventListener(triggered);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateCatchSignalSingle");
		
		WorkItem item = handler.getWorkItem("User Task");
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		kSession.signalEvent("BatmanSignal", "batman is comming");
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		assertEquals("Process variable "+VARIABLE1+" didn't changed.", "batman is comming", ((WorkflowProcessInstance) processInstance).getVariable(VARIABLE1));

	}
}
