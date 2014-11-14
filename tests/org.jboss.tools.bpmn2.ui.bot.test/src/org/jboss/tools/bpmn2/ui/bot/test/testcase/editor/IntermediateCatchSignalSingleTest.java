package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@ProcessDefinition(name="BPMN2-IntermediateCatchSignalSingle", project="EditorTestProject")
public class IntermediateCatchSignalSingleTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateCatchSignalSingle");
		process.addLocalVariable(VARIABLE1, "String");
		process.addSignal("BatmanSignal");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ElementType.USER_TASK);
		
		UserTask userTask = new UserTask("User Task");
		userTask.addActor("Bruce");
		userTask.append("Catch", ElementType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
		
		SignalIntermediateCatchEvent catchEvent = new SignalIntermediateCatchEvent("Catch");
		catchEvent.setSignalMapping("BatmanSignal", VARIABLE1);
		catchEvent.append("Script Task", ElementType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.setScript("Java", "System.out.println("+ VARIABLE1 +");");
		scriptTask.append("EndProcess", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
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