package org.jboss.tools.bpmn2.ui.bot.complex.test.testcase;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.tools.bpmn2.reddeer.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTest;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase;
import org.jboss.tools.bpmn2.ui.bot.complex.test.JBPM6ComplexTestDefinitionRequirement.JBPM6ComplexTestDefinition;
import org.jboss.tools.bpmn2.ui.bot.complex.test.TestPhase.Phase;
import org.jboss.tools.bpmn2.ui.bot.test.jbpm.JbpmAssertions;
import org.jbpm.workflow.instance.WorkflowProcessInstance;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
	public void run(KieSession kSession) throws SAXException, IOException, ParserConfigurationException {
		
		Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new ByteArrayInputStream(
                        "<instanceMetadata><user approved=\"false\" /></instanceMetadata>"
                                .getBytes()));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("instanceMetadata", document);
        params.put(VARIABLE1, 5);
		
		ProcessInstance processInstance = kSession.startProcess("BPMN2XPathExpression", params);
		JbpmAssertions.assertProcessInstanceCompleted(processInstance, kSession);
		
		assertEquals(10, ((WorkflowProcessInstance)processInstance).getVariable(VARIABLE1));
	}
}
