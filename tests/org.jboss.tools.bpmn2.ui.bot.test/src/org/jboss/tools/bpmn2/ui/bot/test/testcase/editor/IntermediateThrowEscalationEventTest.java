package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Escalation;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.EscalationIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-IntermediateThrowEscalationEvent", project="EditorTestProject")
public class IntermediateThrowEscalationEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Escalation myEscalation = new Escalation("myEscalation", "java.lang.Object");
		new org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process("BPMN2-IntermediateThrowEscalationEvent").addEscalation(myEscalation);
		StartEvent start = new StartEvent("StartProcess");
		start.append("Escalation Event", ElementType.ESCALATION_INTERMEDIATE_THROW_EVENT);

		EscalationIntermediateThrowEvent ithrow = new EscalationIntermediateThrowEvent("Escalation Event");
		ithrow.setEscalation(myEscalation);
		ithrow.append("EndProcess", ElementType.END_EVENT);
	}
	
}