package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.reddeer.DefaultOutlineView;

/**
 * ISSUE - Task2 has two outgoing connections and End has two incoming. But why? Unable to
 * 	       reproduce by hand.
 */
@ProcessDefinition(name="BPMN2-Lane", project="EditorTestProject")
public class LaneTest extends JBPM6BaseTest {

	DefaultOutlineView outlineView = new DefaultOutlineView();
	
	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("MyLane", ElementType.LANE, Position.SOUTH_EAST);
		start.delete();
		
		Lane lane = new Lane("MyLane");
		lane.add("LaneStart", ElementType.START_EVENT, new Point(lane.getBounds().width / 6, lane.getBounds().height / 2));
		new StartEvent("LaneStart").append("Hello", ElementType.SCRIPT_TASK);
		
		ScriptTask task = new ScriptTask("Hello");
		task.setScript(ScriptLanguage.JAVA, "System.out.println(\"First task in lane\");");
		task.append("Goodbye", ElementType.SCRIPT_TASK);

		ScriptTask task2 = new ScriptTask("Goodbye");
		task2.setScript(ScriptLanguage.JAVA, "System.out.println(\"Second task in lane\");");
		
		task2.append("End", ElementType.END_EVENT);
	}
	
}