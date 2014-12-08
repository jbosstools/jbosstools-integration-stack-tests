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
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ImportResource(projectName="BpmnTestProject", folderToImportName="resources/bpmn2/model/base", baseDiagramFileName="StartEventTest.bpmn2")
public class StartEventTest extends ImportResourceTest{

	private static final String BASE_FILE = "StartEventTest.bpmn2";
	private static final String PROCESS = "StartEventTest";
	
	@Before
	public void loadProcess() {
		this.process = new Process(PROCESS);
	}
	
	@Test
	public void startEventTest() {
		String packageName = PACKAGE_BASE + "start.normal";
		process.setPackageName(packageName);
		process.add("NormalStart", ElementType.START_EVENT);
		new StartEvent("NormalStart").connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Normal" + BASE_FILE);
//		runNormal(packageName);
	}
	
	private void runNormal(String packageName){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(packageName + "." + PROCESS);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void conditionalStartEventTest() {
		String packageName = PACKAGE_BASE + "start.conditional";
		process.setPackageName(packageName);
		process.add("ConditionalStart", ElementType.CONDITIONAL_START_EVENT);
		ConditionalStartEvent start = new ConditionalStartEvent("ConditionalStart");
		start.setCondition("Rule", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Conditional" + BASE_FILE);
//		runConditional(packageName);
	}
	
	private void runConditional(String packageName){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(packageName + "." + PROCESS);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void timerStartEventTest() {
		String packageName = PACKAGE_BASE + "start.timer";
		process.setPackageName(packageName);
		process.add("TimerStart", ElementType.TIMER_START_EVENT);
		TimerStartEvent start = new TimerStartEvent("TimerStart");
		start.setTimer("5000ms");
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Timer" + BASE_FILE);
//		runTimer(packageName);
	}
	
	private void runTimer(String packageName){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(packageName + "." + PROCESS);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void messageStartEventTest() {
		String packageName = PACKAGE_BASE + "start.message";
		process.setPackageName(packageName);
		process.addMessage("HelloMessage", "String");
		process.add("MessageStart", ElementType.MESSAGE_START_EVENT);
		
		MessageStartEvent start = new MessageStartEvent("MessageStart");
		start.setMessageMapping(new Message("HelloMessage", "String"), STRING_VAR_ONE);
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Message" + BASE_FILE);
//		runMessage(packageName);
	}
	
	private void runMessage(String packageName){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(packageName + "." + PROCESS);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	@Test
	public void signalStartEventTest() {
		String packageName = PACKAGE_BASE + "start.signal";
		process.setPackageName(packageName);
		process.add("SignalStart", ElementType.SIGNAL_START_EVENT);
		SignalStartEvent start = new SignalStartEvent("SignalStart");
		start.setSignal(new Signal("BlockingSignal"));
		start.connectTo(new ScriptTask("Script"));
		
		saveAsAndValidate("Signal" + BASE_FILE);
//		runSignal(packageName);
	}
	
	private void runSignal(String packageName){
		KieSession kSession = createKieSession();
		ProcessInstance processInstance = kSession.startProcess(packageName + "." + PROCESS);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
