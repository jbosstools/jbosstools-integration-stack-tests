package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.IntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-IntermediateThrowEventNone", project="EditorTestProject")
public class IntermediateThrowEventNoneTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("None Event", ElementType.INTERMEDIATE_THROW_EVENT);

		IntermediateThrowEvent ithrow = new IntermediateThrowEvent("None Event");
		ithrow.append("EndProcess", ElementType.END_EVENT);
	}
	
}