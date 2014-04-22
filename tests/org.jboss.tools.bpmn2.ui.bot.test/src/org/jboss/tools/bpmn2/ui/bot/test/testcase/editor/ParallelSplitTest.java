package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-ParallelSplit", project="EditorTestProject")
public class ParallelSplitTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("ParallelSplit", ElementType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway("ParallelSplit");
		gateway.setDirection(Direction.DIVERGING);
		gateway.append("Script1", ElementType.SCRIPT_TASK, Position.NORTH_EAST);
		gateway.append("Script2", ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		ScriptTask script1 = new ScriptTask("Script1");
		script1.setScript("Java", "System.out.println(\"1\");");
		script1.append("End1", ElementType.END_EVENT);
		
		// Fails on setScript and I don't know why!
		ScriptTask script2 = new ScriptTask("Script2");
		script2.select();
		script2.setScript("Java", "System.out.println(\"2\");");
		script2.append("End2", ElementType.END_EVENT);
	}
	
}