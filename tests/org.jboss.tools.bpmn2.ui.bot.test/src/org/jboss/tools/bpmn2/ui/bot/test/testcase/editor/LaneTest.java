package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.reddeer.DefaultOutlineView;

/**
 *     
 */
@ProcessDefinition(name="BPMN2-Lane", project="EditorTestProject")
public class LaneTest extends JBPM6BaseTest {

	DefaultOutlineView outlineView = new DefaultOutlineView();
	
	/*
	 * ISSUE:
	 * 	Task2 has two outgoing connections and End has two incoming. But why? Unable to
	 * 	reproduce by hand.
	 */
	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("MyLane", ConstructType.LANE, Position.SOUTH_EAST);
		
		Lane lane = new Lane("MyLane");
		lane.append("EndProcess", ConstructType.TERMINATE_END_EVENT, Position.SOUTH_EAST);
		lane.add("Hello", ConstructType.SCRIPT_TASK, new Point(lane.getBounds().width / 6, lane.getBounds().height / 2));

		ScriptTask task = new ScriptTask("Hello");
		task.append("Goodbye", ConstructType.SCRIPT_TASK);

		ScriptTask task2 = new ScriptTask("Goodbye");
		
		EndEvent end = new TerminateEndEvent("EndProcess");
		
		outlineView.select("StartProcess");
		start.connectTo(task);
		outlineView.select("EndProcess");
		task2.connectTo(end);
	}
	
}