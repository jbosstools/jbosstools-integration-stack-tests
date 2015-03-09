package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EscalationEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-ConditionalBoundaryEventInterrupting", project="EditorTestProject")
public class ConditionalBoundaryEventInterruptingTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-ConditionalBoundaryEventInterrupting");
		process.addLocalVariable("x", "String");
		
		new StartEvent("StartProcess").delete();

		process.add("Hello", ElementType.SUB_PROCESS);
		
		SubProcess subProcess = new SubProcess("Hello");
		subProcess.append("EndProcess", ElementType.TERMINATE_END_EVENT);
		
		process.add("Start", ElementType.START_EVENT, subProcess, Position.SOUTH);
		new StartEvent("Start").connectTo(subProcess);
		
		subProcess.add("StartSubProcess", ElementType.START_EVENT);
		StartEvent subProcessStartEvent = new StartEvent("StartSubProcess");
		subProcessStartEvent.append("Task", ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		ScriptTask task = new ScriptTask("Task");
		task.setScript(ScriptLanguage.JAVA, "System.out.println(\"Subprocess script\");");
		task.append("EscalationEvent", ElementType.ESCALATION_END_EVENT, Position.SOUTH);
		
		EscalationEndEvent endEvent = new EscalationEndEvent("EscalationEvent");
		endEvent.setEscalation(new Escalation("Timeout", "400"), "x");
		
		subProcess.add("Conditional Boundary Event Process", ElementType.CONDITIONAL_BOUNDARY_EVENT );
		
		ConditionalBoundaryEvent conditionalBoundaryEvent = new ConditionalBoundaryEvent("Conditional Boundary Event Process");
		conditionalBoundaryEvent.setScript("", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		conditionalBoundaryEvent.append("Goodbye", ElementType.SCRIPT_TASK, Position.NORTH);
		
		ScriptTask scriptTask = new ScriptTask("Goodbye");
		scriptTask.setScript(ScriptLanguage.JAVA, "System.out.println(\"Condition met\");");
		scriptTask.append("EndProcess2", ElementType.END_EVENT);
		
		// fix connections - the framework code does not create them because editPart is
		// out of date when added to the editor boundary.
		new ConditionalBoundaryEvent("Conditional Boundary Event Process").connectTo(new ScriptTask("Goodbye"));
		new ScriptTask("Goodbye").connectTo(new EndEvent("EndProcess2"));
	}
	
}