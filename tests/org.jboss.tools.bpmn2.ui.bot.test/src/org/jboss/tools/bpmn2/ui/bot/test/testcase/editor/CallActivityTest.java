package org.jboss.tools.bpmn2.ui.bot.test.testcase.editor;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.Process;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.CallActivity;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.test.JBPM6BaseTest;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;

@ProcessDefinition(name="BPMN2-CallActivity", project="EditorTestProject")
public class CallActivityTest extends JBPM6BaseTest {

	@Override
	public void buildProcessModel() {
		Process process = new Process("BPMN2-CallActivity");
		process.addLocalVariable("x", "String");
		process.addLocalVariable("y", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("CallActivity", ElementType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
		call.setWaitForCompletion(true);
		call.setIndependent(true);
		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new ParameterMapping(new FromVariable("y"), new ToDataInput("subX", "String"), ParameterMapping.Type.INPUT));
		call.addParameterMapping(new ParameterMapping(new FromDataOutput("subY", "String"), new ToVariable("x"), ParameterMapping.Type.OUTPUT));
		
		call.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}
	
}