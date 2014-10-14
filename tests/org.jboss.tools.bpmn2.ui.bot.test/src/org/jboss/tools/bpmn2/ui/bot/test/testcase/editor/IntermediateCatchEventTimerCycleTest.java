package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.TimerIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * ISSUES - Invalid timer string is not found! E.g. 500###ms
 */
@ProcessDefinition(name="BPMN2-IntermediateCatchEventTimerCycle", project="EditorTestProject")
public class IntermediateCatchEventTimerCycleTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Timer", ElementType.TIMER_INTERMEDIATE_CATCH_EVENT);
		
		TimerIntermediateCatchEvent catchEvent = new TimerIntermediateCatchEvent("Timer");
		catchEvent.setTimer("500ms");
		catchEvent.append("Event", ElementType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Event");
		scriptTask.setScript("Java", "System.out.println(\"Timer triggered\");");
		scriptTask.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2IntermediateCatchEventTimerCycle");
		
		try {
		    Thread.sleep(550);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

	
	
}