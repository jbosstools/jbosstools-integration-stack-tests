package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.Task;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-Lane", project="EditorTestProject")
public class LaneTest extends JBPM6BaseTest {

	/*
	 * ISSUE:
	 * 	Task2 has two outgoing connections and End has two incoming. But why? Unable to
	 * 	reproduce by hand.
	 */
	@Override
	public void buildProcessModel() {
//		StartEvent start = new StartEvent("StartProcess");
//		start.append("MyLane", ConstructType.LANE, Position.SOUTH_EAST);
//		
//		Lane lane = new Lane("MyLane");
//		lane.append("EndProcess", ConstructType.TERMINATE_END_EVENT, Position.SOUTH_EAST);
//		lane.add("Hello", ConstructType.TASK, new Point(lane.getBounds().x() + lane.getBounds().width / 6, lane.getBounds().y() + lane.getBounds().height / 2));
//
//		Task task = new Task("Hello");
//		task.append("Goodbye", ConstructType.TASK);
//		
//		Task task2 = new Task("Goodbye");
//		
//		EndEvent end = new TerminateEndEvent("EndProcess");
//		
//		start.connectTo(task);
//		task2.connectTo(end);
		
		/*
		 * The code above will not pass because "Start" is no in the visible view port when the
		 * connect method is called. A scrolling mechanism would solve the issue.
		 */
		StartEvent start = new StartEvent("StartProcess");
		start.append("MyLane", ConstructType.LANE, Position.SOUTH_EAST);
		Lane lane = new Lane("MyLane");
		lane.add("Hello", ConstructType.TASK, new Point(lane.getBounds().x() + lane.getBounds().width / 6, lane.getBounds().y() + lane.getBounds().height / 2));
		Task task = new Task("Hello");
		start.connectTo(task);
		task.append("Goodbye", ConstructType.TASK);
		Task task2 = new Task("Goodbye");
		lane.append("EndProcess", ConstructType.TERMINATE_END_EVENT, Position.SOUTH_EAST);		
		EndEvent end = new TerminateEndEvent("EndProcess");
		task2.connectTo(end);
	}
	
}