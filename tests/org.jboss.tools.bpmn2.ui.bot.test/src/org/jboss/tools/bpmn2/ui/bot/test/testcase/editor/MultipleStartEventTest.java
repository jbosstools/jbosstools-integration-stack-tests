package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.SignalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.TimerStartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-MultipleStartEvent", project="EditorTestProject")
public class MultipleStartEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		new StartEvent("StartProcess").delete();
		
		Process process = new Process("BPMN2-MultipleStartEvent");
		process.add("StartProcess", ElementType.SIGNAL_START_EVENT);
		
		SignalStartEvent start = new SignalStartEvent("StartProcess");
		start.setSignal(new Signal("BlockingSignal"));
		start.append("StartTimer", ElementType.TIMER_START_EVENT, Position.SOUTH);
		start.append("Split", ElementType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);

		ExclusiveGateway gateway = new ExclusiveGateway("Split");
		gateway.setDirection(Direction.CONVERGING);
		gateway.append("User Task", ElementType.USER_TASK);
		
		TimerStartEvent start2 = new TimerStartEvent("StartTimer");
		start2.setTimer("5000ms");
		start2.connectTo(gateway);
		
		UserTask task = new UserTask("User Task");
		task.addActor("john");
		task.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
			Arrays.asList("StartProcess", "Split", "User Task", "EndProcess"),
			Arrays.asList("StartTimer"));
		triggered.setExpectedFinalProcessState(ProcessInstance.STATE_COMPLETED);
		kSession.addEventListener(triggered);
		
		
		kSession.signalEvent("Signal_1", "any data");
		
		handler.completeWorkItem(handler.getWorkItem("User Task"), kSession.getWorkItemManager());
		
		assertTrue(triggered.wasCompletitionReached());
	}
}