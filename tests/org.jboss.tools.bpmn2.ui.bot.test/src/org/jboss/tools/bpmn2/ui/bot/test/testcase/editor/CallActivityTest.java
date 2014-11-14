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
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jboss.tools.bpmn2.ui.bot.test.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;


@ProcessDefinition(name="BPMN2-CallActivity", project="EditorTestProject")
public class CallActivityTest extends JBPM6BaseTest {
	
	private static final String MODEL_NAME = "BPMN2-CallActivity";
	
	@Override
	public void buildProcessModel() {
		Process process = new Process(MODEL_NAME);
		process.addLocalVariable(VARIABLE1, "String");
		process.addLocalVariable(VARIABLE2, "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("CallActivity", ElementType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
		call.setWaitForCompletion(true);
		call.setIndependent(true);
		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new ParameterMapping(new FromVariable(VARIABLE2), new ToDataInput("subX", "String"), ParameterMapping.Type.INPUT));
		call.addParameterMapping(new ParameterMapping(new FromDataOutput("subY", "String"), new ToVariable(VARIABLE1), ParameterMapping.Type.OUTPUT));
		
		call.append("EndProcess", ElementType.TERMINATE_END_EVENT);
	}

	@Override
	public void assertRunOfProcessModel(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess(MODEL_NAME.replace("-", "").replace("_", ""));
	    JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
	
}