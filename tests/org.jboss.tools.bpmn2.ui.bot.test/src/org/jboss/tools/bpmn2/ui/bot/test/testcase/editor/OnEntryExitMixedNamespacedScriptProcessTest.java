package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.Task;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-OnEntryExitMixedNamespacedScriptProcess", project="EditorTestProject")
public class OnEntryExitMixedNamespacedScriptProcessTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-OnEntryExitMixedNamespacedScriptProcess");
		process.addImport("java.util.List");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Task", ElementType.TASK);
		
		Task script = new Task("Task");
		// Add on-entry script1 list.add("On-entry1");
		// Add on-entry script2 list.add("On-entry2");
		// Add on-exit script1 list.add("On-exit");
		// Add on-exit script2 list.add("On-exit2");
 		script.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}