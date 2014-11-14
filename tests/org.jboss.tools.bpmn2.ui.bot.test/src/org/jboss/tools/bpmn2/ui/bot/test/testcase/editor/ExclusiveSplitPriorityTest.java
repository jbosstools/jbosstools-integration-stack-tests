package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.PersistenceWorkItemHandler;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItem;
import org.mvel2.util.Varargs;

@ProcessDefinition(name="BPMN2-ExclusiveSplitPriority", project="EditorTestProject")
public class ExclusiveSplitPriorityTest extends JBPM6BaseTest {
	
	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-ExclusiveSplitPriority");
		process.addLocalVariable(VARIABLE1, "String");
		process.addLocalVariable(VARIABLE2, "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Split", ElementType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway gw = new ExclusiveGateway("Split");
		gw.setDirection(Direction.DIVERGING);
		gw.append("Script1", ElementType.SCRIPT_TASK, Position.NORTH_EAST);
		gw.append("Script2", ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		gw.select();
		gw.setCondition("Split -> Script1", "Java", "return " + VARIABLE1 + " != null;");
		gw.setCondition("Split -> Script2", "Java", "return " + VARIABLE1 + " == null;");
		gw.setPriority("Split -> Script2", "1");
		gw.setPriority("Split -> Script1", "2");
		
		ScriptTask task1 = new ScriptTask("Script1");
		task1.setScript("Java", "System.out.println("+ VARIABLE1 + ");");
		task1.append("Join", ElementType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		
		ExclusiveGateway gw2 = new ExclusiveGateway("Join");
		gw2.setDirection(Direction.CONVERGING);
		gw2.append("Email", ElementType.USER_TASK);
		
		ScriptTask task2 = new ScriptTask("Script2");
		task2.setScript("Java", "System.out.println(" + VARIABLE2 + ");");
		task2.connectTo(gw2);
		
		// TBD: switch to sendTask
		UserTask task = new UserTask("Email");
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "This is an urgent email #{x}"), new ToDataInput("Body", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "Urgent email !"), new ToDataInput("Subject", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "you@mail.com"), new ToDataInput("To", "String"), ParameterMapping.Type.INPUT));
		task.addParameterMapping(new ParameterMapping(new FromExpression("mvel", "ne@mail.com"), new ToDataInput("From", "String"), ParameterMapping.Type.INPUT));
		task.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		PersistenceWorkItemHandler handler = new PersistenceWorkItemHandler();
		kSession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);
		
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList("StartProcess", "Split", "Script1", "Email", "EndProcess"), Arrays.asList("Script2"));
		kSession.addEventListener(triggered);
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put(VARIABLE1, "nonNullValue");
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2ExclusiveSplitPriority", args);
		
		WorkItem item = handler.getWorkItem("Email");
		handler.completeWorkItem(item, kSession.getWorkItemManager());
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
}