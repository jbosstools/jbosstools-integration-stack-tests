package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.IntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;

@ProcessDefinition(name="BPMN2-IntermediateThrowEventNone", project="EditorTestProject")
public class IntermediateThrowEventNoneTest extends JBPM6BaseTest {

	private static final String VAR_NAME = "x";
	private static final String SIG_NAME = "SomeSignal";
	
	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-IntermediateThrowEventNone");
		process.addLocalVariable(VAR_NAME, "String");
		process.addSignal(SIG_NAME);
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("None Event", ElementType.INTERMEDIATE_THROW_EVENT);

		IntermediateThrowEvent ithrow = new IntermediateThrowEvent("None Event");
		ithrow.append("EndProcess", ElementType.END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		// TODO Auto-generated method stub
		
	}
	
}