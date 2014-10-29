package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@ProcessDefinition(name="BPMN2-BooleanStructureRef", project="EditorTestProject")
public class BooleanStructureReferenceTest extends JBPM6BaseTest {

	private static final String NODE_USER_TASK = "User Task";

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-BooleanStructureRef");
		process.addLocalVariable("test", "Boolean");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append(NODE_USER_TASK, ElementType.USER_TASK);

		UserTask userTask = new UserTask(NODE_USER_TASK);
		userTask.addActor("john");
		userTask.setTaskName("UserTask");
//		userTask.addLocalVariable("testHT", "String"); // @BZ
		userTask.addParameterMapping(new ParameterMapping(new FromDataOutput("testHT", "String"), new ToVariable("test"), ParameterMapping.Type.OUTPUT));
		userTask.append("Script", ElementType.SCRIPT_TASK);

		ScriptTask scriptTask = new ScriptTask("Script");
		scriptTask.setScript("", "System.out.println(\"Result \" + test)");
		scriptTask.append("EndProcess", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList("StartProcess" ,
				NODE_USER_TASK, "Script", "EndProcess"), null);
		kSession.addEventListener(triggeredNodes);
	    
		
		Map<String, Object> userNodeResults = new HashMap<String, Object>();
		userNodeResults.put("testHT", true);
		
		PersistenceWorkItemHandler workItemHandler = new PersistenceWorkItemHandler();
		workItemHandler.setResult(NODE_USER_TASK, userNodeResults);
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
		
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("test", false);
		
		BeforeEndEventListener listener = new BeforeEndEventListener();
		kSession.addEventListener(listener);
		ProcessInstance processInstance = kSession.startProcess("BPMN2BooleanStructureRef", arguments);
		
		WorkItem workItem = workItemHandler.getWorkItem(NODE_USER_TASK);
		workItemHandler.completeWorkItem(workItem,kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
	private class BeforeEndEventListener implements ProcessEventListener {
		
		@Override
		public void beforeProcessStarted(ProcessStartedEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterProcessStarted(ProcessStartedEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeProcessCompleted(ProcessCompletedEvent event) {
			assertEquals("Process variable 'test' didn't changed to true.", true, ((WorkflowProcessInstance) event.getProcessInstance()).getVariable("test"));
		}

		@Override
		public void afterProcessCompleted(ProcessCompletedEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		
		}

		@Override
		public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNodeLeft(ProcessNodeLeftEvent event) {
			
		}

		@Override
		public void afterNodeLeft(ProcessNodeLeftEvent event) {
			
		}

		@Override
		public void beforeVariableChanged(ProcessVariableChangedEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterVariableChanged(ProcessVariableChangedEvent event) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}