package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.AdHocSubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-AdHocSubProcess",  project="EditorTestProject")
public class AdHocSubProcessTest extends JBPM6BaseTest {

	/**
	 * ISSUES: - May contain another bug. When adding a connection from an element
	 *           to itself then 'y' is missing in the 'di' element.
	 *         - See ContainerConstruct.add(String, ConstructType) 
	 *         
	 */
	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello", ConstructType.AD_HOC_SUB_PROCESS);

		AdHocSubProcess subprocess = new AdHocSubProcess("Hello");
		subprocess.append("Goodbye", ConstructType.SCRIPT_TASK);		

		ScriptTask task3 = new ScriptTask("Goodbye");
		task3.setScript("", "System.out.println(\"Goodbye World\");");
		task3.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
		
		/*
		 * Finish ad-hoc sub-process.
		 */
		subprocess.add("Hello1", ConstructType.SCRIPT_TASK);
		
		ScriptTask task1 = new ScriptTask("Hello1");
		task1.setScript("", "System.out.println(\"Hello World 1\");");
		
		subprocess.add("Hello2", ConstructType.SCRIPT_TASK, task1, Position.SOUTH);
		
		ScriptTask task2 = new ScriptTask("Hello2");
		task2.setScript("", "System.out.println(\"Hello World 2\");");
		task2.append("Hello", ConstructType.USER_TASK);
	}
	
}