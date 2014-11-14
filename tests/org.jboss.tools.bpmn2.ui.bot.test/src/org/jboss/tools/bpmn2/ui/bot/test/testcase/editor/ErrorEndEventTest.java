package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ErrorRef;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.ErrorEndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@ProcessDefinition(name="BPMN2-ErrorEndEvent", project="EditorTestProject")
public class ErrorEndEventTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-ErrorEndEvent");
		process.addLocalVariable(VARIABLE1, "String");
		process.addError("SimpleError", "error", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("ErrorEvent", ElementType.ERROR_END_EVENT);
		
		ErrorEndEvent end = new ErrorEndEvent("ErrorEvent");
		end.setErrorEvent(new ErrorRef("SimpleError", "error", "String"), VARIABLE1);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2ErrorEndEvent");		
		JbpmAssertions.assertProcessInstanceAborted(processInstance, kSession);
	}
	
}