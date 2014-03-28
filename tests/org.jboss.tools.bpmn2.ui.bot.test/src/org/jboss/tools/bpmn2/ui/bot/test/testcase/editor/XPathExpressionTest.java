package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.junit.Ignore;
import org.junit.Test;

/**
 *     
 */
@ProcessDefinition(name="BPMN2-XPathExpression", project="EditorTestProject")
public class XPathExpressionTest extends JBPM6BaseTest {

	@Ignore(value = "XPath expression language is not supported.")
	@Test
	@Override
	public void executeTest() {
		super.executeTest();
	}
	
	@Override()
	public void buildProcessModel() {
		Process process = new Process("BPMN2-XPathExpression");
		process.addDataType("org.w3c.dom.Document");
		process.addLocalVariable("instanceMetadata", "org.w3c.dom.Document");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Split", ConstructType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway splitGw = new ExclusiveGateway("Split");
		splitGw.setDirection(Direction.DIVERGING);
		splitGw.append("Task1", ConstructType.SCRIPT_TASK, Position.NORTH_EAST);
		splitGw.append("Task2", ConstructType.SCRIPT_TASK, Position.SOUTH_EAST);
		/*
		 * There are several issues with the validator
		 * 1) Problem parsing the expression.
		 * 2) If mvel is not present NPE will be thrown.
		 */
		splitGw.setCondition("Split -> Task1", "", "count($instanceMetadata/instanceMetadata/user[@approved='true']) = 1");
		splitGw.setCondition("Split -> Task2", "", "count($instanceMetadata/instanceMetadata/user[@approved='false']) = 1");
		
		ScriptTask task1 = new ScriptTask("Task1");
		task1.setScript("Java", "System.out.println(\"Task 1\");");
		
		ScriptTask task2 = new ScriptTask("Task2");
		task2.setScript("Java", "System.out.println(\"Task 2\");");
		
		task1.append("Join", ConstructType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		ExclusiveGateway joinGw = new ExclusiveGateway("Join");
		joinGw.setDirection(Direction.CONVERGING);
		task2.connectTo(joinGw);
		
		joinGw.append("EndProcess", ConstructType.END_EVENT);
	}
	
}