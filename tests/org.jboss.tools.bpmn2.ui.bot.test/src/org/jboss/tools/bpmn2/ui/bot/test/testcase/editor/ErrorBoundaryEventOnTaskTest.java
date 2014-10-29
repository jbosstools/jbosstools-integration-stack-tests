package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ErrorBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.ErrorEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

/**
 * ISSUES - Engine does not validate the presence of the rules.
 */
@ProcessDefinition(name="BPMN2-ErrorBoundaryEventOnTask", project="EditorTestProject")
public class ErrorBoundaryEventOnTaskTest extends JBPM6BaseTest {

	private static final String VARIABLE = "localVar";
	
	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-ErrorBoundaryEventOnTask");
		process.addLocalVariable(VARIABLE, "String");
		process.addError("MyError", "java.lang.IllegalArgumentException", "java.lang.IllegalArgumentException");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Split", ElementType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway("Split");
		gateway.setDirection(Direction.DIVERGING);
		gateway.append("User Task", ElementType.USER_TASK, Position.NORTH_EAST);
		gateway.append("User task error attached", ElementType.USER_TASK, Position.SOUTH_EAST);
		
		UserTask task1 = new UserTask("User Task");
		task1.addActor("john");
		task1.append("Error end event", ElementType.ERROR_END_EVENT);
		
		ErrorEndEvent end1 = new ErrorEndEvent("Error end event");
		end1.setErrorEvent(new ErrorRef("MyError", "java.lang.IllegalArgumentException", "java.lang.IllegalArgumentException"), VARIABLE);
		
		UserTask task2 = new UserTask("User task error attached");
		task2.addActor("mary");
		task2.setOnExistScript("Java", "throw new java.lang.IllegalArgumentException(\"Exception for test purpose\");");
		task2.append("Error 1", ElementType.END_EVENT);
		task2.addEvent("Error Boundary Event", ElementType.ERROR_BOUNDARY_EVENT);
		
		ErrorBoundaryEvent boundaryEvent = new ErrorBoundaryEvent("Error Boundary Event");
		boundaryEvent.setErrorEvent(new ErrorRef("MyError", "java.lang.IllegalArgumentException", "java.lang.IllegalArgumentException"), VARIABLE);
		boundaryEvent.append("Script Task", ElementType.SCRIPT_TASK, Position.SOUTH);
		
		ScriptTask script = new ScriptTask("Script Task");
		script.setScript("Java", "System.out.println(\"Error handled\");");
		script.append("Error 2", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(
			Arrays.asList("StartProcess",
				"Split", "User Task", "Error end event", "User task error attached",
				"Error 2", "Script Task"), Arrays.asList("Error 1"));
		kSession.addEventListener(triggeredNodes);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(VARIABLE, "local variable value");
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2ErrorBoundaryEventOnTask", params);
		
		WorkItem workItem = handler.getWorkItem("User task error attached");
		handler.completeWorkItem(workItem,kSession.getWorkItemManager());
		workItem = handler.getWorkItem("User Task");
		handler.completeWorkItem(workItem,kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceAborted(processInstance, kSession);
	}
	
}