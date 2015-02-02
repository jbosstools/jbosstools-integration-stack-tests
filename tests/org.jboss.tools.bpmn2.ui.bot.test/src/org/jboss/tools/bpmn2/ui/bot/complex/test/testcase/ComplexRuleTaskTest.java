package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.endevents.TerminateEndEvent;
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
							openFile="BaseBPMN2-RuleTask.bpmn2",
							saveAs="BPMN2-RuleTask.bpmn2")
public class ComplexRuleTaskTest extends JBPM6ComplexTest{

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		
		BusinessRuleTask rule = (BusinessRuleTask) start.append("Rule", ElementType.BUSINESS_RULE_TASK);
		rule.setRuleFlowGroup("myRules");
		rule.connectTo(new TerminateEndEvent("EndProcess"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2RuleTask");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
