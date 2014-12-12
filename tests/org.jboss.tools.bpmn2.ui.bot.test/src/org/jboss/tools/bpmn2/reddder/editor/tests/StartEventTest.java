package org.jboss.tools.bpmn2.reddder.editor.tests;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Signal;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.MessageStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.SignalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.TimerStartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ImportResourceRequirement.ImportResource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

/**
 * @BZ https://bugzilla.redhat.com/show_bug.cgi?id=1173140
 * @author jomarko
 *
 */

@ImportResource(projectName="BpmnTestProject", folderToImportName="resources/bpmn2/model/base", baseDiagramFileName="StartEventTest.bpmn2")
public class StartEventTest extends ImportResourceTest{

	private static final String BASE_FILE = "StartEventTest.bpmn2";
	private static final String PROCESS = "StartEventTest";
	
	@Before
	public void loadProcess() {
		this.process = new Process(PROCESS);
		String[] parts = methodName.getMethodName().split(" ");
		processId = PACKAGE_BASE + parts[0] + "." + PROCESS;
		//process.setId(processId);
	}
	
	@Test
	public void startEventTest() {
		process.add("NormalStart", ElementType.START_EVENT);
		new StartEvent("NormalStart").connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Normal" + BASE_FILE);
//		runNormal(processId);
	}
	
	private void runNormal(String processId){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(processId);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void conditionalStartEventTest() {
		process.add("ConditionalStart", ElementType.CONDITIONAL_START_EVENT);
		ConditionalStartEvent start = new ConditionalStartEvent("ConditionalStart");
		start.setCondition("Rule", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Conditional" + BASE_FILE);
//		runConditional(processId);
	}
	
	private void runConditional(String processId){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(processId);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void timerStartEventTest() {
		process.add("TimerStart", ElementType.TIMER_START_EVENT);
		TimerStartEvent start = new TimerStartEvent("TimerStart");
		start.setTimer("5000ms");
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Timer" + BASE_FILE);
//		runTimer(processId);
	}
	
	private void runTimer(String processId){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(processId);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void messageStartEventTest() {
		process.addMessage("HelloMessage", "String");
		process.add("MessageStart", ElementType.MESSAGE_START_EVENT);
		
		MessageStartEvent start = new MessageStartEvent("MessageStart");
		start.setMessageMapping(new Message("HelloMessage", "String"), STRING_VAR_ONE);
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Message" + BASE_FILE);
//		runMessage(processId);
	}
	
	private void runMessage(String processId){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(processId);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void signalStartEventTest() {
		process.addSignal("StartSignal");
		process.add("SignalStart", ElementType.SIGNAL_START_EVENT);
		SignalStartEvent start = new SignalStartEvent("SignalStart");
		start.setSignal(new Signal("StartSignal"), OBJECT_VAR);
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Signal" + BASE_FILE);
//		runSignal(processId);
	}
	
	private void runSignal(String processId){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(processId);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
