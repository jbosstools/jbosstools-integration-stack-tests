package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-BooleanStructureRef.bpmn2", saveAs = "BPMN2-BooleanStructureRef.bpmn2")
public class ComplexBooleanStructureReferenceTest extends JBPM6ComplexTest {

	private static final String NODE_USER_TASK = "User Task";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		UserTask userTask = new UserTask(NODE_USER_TASK);
		userTask.addParameterMapping(new ParameterMapping(new FromDataOutput("testHT", "String"),
				new ToVariable(VARIABLE1), ParameterMapping.Type.OUTPUT));
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(
				Arrays.asList("StartProcess", NODE_USER_TASK, "Script", "EndProcess"), null);
		kSession.addEventListener(triggeredNodes);

		Map<String, Object> userNodeResults = new HashMap<String, Object>();
		userNodeResults.put("testHT", true);

		PersistenceWorkItemHandler workItemHandler = new PersistenceWorkItemHandler();
		workItemHandler.setResult(NODE_USER_TASK, userNodeResults);
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);

		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put(VARIABLE1, false);

		BeforeEndEventListener listener = new BeforeEndEventListener();
		kSession.addEventListener(listener);
		ProcessInstance processInstance = kSession.startProcess("BPMN2BooleanStructureRef", arguments);

		WorkItem workItem = workItemHandler.getWorkItem(NODE_USER_TASK);
		workItemHandler.completeWorkItem(workItem, kSession.getWorkItemManager());

		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

	private class BeforeEndEventListener extends DefaultProcessEventListener {

		@Override
		public void beforeProcessCompleted(ProcessCompletedEvent event) {
			assertEquals("Process variable " + VARIABLE1 + " didn't changed to true.", true,
					((WorkflowProcessInstance) event.getProcessInstance()).getVariable(VARIABLE1));
		}

	}
}
