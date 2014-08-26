package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-SubProcess", project="EditorTestProject")
public class SubProcessTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello Subprocess", ElementType.SUB_PROCESS, Position.SOUTH);

		SubProcess subProcess = new SubProcess("Hello Subprocess");
		subProcess.addLocalVariable("x", "String");
		subProcess.append("Goodbye", ElementType.SCRIPT_TASK);

		ScriptTask script4 = new ScriptTask("Goodbye");
		script4.setScript("Java", "System.out.println(\"Goodbye World\");");
		script4.append("EndProcess", ElementType.TERMINATE_END_EVENT);
		
		// Now create the inner of the sub process.
		subProcess.addRelativeToElement("StartSubProcess", ElementType.START_EVENT, subProcess, new Point(-70, -35));
		
		StartEvent subStart = new StartEvent("StartSubProcess");
		subProcess.addRelativeToElement("Hello1", ElementType.SCRIPT_TASK, subStart, new Point(0, 50));
		
		ScriptTask script1 = new ScriptTask("Hello1");
		script1.setScript("Java", "System.out.println(\"x = \" + x);");
		subStart.connectTo(script1);
		subProcess.addRelativeToElement("Hello2", ElementType.SCRIPT_TASK, script1, new Point(130, 0));
		
		
		ScriptTask script2 = new ScriptTask("Hello2");
		script2.setScript("Java", "kcontext.setVariable(\"x\", \"Hello\");");
		script1.connectTo(script2);
		subProcess.addRelativeToElement("EndSubProcess", ElementType.END_EVENT, script2, new Point(0, -50));
		
		EndEvent subEnd = new EndEvent("EndSubProcess");
		script2.connectTo(subEnd);
	}
	
}