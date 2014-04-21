package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConnectionType;
import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 */
@ProcessDefinition(name="BPMN2-Association", project="EditorTestProject")
public class AssociationTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Log", ConstructType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Log");
		script.setScript("", "System.out.println(\"Just outputting something\");");
		
		start.connectTo(script, ConnectionType.ASSOCIATION_UNDIRECTED);
		
		script.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}