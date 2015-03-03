package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-Import", project="EditorTestProject")
public class ImportTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-Import");
		process.addImport("java.util.List");
		process.addImport("java.util.ArrayList");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello", ElementType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Hello");
		script.setScript(ScriptLanguage.JAVA, "List l = new ArrayList(); System.out.println(l);");
		script.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}