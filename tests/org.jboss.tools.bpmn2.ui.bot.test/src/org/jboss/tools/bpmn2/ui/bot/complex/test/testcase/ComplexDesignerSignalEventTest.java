package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-DesignerSignalEvent.bpmn2", saveAs = "BPMN2-DesignerSignalEvent.bpmn2")
public class ComplexDesignerSignalEventTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("start");
		start.setName("Start");
	}
	
	@TestPhase(phase = Phase.VALIDATE)
	public void checkSignalDefinition() {
		Process process = new Process("BPMN2-DesignerSignalEvent");
		List<Signal> definedSignals = process.getSignals();
		assertEquals(1, definedSignals.size());
		assertEquals("com.sample.MySignal", definedSignals.get(0).getName());
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("Start", "End"), new ArrayList<String>());
		triggered.setExpectedFinalProcessState(ProcessInstance.STATE_COMPLETED);
		kSession.addEventListener(triggered);
		
		kSession.signalEvent("com.sample.MySignal", "any data");
		assertEquals(true, triggered.wasCompletitionReached());
	}
}
