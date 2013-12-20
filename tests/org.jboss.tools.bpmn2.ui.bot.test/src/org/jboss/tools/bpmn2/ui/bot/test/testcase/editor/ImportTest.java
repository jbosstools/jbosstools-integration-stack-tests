package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-Import", project="EditorTestProject")
public class ImportTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		BPMN2Process process = new BPMN2Process("BPMN2-Import");
//		process.addImport("java.util.List");
		process.addImport("java.util.ArrayList");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello", ConstructType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Hello");
		script.setScript("Java", "ArrayList l = new ArrayList(); System.out.println(l);");
		script.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}