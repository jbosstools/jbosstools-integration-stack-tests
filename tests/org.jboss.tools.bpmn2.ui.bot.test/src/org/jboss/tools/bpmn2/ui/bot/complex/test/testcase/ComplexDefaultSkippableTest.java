package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertTrue;

import org.drools.core.process.instance.WorkItem;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-DefaultSkippable.bpmn2", saveAs = "BPMN2-DefaultSkippable.bpmn2")
public class ComplexDefaultSkippableTest extends JBPM6ComplexTest {

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		// Do some trivial change to save file was enabled
		new EndEvent("End Event 1").setName("EndProcess");
	}

	@TestPhase(phase = Phase.VALIDATE)
	public void validate() {
		UserTask userTask = new UserTask("User Task 1");
		assertTrue("Skippable should be checked to true by default", userTask.getSkippable());
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);

		ProcessInstance processInstance = kSession.startProcess("BPMN2DefaultSkippable");
		WorkItem item = (WorkItem) handler.getWorkItem("User Task 1");
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}

}
