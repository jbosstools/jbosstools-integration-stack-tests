package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.throwevents.LinkIntermediateThrowEvent;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-LinkEvent.bpmn2",
							saveAs="BPMN2-LinkEvent.bpmn2")
public class ComplexLinkEventTest extends JBPM6ComplexTest {

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		ScriptTask middleStep = new ScriptTask("MiddleStep");
		LinkIntermediateThrowEvent throwEventTwo = 
			(LinkIntermediateThrowEvent)middleStep.append("MiddleStep", ElementType.LINK_INTERMEDIATE_THROW_EVENT);
		
		throwEventTwo.setTarget("go_to", "CatchEnd/go_to");
	}
	
	@TestPhase(phase=Phase.RUN)
	public void runFirstVariant(KieSession kSession) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("VariableOne", 10);
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession.startProcess("BPMN2LinkEvent", args);
		assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
	}
	
	@TestPhase(phase=Phase.RUN)
	public void runSecondVariant(KieSession kSession) {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("VariableOne", 11);
		WorkflowProcessInstance processInstance = (WorkflowProcessInstance) kSession.startProcess("BPMN2LinkEvent", args);
		assertEquals(ProcessInstance.STATE_COMPLETED, processInstance.getState());
	}
}
