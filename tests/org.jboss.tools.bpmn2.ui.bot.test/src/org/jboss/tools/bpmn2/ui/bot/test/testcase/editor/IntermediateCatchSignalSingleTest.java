package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-IntermediateCatchSignalSingle", project="EditorTestProject")
public class IntermediateCatchSignalSingleTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ConstructType.USER_TASK);
		
		UserTask userTask = new UserTask("User Task");
		userTask.addActor("", "Bruce");
		userTask.append("Catch", ConstructType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
		
		SignalIntermediateCatchEvent catchEvent = new SignalIntermediateCatchEvent("Catch");
		catchEvent.setSignal("Batman Signal");
		catchEvent.append("Script Task", ConstructType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.append("EndProcess", ConstructType.END_EVENT);
	}
	
}