package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-ConditionalStart", project="EditorTestProject")
public class ConditionalStartTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		new StartEvent("StartProcess").delete();
		new BPMN2Process("BPMN2-ConditionalStart").add("StartProcess", ConstructType.CONDITIONAL_START_EVENT);
		
		ConditionalStartEvent startEvent = new ConditionalStartEvent("StartProcess");
		startEvent.setCondition("", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		startEvent.append("Hello", ConstructType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Hello");
		scriptTask.setScript("", "System.out.println(\"Hello World\");");
		scriptTask.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}