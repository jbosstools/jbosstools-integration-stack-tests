package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.drools.core.process.instance.WorkItem;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-WebDesignerProcess.bpmn2", saveAs = "BPMN2-WebDesignerProcess.bpmn2")
public class ComplexWebDesignerProcessTest extends JBPM6ComplexTest {

	private static final String SELF_EVALUATION = "Self Evaluation";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		new StartEvent(" ").setName("StartProcess");
		UserTask userTask = new UserTask(SELF_EVALUATION);
		userTask.setSkippable(true);
		userTask.setLocale("sk-SK");
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("employee", "john");

		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession
				.startProcess("BPMN2WebDesignerProcess", args);
		WorkItem item = (WorkItem) handler.getWorkItem(SELF_EVALUATION);
		assertEquals("This user task should be assigned to john", "john", (String) item.getParameter("ActorId"));
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("performance", item.getParameter("reason"));
		handler.setResult("Self Evaluation", results);
		handler.completeWorkItem(item, kSession.getWorkItemManager());

		assertEquals(processInstance.getVariable("reason"), processInstance.getVariable("performance"));

		item = (WorkItem) handler.getWorkItem("HR Evaluation");
		handler.completeWorkItem(item, kSession.getWorkItemManager());

		item = (WorkItem) handler.getWorkItem("PM Evaluation");
		handler.completeWorkItem(item, kSession.getWorkItemManager());

		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

}
