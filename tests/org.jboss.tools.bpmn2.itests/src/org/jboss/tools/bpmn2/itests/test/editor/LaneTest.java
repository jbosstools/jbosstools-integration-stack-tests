package org.jboss.tools.bpmn2.itests.test.editor;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.Task;
import org.jboss.tools.bpmn2.itests.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-Lane", project="EditorTestProject")
public class LaneTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
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