package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.EscalationBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EscalationEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.Direction;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;

/**
 * ISSUE - language should be 'http://www.jboss.org/drools/rule' but it's not available.
 */
@ProcessDefinition(name="BPMN2-BoundaryEscalationEventOnTask", project="EditorTestProject")
public class BoundaryEscalationEventOnTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-BoundaryEscalationEventOnTask");
		process.addLocalVariable("varA", "String");
		process.addEscalation("MyEsc", "MyEscalation", "String");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Split", ElementType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway("Split");
		gateway.setDirection(Direction.DIVERGING);
		gateway.append("User Task With Escalation", ElementType.USER_TASK, Position.NORTH_EAST);
		gateway.append("User Task", ElementType.USER_TASK, Position.SOUTH_EAST);

		UserTask userTask1 = new UserTask("User Task With Escalation");
		userTask1.addActor("John");
		userTask1.append("EscalationEndProcess", ElementType.ESCALATION_END_EVENT);

		EscalationEndEvent escalationEndEvent = new EscalationEndEvent("EscalationEndProcess");
		escalationEndEvent.setEscalation(new Escalation("MyEsc","MyEscalation","String"), "varA");
	
		UserTask userTask2 = new UserTask("User Task");
		userTask2.addActor("Mary");
		userTask2.append("EndProcess", ElementType.END_EVENT);
		userTask2.addEvent("Escalation Boundary Event", ElementType.ESCALATION_BOUNDARY_EVENT);

		EscalationBoundaryEvent boundaryEvent = new EscalationBoundaryEvent("Escalation Boundary Event");
		boundaryEvent.setEscalation(new Escalation("MyEsc","MyEscalation", "String"), "varA");
		boundaryEvent.append("Script Task", ElementType.SCRIPT_TASK, Position.SOUTH);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.setScript("Java", "System.out.println(\"Escalation handled\");");
		scriptTask.append("EndProcess2", ElementType.END_EVENT);
	}
	
}