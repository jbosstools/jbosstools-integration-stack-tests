package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-ConditionalStart", project="EditorTestProject")
public class ConditionalStartTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		new StartEvent("StartProcess").delete();
		new Process("BPMN2-ConditionalStart").add("StartProcess", ElementType.CONDITIONAL_START_EVENT);
		
		ConditionalStartEvent startEvent = new ConditionalStartEvent("StartProcess");
		startEvent.setCondition("", "org.jbpm.bpmn2.objects.Person(name == \"john\")"); // causes NPE
		startEvent.append("Hello", ElementType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Hello");
		scriptTask.setScript(ScriptLanguage.JAVA, "System.out.println(\"Hello World\");");
		scriptTask.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}