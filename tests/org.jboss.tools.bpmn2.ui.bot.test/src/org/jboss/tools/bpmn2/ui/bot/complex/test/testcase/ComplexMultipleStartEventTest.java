package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.SignalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.TimerStartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-MultipleStartEvent.bpmn2",
							saveAs="BPMN2-MultipleStartEvent.bpmn2")
public class ComplexMultipleStartEventTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void model(){
		
		ExclusiveGateway gateway = new ExclusiveGateway("Split");
		
		Process process = new Process("BPMN2-MultipleStartEvent");
		
		SignalStartEvent start = (SignalStartEvent) process.add("StartProcess", ElementType.SIGNAL_START_EVENT);;
		start.setSignal(new Signal("BlockingSignal"), VARIABLE1);
		start.connectTo(gateway);

		TimerStartEvent start2 = (TimerStartEvent) process.add("StartTimer", ElementType.TIMER_START_EVENT, start, Position.SOUTH_WEST);
		start2.setTimer("5000ms");
		start2.connectTo(gateway);
		
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Split", "User Task", "EndProcess"),
			Arrays.asList("StartTimer"));
		triggered.setExpectedFinalProcessState(ProcessInstance.STATE_COMPLETED);
		kSession.addEventListener(triggered);
		
		kSession.signalEvent("BlockingSignal", "any data");
		ArrayList<ProcessInstance> list = new ArrayList<ProcessInstance>(kSession.getProcessInstances());
		assertEquals(1, list.size());
		handler.completeWorkItem(handler.getWorkItem("User Task"), kSession.getWorkItemManager());
		assertEquals(ProcessInstance.STATE_COMPLETED, list.get(0).getState());
	}
}
