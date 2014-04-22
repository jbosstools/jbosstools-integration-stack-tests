package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.InclusiveGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-InclusiveSplit", project="EditorTestProject")
public class InclusiveSplitTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-InclusiveSplit");
		process.addDataType("Integer");
		process.addLocalVariable("x", "Integer");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Gateway", ElementType.INCLUSIVE_GATEWAY);
		
		InclusiveGateway gateway = new InclusiveGateway("Gateway");
		gateway.append("Script1", ElementType.SCRIPT_TASK, Position.NORTH);
		gateway.append("Script2", ElementType.SCRIPT_TASK);
		gateway.append("Script3", ElementType.SCRIPT_TASK, Position.SOUTH);
		
		gateway.select();
		gateway.setCondition("Gateway -> Script1", "java", "return x > 0;");
		gateway.setCondition("Gateway -> Script2", "java", "return x > 10;");
		gateway.setCondition("Gateway -> Script3", "java", "return x > 20;");

		ScriptTask script1 = new ScriptTask("Script1");
		script1.setScript("Java", "System.out.println(\"path1\");");
		script1.append("EndProcess1", ElementType.END_EVENT);
		
		ScriptTask script2 = new ScriptTask("Script2");
		script2.setScript("Java", "System.out.println(\"path2\");");
		script2.append("EndProcess2", ElementType.END_EVENT);

		ScriptTask script3 = new ScriptTask("Script3");
		script3.setScript("Java", "System.out.println(\"path3\");");
		script3.append("EndProcess3", ElementType.END_EVENT);
	}
	
}