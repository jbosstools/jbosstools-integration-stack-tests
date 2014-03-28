package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.Task;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.EventBasedGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 */
@ProcessDefinition(name="BPMN2-EventBasedSplit", project="EditorTestProject")
public class EventBasedSplitTest extends JBPM6BaseTest {

	/**
	 * ISSUES:
	 *   1) Fails probably due to BZ-XYZ (event based gateway does not display correctly)
	 */
	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-EventBasedSplit");
		process.addLocalVariable("x", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Email1", ConstructType.USER_TASK);
		
		UserTask task1 = new UserTask("Email1");
		task1.append("Split", ConstructType.EVENT_BASED_GATEWAY);
		
		EventBasedGateway gateway1 = new EventBasedGateway("Split");
		gateway1.setDirection(Direction.DIVERGING);
		gateway1.append("Event1", ConstructType.SIGNAL_INTERMEDIATE_CATCH_EVENT, Position.NORTH_EAST);
		gateway1.append("Event2", ConstructType.SIGNAL_INTERMEDIATE_CATCH_EVENT, Position.SOUTH_EAST);
		
		SignalIntermediateCatchEvent event1 = new SignalIntermediateCatchEvent("Event1");
		event1.setSignalMapping("Signal1", "BPMN2-EventBasedSplit/x");
//		event1.addParameterMapping(new OutputParameterMapping(new FromDataOutput("Event1"), new ToVariable("BPMN2-EventBasedSplit/x"), "Output Parameters"));
		event1.append("Script1", ConstructType.SCRIPT_TASK);
		
		ScriptTask script1 = new ScriptTask("Script1");
		script1.setScript("Java", "System.out.println(\"Executing Yes\");");
		
		SignalIntermediateCatchEvent event2 = new SignalIntermediateCatchEvent("Event2");
		event2.setSignalMapping("Signal2", "BPMN2-EventBasedSplit/x");
//		event2.addParameterMapping(new OutputParameterMapping(new FromDataOutput("Event2"), new ToVariable("BPMN2-EventBasedSplit/x"), "Output Parameters"));
		event2.append("Script2", ConstructType.SCRIPT_TASK);
		
		ScriptTask script2 = new ScriptTask("Script2");
		script2.setScript("Java", "System.out.println(\"Executing No\");");
		
		script1.append("Join", ConstructType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		ExclusiveGateway gateway2 = new ExclusiveGateway("Join");
		gateway2.setDirection(Direction.CONVERGING);
		script2.connectTo(gateway2);
		
		gateway2.append("Script", ConstructType.SCRIPT_TASK);
		
		ScriptTask script3 = new ScriptTask("Script");
		script3.setScript("Java", "System.out.println(\"x=\" + x);");
		script3.append("Email2", ConstructType.USER_TASK);
		
		Task task2 = new Task("Email2");
		task2.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}