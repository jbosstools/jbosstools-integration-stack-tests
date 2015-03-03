package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.Position;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ScriptLanguage;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 * ISSUE - language should be 'http://www.jboss.org/drools/rule' but it's not available.
 */
@ProcessDefinition(name="BPMN2-BoundaryConditionalEventOnTask", project="EditorTestProject")
public class BoundaryConditionalEventOnTaskTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-BoundaryConditionalEventOnTask");
		process.addDataType("String");
		process.addLocalVariable("x", "String");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ElementType.USER_TASK);
		
		UserTask userTask1 = new UserTask("User Task");
		userTask1.addActor("john");
		userTask1.append("User Task 2", ElementType.USER_TASK, Position.NORTH_EAST);
		userTask1.addEvent("Conditional Boundary Event", ElementType.CONDITIONAL_BOUNDARY_EVENT);

		// ISSUE: language should be 'http://www.jboss.org/drools/rule' but it's not available.
		ConditionalBoundaryEvent boundaryEvent = new ConditionalBoundaryEvent("Conditional Boundary Event");
		boundaryEvent.setScript("", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		
		boundaryEvent.append("Condition met", ElementType.SCRIPT_TASK, Position.SOUTH_EAST);
		UserTask userTask2 = new UserTask("User Task 2");
		userTask2.addActor("john");
		userTask2.append("End 1", ElementType.END_EVENT);
		
		ScriptTask scriptTask1 = new ScriptTask("Condition met");
		scriptTask1.setScript(ScriptLanguage.JAVA, "System.out.println(\"Conditional boundary event executed\";)");
		scriptTask1.append("End 2", ElementType.END_EVENT);
	}
	
}