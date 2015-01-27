package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.TimerIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-IntermediateCatchEventTimerCycle.bpmn2",
							saveAs="BPMN2-IntermediateCatchEventTimerCycle.bpmn2")
public class ComplexIntermediateCatchEventTimerCycleTest extends JBPM6ComplexTest {
	
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Timer", ElementType.TIMER_INTERMEDIATE_CATCH_EVENT);
		
		TimerIntermediateCatchEvent catchEvent = new TimerIntermediateCatchEvent("Timer");
		catchEvent.setTimer("500ms");
		catchEvent.connectTo(new ScriptTask("Event"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateCatchEventTimerCycle");
		
		try {
		    Thread.sleep(550);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

}
