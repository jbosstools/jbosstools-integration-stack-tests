package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.dataobjects.DataObject;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUE - Some users may use &quot; instead of ". The validator will not replace the entities.
 */
@ProcessDefinition(name="BPMN2-DataObject", project="EditorTestProject")
public class DataObjectTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Script", ElementType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Script");
		script.setScript("Java", "System.out.println(\"Processing evaluation for employee \" + employee);");
		script.append("EndProcess", ElementType.TERMINATE_END_EVENT);
		
		Process process = new Process("BPMN2-DataObject");
		process.add("employee", ElementType.DATA_OBJECT, start, Position.SOUTH);
		
		DataObject object = new DataObject("employee");
		object.connectTo(script, ConnectionType.DATA_ASSOCIATION);
		object.select();
		object.setDataType("String");
		
	}
	
}