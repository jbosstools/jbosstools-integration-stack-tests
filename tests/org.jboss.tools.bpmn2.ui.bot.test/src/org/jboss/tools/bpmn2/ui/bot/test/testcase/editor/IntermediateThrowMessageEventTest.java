package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ConstructType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.MessageIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-IntermediateThrowMessageEvent", project="EditorTestProject")
public class IntermediateThrowMessageEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		BPMN2Process process = new BPMN2Process("BPMN2-IntermediateThrowMessageEvent");
		process.addMessage("_2_Message", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Message Event", ConstructType.MESSAGE_INTERMEDIATE_THROW_EVENT);

		MessageIntermediateThrowEvent ithrow = new MessageIntermediateThrowEvent("Message Event");
		ithrow.setMessage("_2_Message", "String");
		ithrow.append("EndProcess", ConstructType.END_EVENT);
	}
	
}