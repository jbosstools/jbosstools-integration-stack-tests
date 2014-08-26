package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromExpression;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-ExclusiveSplitPriority", project="EditorTestProject")
public class ExclusiveSplitPriorityTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-ExclusiveSplitPriority");
		process.addLocalVariable("x", "String");
		process.addLocalVariable("y", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Split", ElementType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway gw = new ExclusiveGateway("Split");
		gw.setDirection(Direction.DIVERGING);
		gw.append("Script1", ElementType.SCRIPT_TASK, Position.NORTH_EAST);
		gw.append("Script2", ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		gw.select();
		gw.setCondition("Split -> Script1", "Java", "return x!=null;");
		gw.setCondition("Split -> Script2", "Java", "return x==null;");
		gw.setPriority("Split -> Script2", "1");
		gw.setPriority("Split -> Script1", "2");
		
		ScriptTask task1 = new ScriptTask("Script1");
		task1.setScript("Java", "System.out.println(\"x=\" + x);");
		task1.append("Join", ElementType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		
		ExclusiveGateway gw2 = new ExclusiveGateway("Join");
		gw2.setDirection(Direction.CONVERGING);
		gw2.append("Email", ElementType.USER_TASK);
		
		ScriptTask task2 = new ScriptTask("Script2");
		task2.setScript("Java", "System.out.println(\"y=\" + y);");
		task2.connectTo(gw2);
		
		// TBD: switch to sendTask
		UserTask task = new UserTask("Email");
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "This is an urgent email #{x}"), new ToDataInput("Body", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "Urgent email !"), new ToDataInput("Subject", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "you@mail.com"), new ToDataInput("To", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "ne@mail.com"), new ToDataInput("From", "String"), ParameterMapping.Type.INPUT));
		task.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}