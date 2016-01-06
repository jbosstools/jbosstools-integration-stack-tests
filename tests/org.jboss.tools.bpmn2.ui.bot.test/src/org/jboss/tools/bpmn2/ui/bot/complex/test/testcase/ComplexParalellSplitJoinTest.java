package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.ParallelGatewaysListener;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

@JBPM6ComplexTestDefinition(projectName = "JBPM6ComplexTest", importFolder = "resources/bpmn2/model/base", openFile = "BaseBPMN2-ParalellSplitJoin.bpmn2", saveAs = "BPMN2-ParalellSplitJoin.bpmn2")
public class ComplexParalellSplitJoinTest extends JBPM6ComplexTest {

	private static final String END = "End";
	private static final String START = "Start";
	private static final String GATEWAY_CON = "Gateway2";
	private static final String GATEWAY_DIV = "Gateway1";
	private static final String UT_PM_EVALUATION = "PM Evaluation";
	private static final String UT_HR_EVALUATION = "HR Evaluation";
	private static final String UT_SELF_EVALUATION = "Self Evaluation";

	@TestPhase(phase = Phase.MODEL)
	public void model() {
		UserTask selfEvaluation = new UserTask("Self Evaluation");
		UserTask hrEvaluation = new UserTask("HR Evaluation");
		UserTask pmEvaluation = new UserTask("PM Evaluation");

		ParallelGateway gateway1 = (ParallelGateway) selfEvaluation.append("Gateway1", ElementType.PARALLEL_GATEWAY,
				ConnectionType.SEQUENCE_FLOW);
		gateway1.setDirection(Direction.DIVERGING);

		gateway1.connectTo(hrEvaluation);
		gateway1.connectTo(pmEvaluation);

		ParallelGateway gateway2 = (ParallelGateway) hrEvaluation.append("Gateway2", ElementType.PARALLEL_GATEWAY,
				ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		gateway2.setDirection(Direction.CONVERGING);
		gateway2.connectTo(new EndEvent("End"));

		pmEvaluation.connectTo(gateway2);
	}

	@TestPhase(phase = Phase.RUN)
	public void run(KieSession kSession) {
		PersistenceWorkItemHandler workItemHandler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);

		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(Arrays.asList(START, UT_PM_EVALUATION,
				UT_HR_EVALUATION, UT_SELF_EVALUATION, GATEWAY_CON, GATEWAY_DIV, END), null);
		ParallelGatewaysListener gatewaysEventListener = new ParallelGatewaysListener(GATEWAY_DIV, GATEWAY_CON);
		kSession.addEventListener(gatewaysEventListener);
		kSession.addEventListener(triggeredNodes);

		ProcessInstance processInstance = kSession.startProcess("Evaluation");
		JbpmAssertions.assertProcessInstanceActive(processInstance, kSession);

		assertTrue(workItemHandler.getWorkItems().size() == 1);

		WorkItem workItem = workItemHandler.getWorkItem(UT_SELF_EVALUATION);
		assertNotNull(workItem);
		workItemHandler.completeWorkItem(workItem, kSession.getWorkItemManager());

		assertTrue(workItemHandler.getWorkItems().size() == 2);

		workItemHandler.completeWorkItem(workItemHandler.getWorkItem(UT_HR_EVALUATION), kSession.getWorkItemManager());
		workItemHandler.completeWorkItem(workItemHandler.getWorkItem(UT_PM_EVALUATION), kSession.getWorkItemManager());

		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
