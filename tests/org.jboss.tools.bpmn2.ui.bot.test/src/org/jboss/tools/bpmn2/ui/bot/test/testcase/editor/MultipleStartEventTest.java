package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.TimerStartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-MultipleStartEvent", project="EditorTestProject")
public class MultipleStartEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("StartTimer", ConstructType.TIMER_START_EVENT, Position.SOUTH);
		start.append("Split", ConstructType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);

		ExclusiveGateway gateway = new ExclusiveGateway("Split");
		gateway.setDirection(Direction.CONVERGING);
		gateway.append("User Task", ConstructType.USER_TASK);
		
		TimerStartEvent start2 = new TimerStartEvent("StartTimer");
		start2.setTimer("500ms");
		start2.connectTo(gateway);
		
		UserTask task = new UserTask("User Task");
		task.addActor("john");
		task.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}