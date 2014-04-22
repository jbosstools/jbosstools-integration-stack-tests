package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUES - Changing the BPMN2 runtime requires project close/open.
 */
//@ProcessRuntime()
@ProcessDefinition(name="Evaluation", project="EmployeeEvaluation")
public class ParallelSplitJoinTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.select();
		start.setName("Start");
		start.append("Self Evaluation", ElementType.USER_TASK);
		
		UserTask userTask1 = new UserTask("Self Evaluation");
		userTask1.addActor("employee"); 
		userTask1.append("Gateway1", ElementType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW);
		
		ParallelGateway gateway1 = new ParallelGateway("Gateway1");
		gateway1.setDirection(Direction.DIVERGING);
		
		gateway1.append("HR Evaluation", ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway1.append("PM Evaluation", ElementType.USER_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);

		UserTask userTask2 = new UserTask("HR Evaluation");
		userTask2.addActor("Mary");
		userTask2.append("Gateway2", ElementType.PARALLEL_GATEWAY, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		
		ParallelGateway gateway2 = new ParallelGateway("Gateway2");
		gateway2.setDirection(Direction.CONVERGING);

		UserTask userTask3 = new UserTask("PM Evaluation");
	    userTask3.addActor("John");
		userTask3.connectTo(gateway2);
		
		gateway2.append("End", ElementType.END_EVENT);
	}
	
}