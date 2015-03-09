package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Message;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.MessageStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-MessageStart", project="EditorTestProject")
public class MessageStartTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-MessageStart");
		process.addLocalVariable("x", "String");
		
		Message msg = new Message("_2_Message", "String");
		process.addMessage(msg);
		
		new StartEvent("StartProcess").delete();
		process.add("StartProcess", ElementType.MESSAGE_START_EVENT);
		
		MessageStartEvent start = new MessageStartEvent("StartProcess");
		start.setMessageMapping(msg, "x");
		start.append("Script", ElementType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Script");
		script.setScript(ScriptLanguage.JAVA, "System.out.println(\"x = \" + x);");
		script.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}