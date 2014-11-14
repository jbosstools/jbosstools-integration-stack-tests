package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.EventBasedGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
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

/**
 * ISSUES - Fails probably due to BZ-XYZ (event based gateway does not display correctly)
 */
@ProcessDefinition(name="BPMN2-EventBasedSplit", project="EditorTestProject")
public class EventBasedSplitTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-EventBasedSplit");
		process.addLocalVariable(VARIABLE1, "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Email1", ElementType.USER_TASK);
		
		UserTask task1 = new UserTask("Email1");
		task1.append("Split", ElementType.EVENT_BASED_GATEWAY);
		
		EventBasedGateway gateway1 = new EventBasedGateway("Split");
		gateway1.setDirection(Direction.DIVERGING);
		gateway1.append("Event1", ElementType.SIGNAL_INTERMEDIATE_CATCH_EVENT, Position.NORTH_EAST);
		gateway1.append("Event2", ElementType.SIGNAL_INTERMEDIATE_CATCH_EVENT, Position.SOUTH_EAST);
		
		SignalIntermediateCatchEvent event1 = new SignalIntermediateCatchEvent("Event1");
		event1.setSignalMapping("Signal1", VARIABLE1);
//		event1.addParameterMapping(new OutputParameterMapping(new FromDataOutput("Event1"), new ToVariable("BPMN2-EventBasedSplit/x"), "Output Parameters"));
		event1.append("Script1", ElementType.SCRIPT_TASK);
		
		ScriptTask script1 = new ScriptTask("Script1");
		script1.setScript("Java", "System.out.println(\"Executing Yes\");");
		
		SignalIntermediateCatchEvent event2 = new SignalIntermediateCatchEvent("Event2");
		event2.setSignalMapping("Signal2", VARIABLE1);
//		event2.addParameterMapping(new OutputParameterMapping(new FromDataOutput("Event2"), new ToVariable("BPMN2-EventBasedSplit/x"), "Output Parameters"));
		event2.append("Script2", ElementType.SCRIPT_TASK);
		
		ScriptTask script2 = new ScriptTask("Script2");
		script2.setScript("Java", "System.out.println(\"Executing No\");");
		
		script1.append("Join", ElementType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		ExclusiveGateway gateway2 = new ExclusiveGateway("Join");
		gateway2.setDirection(Direction.CONVERGING);
		script2.connectTo(gateway2);
		
		gateway2.append("Script", ElementType.SCRIPT_TASK);
		
		ScriptTask script3 = new ScriptTask("Script");
		script3.setScript("Java", "System.out.println(" + VARIABLE1 + ");");
		script3.append("Email2", ElementType.USER_TASK);
		
		UserTask task2 = new UserTask("Email2");
		task2.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "Email1", "Split", "Script1", "Join", "Script", "Email2", "EndProcess"), Arrays.asList("Script2"));
		kSession.addEventListener(triggered);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2EventBasedSplit");
		
		WorkItem item = handler.getWorkItem("Email1");
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		kSession.signalEvent("Signal1", "Signal1 sended");
		item = handler.getWorkItem("Email2");
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		assertEquals("Process variable "+VARIABLE1+" didn't changed.", "Signal1 sended", ((WorkflowProcessInstance) processInstance).getVariable(VARIABLE1));
	}
	
}