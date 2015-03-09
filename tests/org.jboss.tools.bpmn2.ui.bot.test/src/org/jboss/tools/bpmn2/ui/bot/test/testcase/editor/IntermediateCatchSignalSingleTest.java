package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.catchevents.SignalIntermediateCatchEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-IntermediateCatchSignalSingle", project="EditorTestProject")
public class IntermediateCatchSignalSingleTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateCatchSignalSingle");
		process.addLocalVariable("s", "String");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ElementType.USER_TASK);
		
		UserTask userTask = new UserTask("User Task");
		userTask.addActor("Bruce");
		userTask.setTaskName("TaskForBruce");
		userTask.append("Catch", ElementType.SIGNAL_INTERMEDIATE_CATCH_EVENT);
		
		SignalIntermediateCatchEvent catchEvent = new SignalIntermediateCatchEvent("Catch");
		catchEvent.setSignalMapping("BatmanSignal", "s");
		catchEvent.append("Script Task", ElementType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.setScript(ScriptLanguage.JAVA, "System.out.println(s);");
		scriptTask.append("EndProcess", ElementType.END_EVENT);
	}
	
}