package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.ParallelGatewaysListener;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

/**
 * ISSUES - Changing the BPMN2 runtime requires project close/open.
 */
//@ProcessRuntime()
@ProcessDefinition(name="Evaluation", project="EmployeeEvaluation")
public class ParallelSplitJoinTest extends JBPM6BaseTest {

	private static final String END = "End";
	private static final String START = "Start";
	private static final String GATEWAY_CON = "Gateway2";
	private static final String GATEWAY_DIV = "Gateway1";
	private static final String UT_PM_EVALUATION = "PM Evaluation";
	private static final String UT_HR_EVALUATION = "HR Evaluation";
	private static final String UT_SELF_EVALUATION = "Self Evaluation";

	@Override
	public void buildProcessModel() {
		
		StartEvent start = new StartEvent("StartProcess");
		start.select();
		start.setName(START);
		start.append(UT_SELF_EVALUATION, ElementType.USER_TASK);
		
		UserTask userTask1 = new UserTask(UT_SELF_EVALUATION);
		userTask1.addActor("employee"); 
		userTask1.append(GATEWAY_DIV, ElementType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW);
		
		ParallelGateway gateway1 = new ParallelGateway(GATEWAY_DIV);
		gateway1.setDirection(Direction.DIVERGING);
		
		gateway1.append(UT_HR_EVALUATION, ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway1.append(UT_PM_EVALUATION, ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);

		UserTask userTask2 = new UserTask(UT_HR_EVALUATION);
		userTask2.addActor("Mary");
		userTask2.append(GATEWAY_CON, ElementType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		
		ParallelGateway gateway2 = new ParallelGateway(GATEWAY_CON);
		gateway2.setDirection(Direction.CONVERGING);

		UserTask userTask3 = new UserTask(UT_PM_EVALUATION);
	    userTask3.addActor("John");
		userTask3.connectTo(gateway2);
		
		gateway2.append(END, ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler workItemHandler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
	    
		TriggeredNodesListener triggeredNodes = new TriggeredNodesListener(
			Arrays.asList(START, UT_PM_EVALUATION, UT_HR_EVALUATION, UT_SELF_EVALUATION, 
			GATEWAY_CON, GATEWAY_DIV, END), null);
		ParallelGatewaysListener gatewaysEventListener = new ParallelGatewaysListener(GATEWAY_DIV, GATEWAY_CON); 
		kSession.addEventListener(gatewaysEventListener);
		kSession.addEventListener(triggeredNodes);
		
		ProcessInstance processInstance = kSession.startProcess("Evaluation");
		JbpmAssertions.assertProcessInstanceActive(processInstance, kSession);

		assertTrue(workItemHandler.getWorkItems().size() == 1);

		WorkItem workItem = workItemHandler.getWorkItem(UT_SELF_EVALUATION);
		assertNotNull(workItem);
		workItemHandler.completeWorkItem(workItem,kSession.getWorkItemManager());
		
		assertTrue(workItemHandler.getWorkItems().size() == 2);
		
		workItemHandler.completeWorkItem(workItemHandler.getWorkItem(UT_HR_EVALUATION),kSession.getWorkItemManager());
		workItemHandler.completeWorkItem(workItemHandler.getWorkItem(UT_PM_EVALUATION),kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}