package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import java.util.Arrays;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.TriggeredNodesListener;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-ConditionalBoundaryEventInterrupting", project="EditorTestProject")
public class ConditionalBoundaryEventInterruptingTest extends JBPM6BaseTest {

	private static final String END_PROCESS2 = "EndProcess2";
	private static final String HANDLER = "Handler";
	private static final String END_SUB = "EndSubProcess";
	private static final String INCREMENTOR = "Incrementor";
	private static final String START_SUB = "StartSubProcess";
	private static final String END_PROCESS = "EndProcess";
	private static final String SUB_PROCESS = "IncrementorSubProcess";
	private static final String START_PROCESS = "StartProcess";

	@Override
	public void buildProcessModel() {
		
		StartEvent startEvent = new StartEvent(START_PROCESS);
		startEvent.append(SUB_PROCESS, ElementType.SUB_PROCESS, Position.SOUTH);
		
		SubProcess subProcess = new SubProcess(SUB_PROCESS);
		subProcess.addLocalVariable(VARIABLE1, "Integer");
		subProcess.setOnEntryScript("Java", VARIABLE1 + " = 0;");
		subProcess.append(END_PROCESS, ElementType.TERMINATE_END_EVENT);
		subProcess.add(START_SUB, ElementType.START_EVENT);
		
		StartEvent subProcessStartEvent = new StartEvent(START_SUB);
		subProcessStartEvent.append(INCREMENTOR, ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		ScriptTask task = new ScriptTask(INCREMENTOR);
		task.setScript("Java", VARIABLE1 + " = " + VARIABLE1 + " + 10;");
		task.append(END_SUB, ElementType.END_EVENT, Position.NORTH);
		
		subProcess.add("Conditional Boundary Event Process", ElementType.CONDITIONAL_BOUNDARY_EVENT);
		
		ConditionalBoundaryEvent conditionalBoundaryEvent = new ConditionalBoundaryEvent("Conditional Boundary Event Process");
		conditionalBoundaryEvent.setScript("Java", "return " + VARIABLE1 + " > 5;");
		conditionalBoundaryEvent.append(HANDLER, ElementType.SCRIPT_TASK, Position.NORTH);
		
		ScriptTask scriptTask = new ScriptTask(HANDLER);
		scriptTask.setScript("Java", "System.out.println(\"Print handling\");");
		scriptTask.append(END_PROCESS2, ElementType.END_EVENT);
		
		// fix connections - the framework code does not create them because editPart is
		// out of date when added to the editor boundary.
		new ConditionalBoundaryEvent("Conditional Boundary Event Process").connectTo(new ScriptTask(HANDLER));
		new ScriptTask(HANDLER).connectTo(new EndEvent(END_PROCESS2));
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2ConditionalBoundaryEventInterrupting");
		TriggeredNodesListener triggered = new TriggeredNodesListener(
				Arrays.asList(START_PROCESS, SUB_PROCESS, START_SUB, INCREMENTOR, 
				END_SUB, HANDLER, END_PROCESS2), 
				Arrays.asList(END_PROCESS));
		kSession.addEventListener(triggered);
		
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}