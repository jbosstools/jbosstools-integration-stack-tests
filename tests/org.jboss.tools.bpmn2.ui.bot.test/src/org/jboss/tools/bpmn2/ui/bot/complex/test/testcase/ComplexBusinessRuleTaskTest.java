package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.ElementType;
import org.jboss.tools.bpmn2.reddeer.editor.jbpm.activities.BusinessRuleTask;
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
							openFile="BaseBPMN2-BusinessRuleTask.bpmn2",
							saveAs="BPMN2-BusinessRuleTask.bpmn2")
public class ComplexBusinessRuleTaskTest extends JBPM6ComplexTest{

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Business Rule Task", ElementType.BUSINESS_RULE_TASK);
		
		BusinessRuleTask task = new BusinessRuleTask("Business Rule Task");
		task.setRuleFlowGroup("MyRuleFlowGroup");
		task.connectTo(new EndEvent("EndProcess"));
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2BusinessRuleTask");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
