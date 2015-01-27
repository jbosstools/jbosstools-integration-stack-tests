package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ParameterMapping;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.CallActivity;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-CallActivity.bpmn2",
							saveAs="BPMN2-CallActivity.bpmn2")
public class ComplexCallActivityTest extends JBPM6ComplexTest {
	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		
		start.append("CallActivity", ElementType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
		call.setWaitForCompletion(true);
		call.setIndependent(true);
		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new ParameterMapping(new FromVariable(VARIABLE2), new ToDataInput("subX", "String"), ParameterMapping.Type.INPUT));
		call.addParameterMapping(new ParameterMapping(new FromDataOutput("subY", "String"), new ToVariable(VARIABLE1), ParameterMapping.Type.OUTPUT));
		
		call.connectTo(new EndEvent("EndProcess"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2CallActivity");
	    JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
