package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ErrorBoundaryEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-ErrorBoundaryEventOnTask.bpmn2", saveAs = "BPMN2-ErrorBoundaryEventOnTask.bpmn2", knownIssues={"1311135"})
public class ComplexErrorBoundaryEventOnTaskTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		UserTask task2 = new UserTask("User task error attached");

		ErrorBoundaryEvent boundaryEvent = (ErrorBoundaryEvent) task2.addEvent("Error Boundary Event",
				ElementType.ERROR_BOUNDARY_EVENT);
		boundaryEvent.setErrorEvent(
				new ErrorRef("MyError", "java.lang.IllegalArgumentException", "java.lang.IllegalArgumentException"),
				VARIABLE1);
		ScriptTask scriptTask = new ScriptTask("Script Task");
		boundaryEvent.connectTo(scriptTask);
		
 		scriptTask.addEvent("WrongEvent", ElementType.ERROR_BOUNDARY_EVENT);
		try {
			new ErrorBoundaryEvent("WrongEvent");
			fail("boundary events prohibitted on script tasks");
		} catch (RuntimeException e) {
			// ok
		}
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);

		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList("StartProcess", "Split",
				"User Task", "Error end event", "User task error attached", "Error 2", "Script Task"),
				Arrays.asList("Error 1"));
		kSession.addEventListener(triggeredNodes);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(VARIABLE1, "local variable value");

		ProcessInstance processInstance = kSession.startProcess("BPMN2ErrorBoundaryEventOnTask", params);

		WorkItem workItem = handler.getWorkItem("User task error attached");
		handler.completeWorkItem(workItem, kSession.getWorkItemManager());
		workItem = handler.getWorkItem("User Task");
		handler.completeWorkItem(workItem, kSession.getWorkItemManager());

		JbpmAssertions.assertProcessInstanceAborted(processInstance, kSession);
	}

}
