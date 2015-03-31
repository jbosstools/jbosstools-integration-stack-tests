package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;

@JBPM6ComplexTestDefinition(projectName="JBPM6ComplexTest",
							importFolder="resources/bpmn2/model/base",
							openFile="BaseBPMN2-XPathExpression.bpmn2",
							saveAs="BPMN2-XPathExpression.bpmn2")
public class ComplexXPathExpressionTest extends JBPM6ComplexTest{

	@TestPhase(phase=Phase.MODEL)
	public void model() {
		ExclusiveGateway splitGw = new ExclusiveGateway("Split");
		splitGw.setCondition("Split -> Task1", "XPath 1.0", "count($instanceMetadata/instanceMetadata/user[@approved='true']) = 1");
		splitGw.setCondition("Split -> Task2", "XPath 1.0", "count($instanceMetadata/instanceMetadata/user[@approved='false']) = 1");
	}
	
	@TestPhase(phase=Phase.RUN)
	public void run(KieSession kSession) {
		ProcessInstance processInstance = kSession.startProcess("BPMN2XPathExpression");
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
	}
}
