package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.definition.process.Connection;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.NodeInstance;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;

/**
 * ISSUES - Changing the BPMN2 runtime requires project close/open.
 */
//@ProcessRuntime()
@ProcessDefinition(name="Evaluation", project="EmployeeEvaluation")
public class ParallelSplitJoinTest extends JBPM6BaseTest {

	private static final String UT_PM_EVALUATION = "PM Evaluation";
	private static final String UT_HR_EVALUATION = "HR Evaluation";
	private static final String UT_SELF_EVALUATION = "Self Evaluation";

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.select();
		start.setName("Start");
		start.append(UT_SELF_EVALUATION, ElementType.USER_TASK);
		
		UserTask userTask1 = new UserTask(UT_SELF_EVALUATION);
		userTask1.addActor("employee"); 
		userTask1.append("Gateway1", ElementType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW);
		
		ParallelGateway gateway1 = new ParallelGateway("Gateway1");
		gateway1.setDirection(Direction.DIVERGING);
		
		gateway1.append(UT_HR_EVALUATION, ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway1.append(UT_PM_EVALUATION, ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);

		UserTask userTask2 = new UserTask(UT_HR_EVALUATION);
		userTask2.addActor("Mary");
		userTask2.append("Gateway2", ElementType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		
		ParallelGateway gateway2 = new ParallelGateway("Gateway2");
		gateway2.setDirection(Direction.CONVERGING);

		UserTask userTask3 = new UserTask(UT_PM_EVALUATION);
	    userTask3.addActor("John");
		userTask3.connectTo(gateway2);
		
		gateway2.append("End", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler workItemHandler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", workItemHandler);
	    
		GatewaysEventListener gatewaysEventListener = new GatewaysEventListener("Gateway1", "Gateway2"); 
		kSession.addEventListener(gatewaysEventListener);
		
		ProcessInstance processInstance = kSession.startProcess("Evaluation");
		gatewaysEventListener.setProcessInstance(processInstance);
	    jbpmAsserter.assertProcessInstanceActive(processInstance.getId(), kSession);

		assertTrue(workItemHandler.getWorkItems().size() == 1);

		WorkItem workItem = workItemHandler.getWorkItem(UT_SELF_EVALUATION);
		assertNotNull(workItem);
		workItemHandler.completeWorkItem(workItem,kSession.getWorkItemManager());
		
		assertTrue(workItemHandler.getWorkItems().size() == 2);
		
		workItemHandler.completeWorkItem(workItemHandler.getWorkItem(UT_HR_EVALUATION),kSession.getWorkItemManager());
		workItemHandler.completeWorkItem(workItemHandler.getWorkItem(UT_PM_EVALUATION),kSession.getWorkItemManager());
		
		jbpmAsserter.assertProcessInstanceCompleted(processInstance.getId(), kSession);
	}
	
	private class GatewaysEventListener implements ProcessEventListener {

		private String diverging;
		private String converging;
		
		private NodeInstance divergingGateway;
		private NodeInstance convergingGateway;
		
		private ProcessInstance processInstance;
		
		public GatewaysEventListener(String diverging, String converging) {
			this.diverging = diverging;
			this.converging = converging;
			divergingGateway = null;
			convergingGateway = null;
		}
		
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterProcessCompleted(ProcessCompletedEvent event) {
			jbpmAsserter.assertNodeExists(processInstance, "Gateway1");
			jbpmAsserter.assertNodeExists(processInstance, "Gateway2");
			jbpmAsserter.assertNumOfIncommingConnections(processInstance, "Gateway1", 1);
			jbpmAsserter.assertNumOfOutgoingConnections(processInstance, "Gateway1", 2);
			jbpmAsserter.assertNumOfIncommingConnections(processInstance, "Gateway2", 2);
			jbpmAsserter.assertNumOfOutgoingConnections(processInstance, "Gateway2", 1);
		}

		@Override
		public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
			NodeInstance node = event.getNodeInstance();
			String nodeName = node.getNodeName();
			
			if(nodeName.compareTo(diverging) == 0) {
				assertNull(convergingGateway);
				divergingGateway = node;
			}
			
			if(nodeName.compareTo(converging) == 0) {
				assertNotNull(divergingGateway);
				convergingGateway = node;
			}
			
		}

		@Override
		public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNodeLeft(ProcessNodeLeftEvent event) {
			// TODO Auto-generated method stub
		}

		@Override
		public void afterNodeLeft(ProcessNodeLeftEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeVariableChanged(ProcessVariableChangedEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterVariableChanged(ProcessVariableChangedEvent event) {
			// TODO Auto-generated method stub
			
		}
		
		public void setProcessInstance(ProcessInstance instance) {
			processInstance = instance;
		}
	
	}
}